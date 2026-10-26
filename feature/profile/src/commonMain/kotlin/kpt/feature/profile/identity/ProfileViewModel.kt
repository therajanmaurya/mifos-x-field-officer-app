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

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kpt.core.base.store.screen.ScreenState
import kpt.core.base.ui.viewmodel.BaseViewModel
import kpt.core.data.serverconfig.ServerConfigRepository
import kpt.core.datastore.prefs.ProjectPreferencesRepository
import kpt.core.model.utils.ServerConfig
import kpt.core.model.utils.getInstanceUrl

/**
 * Signed-in officer identity — the Profile tab's content. Sign-out moved to the bottom of Settings
 * (2026-10-25, `feature/settings/.../signout/`).
 *
 * There is no original to port: the field-officer app had no Profile tab. The template backbone
 * always renders one (spec §6), so this is a deliberate NEW surface, kept to identity plus sign-out
 * so "new surface inside a migration" stays defensible (`feature/profile/MIGRATION.md` §7).
 *
 * Identity is read from the session persisted at login.
 */
class ProfileViewModel(
    private val project: ProjectPreferencesRepository,
    serverConfigs: ServerConfigRepository,
) : BaseViewModel<ProfileState, Nothing, Nothing>(ProfileState()) {

    /**
     * What the Profile tab shows: the identity and the server, DERIVED — the session persisted at sign-in (readable with no network) and
     * the main runtime network config. No `init {}` collector.
     */
    val profile: StateFlow<ProfileState> = combine(
        stateFlow,
        project.fineractUser,
        serverConfigs.serverConfigStream(viewModelScope).state.map { (it as? ScreenState.Content<ServerConfig>)?.data },
    ) { ui, fineract, server ->
        ui.copy(
            username = fineract.username.orEmpty(),
            officeName = fineract.officeName.orEmpty(),
            instanceUrl = server?.getInstanceUrl().orEmpty(),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(STATE_TIMEOUT_MS), ProfileState())

    // No actions: the tab is read-only.
    override fun handleAction(action: Nothing) = Unit

    private companion object {
        const val STATE_TIMEOUT_MS = 5_000L
    }
}

data class ProfileState(
    val username: String = "",
    val officeName: String = "",
    val instanceUrl: String = "",
)
