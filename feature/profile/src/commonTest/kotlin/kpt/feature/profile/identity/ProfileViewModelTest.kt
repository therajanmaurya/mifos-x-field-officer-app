/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.feature.profile.identity

import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kpt.core.base.common.manager.DispatcherManager
import kpt.core.data.testing.FakeServerConfigRepository
import kpt.core.datastore.prefs.ProjectPreferencesRepositoryImpl
import kpt.core.datastore.prefs.UserPreferencesRepositoryImpl
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kpt.core.model.utils.getInstanceUrl

/** Profile is identity only (sign-out moved to Settings): it shows the session and the server. */
@OptIn(kpt.core.data.testing.ExperimentalDataTestingApi::class)
class ProfileViewModelTest {

    @BeforeTest fun setUp() = Dispatchers.setMain(Dispatchers.Unconfined)

    @AfterTest fun tearDown() = Dispatchers.resetMain()

    private val dispatchers = object : DispatcherManager {
        override val default: CoroutineDispatcher = Dispatchers.Unconfined
        override val main: MainCoroutineDispatcher = Dispatchers.Main
        override val io: CoroutineDispatcher = Dispatchers.Unconfined
        override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
        override val appScope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined)
    }

    private fun preferences() = run {
        val plain = MapSettings()
        val secure = MapSettings()
        ProjectPreferencesRepositoryImpl(
            delegate = UserPreferencesRepositoryImpl(plain, secure, dispatchers),
            plainSettings = plain,
            secureSettings = secure,
            dispatcher = dispatchers,
        )
    }

    @Test
    fun theServerShownIsTheMainNetworkConfig() = runTest {
        val vm = ProfileViewModel(preferences(), FakeServerConfigRepository())
        backgroundScope.launch { vm.profile.collect {} }
        testScheduler.runCurrent()
        assertEquals(FakeServerConfigRepository.TEST_DEFAULT.getInstanceUrl(), vm.profile.value.instanceUrl)
    }
}
