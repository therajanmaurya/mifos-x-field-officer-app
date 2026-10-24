/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.network.config

import kpt.core.base.network.AccessPoint
import kpt.core.model.utils.ServerConfig

/**
 * The server the app ships pointed at, as the server-config SCREEN describes one — DERIVED from the
 * generated `mifos` access point, never written a second time.
 *
 * `app-profile/app.yaml#network.access_points` is the one place the server is declared. The HTTP
 * client reads it through [AppAccessPoints]; the screen's "Use defaults" and the stored preference's
 * fallback read it through here. Before this, `ServerConfig.DEFAULT` was a hand-kept copy that a parity
 * test had to police — and on 2026-10-01 the two disagreed, every call 404'd, and the screen showed the
 * "right" values throughout.
 *
 * Fork-owned (`owner: fork`), beside the generated [AppAccessPoints]: `ServerConfig` is this app's
 * runtime-server feature, not the template's.
 */
object AppServerConfig {

    /** The access point the field officer signs in to. */
    const val ACCESS_POINT = "mifos"

    /** The header that carries Fineract's tenant on [ACCESS_POINT]. */
    const val TENANT_HEADER = "Fineract-Platform-TenantId"

    val DEFAULT: ServerConfig = AppAccessPoints.points.single { it.id == ACCESS_POINT }.toServerConfig()
}

private val ORIGIN = Regex("^(https?://)([^/:]+)(?::(\\d+))?/?$")

/** Splits an access point back into the screen's five fields. */
internal fun AccessPoint.toServerConfig(tenantHeader: String = AppServerConfig.TENANT_HEADER): ServerConfig {
    val (protocol, host, port) = requireNotNull(ORIGIN.find(baseUrl)) {
        "access point '$id' base_url '$baseUrl' is not an origin (scheme://host[:port]/)"
    }.destructured
    val path = basePath.trim('/')
    return ServerConfig(
        protocol = protocol,
        endPoint = host,
        apiPath = if (path.isEmpty()) "/" else "/$path/",
        port = port.ifEmpty { if (protocol == "https://") "443" else "80" },
        tenant = headers.firstOrNull { it.name.equals(tenantHeader, ignoreCase = true) }?.value.orEmpty(),
    )
}
