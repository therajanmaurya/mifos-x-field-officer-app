/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.feature.auth

import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kpt.core.base.common.manager.DispatcherManager
import kpt.core.base.store.mutation.BlockReason
import kpt.core.base.store.mutation.MutationResult
import kpt.core.data.auth.AuthCommandRepository
import kpt.core.datastore.prefs.ProjectPreferencesRepository
import kpt.core.datastore.prefs.ProjectPreferencesRepositoryImpl
import kpt.core.datastore.prefs.UserPreferencesRepositoryImpl
import kpt.core.network.mifos.auth.dto.PostAuthenticationRequest
import kpt.core.network.mifos.auth.dto.PostAuthenticationResponse
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest fun setUp() = Dispatchers.setMain(dispatcher)

    @AfterTest fun tearDown() = Dispatchers.resetMain()

    /** Records whether the server was reached, so local validation can be proven to short-circuit. */
    private class FakeAuth(private val result: MutationResult<PostAuthenticationResponse>) : AuthCommandRepository {
        var calls = 0
        override suspend fun authenticate(
            postAuthenticationRequest: PostAuthenticationRequest,
        ): MutationResult<PostAuthenticationResponse> {
            calls++
            return result
        }
    }

    private val dispatchers = object : DispatcherManager {
        override val default: CoroutineDispatcher = Dispatchers.Unconfined
        override val main: MainCoroutineDispatcher = Dispatchers.Main
        override val io: CoroutineDispatcher = Dispatchers.Unconfined
        override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
        override val appScope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined)
    }

    /**
     * The REAL preferences seam over in-memory settings rather than a hand-written fake: the
     * interface inherits 22 framework members, so a fake would be 29 stubs that prove nothing, and
     * this additionally exercises the persistence the ViewModel depends on.
     */
    private fun preferences(): ProjectPreferencesRepository {
        val plain = MapSettings()
        val secure = MapSettings()
        return ProjectPreferencesRepositoryImpl(
            delegate = UserPreferencesRepositoryImpl(plain, secure, dispatchers),
            plainSettings = plain,
            secureSettings = secure,
            dispatcher = dispatchers,
        )
    }

    private fun response(authenticated: Boolean) = PostAuthenticationResponse(
        authenticated = authenticated,
        base64EncodedAuthenticationKey = "a2V5",
        userId = 7,
        officeId = 3,
        officeName = "Head Office",
        permissions = listOf("ALL_FUNCTIONS"),
    )

    private fun submitValid(model: LoginViewModel) {
        model.actionChannel.trySend(LoginAction.UsernameChanged("officer"))
        model.actionChannel.trySend(LoginAction.PasswordChanged("password1"))
        model.actionChannel.trySend(LoginAction.Submit)
    }

    @Test
    fun shortUsernameIsRejectedWithoutCallingTheServer() = runTest(dispatcher) {
        val auth = FakeAuth(MutationResult.Applied(response(true), synced = true))
        val model = LoginViewModel(auth, preferences())
        model.actionChannel.trySend(LoginAction.UsernameChanged("abc"))
        model.actionChannel.trySend(LoginAction.PasswordChanged("longenough"))
        model.actionChannel.trySend(LoginAction.Submit)
        runCurrent()
        assertEquals(LoginError.UsernameTooShort, model.stateFlow.value.error)
        // Validating locally exists to save a round trip the officer may not be able to make.
        assertEquals(0, auth.calls)
    }

    @Test
    fun offlineIsReportedAsOfflineNotAsBadCredentials() = runTest(dispatcher) {
        val model = LoginViewModel(FakeAuth(MutationResult.Blocked(BlockReason.OFFLINE)), preferences())
        submitValid(model)
        runCurrent()
        // Different problems: one is solved by finding signal, the other by retyping a password.
        assertEquals(LoginError.Offline, model.stateFlow.value.error)
    }

    @Test
    fun authenticatedFalseIsAFailedLoginEvenThoughTheCallSucceeded() = runTest(dispatcher) {
        // Fineract answers 200 with authenticated=false for a bad credential rather than a 4xx,
        // so a successful CALL is not a successful LOGIN.
        val model = LoginViewModel(FakeAuth(MutationResult.Applied(response(false), synced = true)), preferences())
        submitValid(model)
        runCurrent()
        assertEquals(LoginError.Rejected, model.stateFlow.value.error)
    }

    @Test
    fun successPersistsTheFineractUserAndSignalsTheShell() = runTest(dispatcher) {
        val prefs = preferences()
        val model = LoginViewModel(FakeAuth(MutationResult.Applied(response(true), synced = true)), prefs)
        var loggedIn = false
        val job = launch { model.eventFlow.collect { if (it is LoginEvent.LoggedIn) loggedIn = true } }
        submitValid(model)
        runCurrent()
        assertNull(model.stateFlow.value.error)
        assertEquals("officer", prefs.fineractUser.value.username)
        assertEquals("a2V5", prefs.fineractUser.value.base64EncodedAuthenticationKey)
        assertTrue(prefs.fineractUser.value.isAuthenticated)
        // The token the network layer reads must not lag the user record.
        assertEquals("Basic a2V5", prefs.authToken)
        assertTrue(loggedIn)
        job.cancel()
    }
}
