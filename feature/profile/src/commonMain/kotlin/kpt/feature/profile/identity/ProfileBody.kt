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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kpt.feature.profile.TestTags
import kpt.feature.profile.generated.resources.Res
import kpt.feature.profile.generated.resources.feature_profile_office
import kpt.feature.profile.generated.resources.feature_profile_server
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * The Profile tab's body — signed-in officer identity. Sign-out is at the bottom of Settings.
 *
 * Supplied through `BackboneRegistry.profileBody`, another seam that was EMPTY.
 */
@Composable
fun ProfileBody(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.profile.collectAsStateWithLifecycle()

    ProfileBodyContent(state = state, modifier = modifier)
}

/** Stateless half — see `SearchBodyContent` for why the split exists. */
@Composable
internal fun ProfileBodyContent(
    state: ProfileState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag(TestTags.Profile.BODY),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = state.username,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.testTag(TestTags.Profile.USERNAME),
        )
        Text(
            text = stringResource(Res.string.feature_profile_office, state.officeName),
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            // Which server the officer is signed in to is identity, not decoration: a field officer
            // switching between a demo and a production instance has no other way to tell.
            text = stringResource(Res.string.feature_profile_server, state.instanceUrl),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.testTag(TestTags.Profile.SERVER),
        )

    }
}
