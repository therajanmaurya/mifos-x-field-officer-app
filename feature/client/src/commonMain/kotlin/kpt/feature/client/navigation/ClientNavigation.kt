/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.feature.client.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable
import kpt.core.base.ui.nav.FeatureDestination
import kpt.core.base.ui.nav.composableWithStayTransitions
import kpt.feature.client.list.ClientListScreen

@Serializable
data object ClientListRoute

fun NavController.navigateToClientList(navOptions: NavOptions? = null) =
    navigate(ClientListRoute, navOptions)

/**
 * The client list — a TOP-LEVEL destination on the authenticated graph, so it is annotated and
 * collected into `GeneratedFeatureDestinations` by `:cmp-navigation:generateFeatureDestinations`.
 *
 * This file MUST live in a `navigation` package: the generator calls `scan(roots, "navigation")`, so
 * an annotated destination anywhere else is silently skipped and the screen simply does not exist in
 * the app — with a clean build. (A wrong SIGNATURE throws loudly; a wrong PACKAGE does not. Found the
 * hard way in S3a, when `ClientModule` appeared in the Koin aggregate and the destination did not.)
 *
 * The original registered ~38 client destinations through one `clientNavGraph`. Those arrive with
 * their screens across S3a–S3d; nested drill-downs stay nested and are NOT annotated — only
 * top-level entries are (spec §4).
 *
 * Client detail does not exist yet, so the row tap and create-client are passed as `null` rather than
 * as empty lambdas — the screen then omits the affordance entirely instead of rendering one that does
 * nothing. S3b supplies the detail route, S3c the create flow.
 */
@FeatureDestination
fun NavGraphBuilder.clientListDestination(navController: NavController) {
    composableWithStayTransitions<ClientListRoute> {
        ClientListScreen(
            // null, NOT an empty lambda. Client detail arrives in S3b and create-client in S3c; until
            // then the row renders without a click and the FAB is not drawn at all. An empty lambda
            // would ship a tap that silently does nothing.
            onClientClick = null,
            onCreateClient = null,
        )
    }
}
