/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.model.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerConfig(
    val protocol: String,
    @SerialName("end_point")
    val endPoint: String,
    @SerialName("api_path")
    val apiPath: String,
    val port: String,
    val tenant: String,
) {
    companion object {
        // No `DEFAULT` here, deliberately. The shipped server is DERIVED from the generated access point
        // (`kpt.core.network.config.AppServerConfig.DEFAULT`, from `app-profile/app.yaml`) — a constant
        // in this module could only ever be a second copy of it, and core/model cannot see core/network.
        // Read it through `ServerConfigRepository.defaultConfig`, or inject it by [DEFAULT_QUALIFIER].

        /** Koin qualifier of the shipped default, bound by core/network's `ProjectNetworkModule`. */
        const val DEFAULT_QUALIFIER = "serverConfig.default"

        val LOCALHOST = ServerConfig(
            protocol = "http://",
            endPoint = "localhost",
            apiPath = "/fineract-provider/api/v1/",
            port = "8080",
            tenant = "default",
        )
    }
}

fun ServerConfig.getInstanceUrl(): String {
    return "$protocol$endPoint$apiPath"
}
