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

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kpt.core.base.store.mutation.BlockReason
import kpt.core.base.store.mutation.MutationResult
import kpt.core.base.ui.viewmodel.BaseViewModel
import kpt.core.data.auth.AuthCommandRepository
import kpt.core.datastore.prefs.ProjectPreferencesRepository
import kpt.core.model.objects.users.User
import kpt.core.network.mifos.auth.dto.PostAuthenticationRequest
import kpt.core.network.mifos.auth.dto.PostAuthenticationResponse

/**
 * Sign-in against the configured Fineract instance.
 *
 * Authentication is the one write that CANNOT be optimistic — there is no local credential store to
 * check against, so [AuthCommandRepository] runs it `OnlineRequired` and an offline attempt comes
 * back [MutationResult.Blocked]. That is surfaced as its own message rather than a generic failure:
 * "wrong password" and "no signal" are different problems and a field officer acts on them
 * differently.
 */
class LoginViewModel(
    private val auth: AuthCommandRepository,
    private val preferences: ProjectPreferencesRepository,
) : BaseViewModel<LoginState, LoginEvent, LoginAction>(LoginState()) {

    override fun handleAction(action: LoginAction) {
        when (action) {
            is LoginAction.UsernameChanged ->
                mutableStateFlow.value = state.copy(username = action.value, error = null)

            is LoginAction.PasswordChanged ->
                mutableStateFlow.value = state.copy(password = action.value, error = null)

            LoginAction.Submit -> submit()
        }
    }

    private fun submit() {
        val username = state.username.trim()
        val password = state.password
        // Validated here rather than on the server: a round trip to be told the field is too short
        // is a round trip a field officer often cannot afford.
        val validation = validate(username, password)
        if (validation != null) {
            mutableStateFlow.value = state.copy(error = validation)
            return
        }
        mutableStateFlow.value = state.copy(isSubmitting = true, error = null)
        viewModelScope.launch {
            when (val result = auth.authenticate(PostAuthenticationRequest(username = username, password = password))) {
                is MutationResult.Applied -> onAuthenticated(result.value, username, password)
                is MutationResult.Blocked ->
                    fail(if (result.reason == BlockReason.OFFLINE) LoginError.Offline else LoginError.Rejected)
                is MutationResult.Failed -> fail(LoginError.Rejected)
                is MutationResult.Conflicted -> fail(LoginError.Rejected)
            }
        }
    }

    /**
     * Fineract answers 200 with `authenticated = false` for a bad credential rather than a 4xx, so
     * a successful CALL is not a successful LOGIN — the flag has to be checked explicitly.
     */
    private suspend fun onAuthenticated(
        response: PostAuthenticationResponse,
        username: String,
        password: String,
    ) {
        if (response.authenticated != true) {
            fail(LoginError.Rejected)
            return
        }
        preferences.updateFineractUser(
            User(
                username = username,
                password = password,
                userId = response.userId ?: 0,
                base64EncodedAuthenticationKey = response.base64EncodedAuthenticationKey,
                isAuthenticated = true,
                officeId = response.officeId ?: 0,
                officeName = response.officeName,
                permissions = response.permissions.orEmpty(),
            ),
        )
        mutableStateFlow.value = state.copy(isSubmitting = false)
        sendEvent(LoginEvent.LoggedIn)
    }

    private fun fail(error: LoginError) {
        mutableStateFlow.value = state.copy(isSubmitting = false, error = error)
    }

    private fun validate(username: String, password: String): LoginError? = when {
        username.length < MIN_USERNAME -> LoginError.UsernameTooShort
        password.length < MIN_PASSWORD -> LoginError.PasswordTooShort
        else -> null
    }

    private companion object {
        const val MIN_USERNAME = 5
        const val MIN_PASSWORD = 6
    }
}

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isSubmitting: Boolean = false,
    val error: LoginError? = null,
)

/** Distinct causes, because the officer's next step differs for each. */
enum class LoginError { UsernameTooShort, PasswordTooShort, Rejected, Offline }

sealed interface LoginAction {
    data class UsernameChanged(val value: String) : LoginAction
    data class PasswordChanged(val value: String) : LoginAction
    data object Submit : LoginAction
}

sealed interface LoginEvent {
    data object LoggedIn : LoginEvent
}
