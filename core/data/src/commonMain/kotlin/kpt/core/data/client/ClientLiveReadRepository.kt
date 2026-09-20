/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.data.client

import io.ktor.client.statement.HttpResponse
import kpt.core.network.mifos.client.dto.GetClientsResponse

/**
 * Client reads that are deliberately NOT cached.
 *
 * Everything else in this module is offline-first; these are the exceptions, and each one is an
 * exception for a stated reason rather than an omission. They fail with no connectivity — a caller
 * must treat that as expected, not as an error worth retrying forever.
 */
interface ClientLiveReadRepository {

    /** Online-only: filtered query — see retrieveAll23. */
    suspend fun retrieveAll21(officeId: Long? = null, externalId: String? = null, displayName: String? = null, firstName: String? = null, lastName: String? = null, status: String? = null, underHierarchy: String? = null, offset: Int? = null, limit: Int? = null, orderBy: String? = null, sortOrder: String? = null, orphansOnly: Boolean? = null): GetClientsResponse

    /** Online-only: binary download — see downloadDocument. */
    suspend fun getClientImage(clientId: Int): HttpResponse
}
