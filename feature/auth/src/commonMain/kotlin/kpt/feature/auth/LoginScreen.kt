/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kpt.core.designsystem.component.MifosAndroidClientIcon
import kpt.core.designsystem.component.MifosOutlinedTextField
import kpt.core.designsystem.icon.MifosIcons
import kpt.feature.auth.generated.resources.Res
import kpt.feature.auth.generated.resources.feature_auth_enter_credentials
import kpt.feature.auth.generated.resources.feature_auth_error_login_failed
import kpt.feature.auth.generated.resources.feature_auth_error_offline
import kpt.feature.auth.generated.resources.feature_auth_error_password_length
import kpt.feature.auth.generated.resources.feature_auth_error_username_length
import kpt.feature.auth.generated.resources.feature_auth_login
import kpt.feature.auth.generated.resources.feature_auth_mifos_logo
import kpt.feature.auth.generated.resources.feature_auth_password
import kpt.feature.auth.generated.resources.feature_auth_update_server_config
import kpt.feature.auth.generated.resources.feature_auth_username
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * Sign-in screen, ported from `6b66e8a43:feature/auth/.../login/LoginScreen.kt` (258 lines).
 *
 * The file this replaces was AUTHORED rather than ported and had lost four things the original
 * carried: the Mifos logo, the `Scaffold`/`SnackbarHost` (so a server error had nowhere to surface),
 * the "please enter your credentials" prompt, and the server-config entry point — without which a
 * first-run user cannot set the Fineract URL and so can never sign in at all. See
 * `feature/auth/MIGRATION.md`.
 *
 * Two deliberate improvements over the original, both recorded in the ledger:
 *
 *  - **Validation errors render per field**, not in the Snackbar. The original pushed every message
 *    through `snackbarHostState.showSnackbar`, including "invalid username length", so a field
 *    error appeared detached from the field it concerned. The typed reasons from
 *    `kpt.core.domain.validation` make per-field attribution possible.
 *  - **Offline is distinguishable from rejected.** `DataState` could not express it; `MutationResult`
 *    `Blocked(OFFLINE)` can.
 */
@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
    onUpdateServerConfig: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                LoginEvent.LoggedIn -> onLoggedIn()
            }
        }
    }

    LoginContent(
        state = state,
        onAction = { viewModel.actionChannel.trySend(it) },
        onUpdateServerConfig = onUpdateServerConfig,
        modifier = modifier,
    )
}

@Composable
internal fun LoginContent(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    onUpdateServerConfig: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Only transport-level failures reach the Snackbar; field problems render on their field.
    val transportError = state.error?.takeIf { it.isTransportError }
    val transportMessage = transportError?.let { stringResource(it.message) }
    LaunchedEffect(transportError, transportMessage) {
        if (transportMessage != null) snackbarHostState.showSnackbar(transportMessage)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .testTag(LoginTestTags.SCREEN),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Server configuration first, as in the original: a first-run user has no Fineract URL
            // yet, so sign-in cannot succeed until this is reachable BEFORE they try.
            FilledTonalButton(
                onClick = onUpdateServerConfig,
                enabled = !state.isSubmitting,
                colors = ButtonDefaults.filledTonalButtonColors(),
                modifier = Modifier.fillMaxWidth().testTag(LoginTestTags.SERVER_CONFIG),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(text = stringResource(Res.string.feature_auth_update_server_config))
                    Icon(imageVector = MifosIcons.ArrowForward, contentDescription = null)
                }
            }

            MifosAndroidClientIcon(
                imageVector = painterResource(Res.drawable.feature_auth_mifos_logo),
                modifier = Modifier.testTag(LoginTestTags.LOGO),
            )

            Text(
                text = stringResource(Res.string.feature_auth_enter_credentials),
                style = MaterialTheme.typography.bodyMedium,
            )

            MifosOutlinedTextField(
                value = state.username,
                onValueChange = { onAction(LoginAction.UsernameChanged(it)) },
                label = stringResource(Res.string.feature_auth_username),
                leadingIcon = MifosIcons.Person,
                enabled = !state.isSubmitting,
                isError = state.error == LoginError.UsernameTooShort,
                errorText = (state.error == LoginError.UsernameTooShort)
                    .takeIf { it }
                    ?.let { stringResource(Res.string.feature_auth_error_username_length) },
                modifier = Modifier.fillMaxWidth().testTag(LoginTestTags.USERNAME),
            )

            MifosOutlinedTextField(
                value = state.password,
                onValueChange = { onAction(LoginAction.PasswordChanged(it)) },
                label = stringResource(Res.string.feature_auth_password),
                leadingIcon = MifosIcons.Lock,
                enabled = !state.isSubmitting,
                keyboardType = KeyboardType.Password,
                isError = state.error == LoginError.PasswordTooShort,
                errorText = (state.error == LoginError.PasswordTooShort)
                    .takeIf { it }
                    ?.let { stringResource(Res.string.feature_auth_error_password_length) },
                modifier = Modifier.fillMaxWidth().testTag(LoginTestTags.PASSWORD),
            )

            if (state.isSubmitting) {
                CircularProgressIndicator(Modifier.testTag(LoginTestTags.PROGRESS))
            } else {
                Button(
                    onClick = { onAction(LoginAction.Submit) },
                    modifier = Modifier.fillMaxWidth().testTag(LoginTestTags.SUBMIT),
                ) {
                    Text(stringResource(Res.string.feature_auth_login))
                }
            }
        }
    }
}

/**
 * True for failures that are about reaching the server, not about what the user typed.
 *
 * Drives the Snackbar-vs-field split: a transport failure has no field to attach to, while
 * "invalid username length" does.
 */
private val LoginError.isTransportError: Boolean
    get() = when (this) {
        LoginError.Rejected, LoginError.Offline -> true
        LoginError.UsernameTooShort, LoginError.PasswordTooShort -> false
    }

private val LoginError.message: StringResource
    get() = when (this) {
        LoginError.UsernameTooShort -> Res.string.feature_auth_error_username_length
        LoginError.PasswordTooShort -> Res.string.feature_auth_error_password_length
        LoginError.Rejected -> Res.string.feature_auth_error_login_failed
        LoginError.Offline -> Res.string.feature_auth_error_offline
    }
