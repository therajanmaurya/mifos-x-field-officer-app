/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.network.lwmswhoxvvoagzkqxiyd.appserverconfig.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

/** One row of `public.app_server_config` (supabase/migrations/20261025120000_app_server_config.sql). */
@Serializable
data class RemoteServerInstanceDto(
    val label: String,
    val protocol: String,
    val endpoint: String,
    val path: String,
    val port: String? = null,
    @SerialName("platform_tenant_id") val platformTenantId: String,
    @SerialName("is_default") val isDefault: Boolean = false,
    @SerialName("supported_apps") val supportedApps: List<String> = emptyList(),
    @SerialName("demo_accounts") val demoAccounts: List<RemoteDemoAccountDto> = emptyList(),
    /** mifos-pay's interbank list; this app does not read it, so it stays raw. */
    @SerialName("interbank_servers") val interbankServers: JsonArray = JsonArray(emptyList()),
)

/** A PUBLISHED demo sign-in of a public demo instance. */
@Serializable
data class RemoteDemoAccountDto(val role: String, val username: String, val password: String)
