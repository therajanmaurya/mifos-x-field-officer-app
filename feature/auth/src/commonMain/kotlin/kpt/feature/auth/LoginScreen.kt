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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kpt.feature.auth.generated.resources.Res
import kpt.feature.auth.generated.resources.feature_auth_error_login_failed
import kpt.feature.auth.generated.resources.feature_auth_error_offline
import kpt.feature.auth.generated.resources.feature_auth_error_password_length
import kpt.feature.auth.generated.resources.feature_auth_error_username_length
import kpt.feature.auth.generated.resources.feature_auth_login
import kpt.feature.auth.generated.resources.feature_auth_password
import kpt.feature.auth.generated.resources.feature_auth_username
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onLoggedIn: () -> Unit,
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
        modifier = modifier,
    )
}

@Composable
internal fun LoginContent(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp).testTag(LoginTestTags.SCREEN),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        OutlinedTextField(
            value = state.username,
            onValueChange = { onAction(LoginAction.UsernameChanged(it)) },
            label = { Text(stringResource(Res.string.feature_auth_username)) },
            singleLine = true,
            enabled = !state.isSubmitting,
            modifier = Modifier.fillMaxWidth().testTag(LoginTestTags.USERNAME),
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = { onAction(LoginAction.PasswordChanged(it)) },
            label = { Text(stringResource(Res.string.feature_auth_password)) },
            singleLine = true,
            enabled = !state.isSubmitting,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().testTag(LoginTestTags.PASSWORD),
        )

        state.error?.let { error ->
            Text(
                text = stringResource(error.message()),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.testTag(LoginTestTags.ERROR),
            )
        }

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

/** Offline is its own message — the officer's next step is "find signal", not "retype the password". */
private fun LoginError.message() = when (this) {
    LoginError.UsernameTooShort -> Res.string.feature_auth_error_username_length
    LoginError.PasswordTooShort -> Res.string.feature_auth_error_password_length
    LoginError.Rejected -> Res.string.feature_auth_error_login_failed
    LoginError.Offline -> Res.string.feature_auth_error_offline
}
