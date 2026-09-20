/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.data.client.impl

import io.ktor.client.statement.HttpResponse
import kpt.core.base.data.annotation.RepositoryBinding
import kpt.core.data.client.ClientLiveReadRepository
import kpt.core.network.mifos.client.api.ClientApi
import kpt.core.network.mifos.client.dto.GetClientsResponse

@RepositoryBinding(binds = ClientLiveReadRepository::class)
internal class ClientLiveReadRepositoryImpl(
    private val clientApi: ClientApi,
) : ClientLiveReadRepository {

    override suspend fun retrieveAll21(officeId: Long?, externalId: String?, displayName: String?, firstName: String?, lastName: String?, status: String?, underHierarchy: String?, offset: Int?, limit: Int?, orderBy: String?, sortOrder: String?, orphansOnly: Boolean?): GetClientsResponse =
        clientApi.retrieveAll21(officeId = officeId, externalId = externalId, displayName = displayName, firstName = firstName, lastName = lastName, status = status, underHierarchy = underHierarchy, offset = offset, limit = limit, orderBy = orderBy, sortOrder = sortOrder, orphansOnly = orphansOnly)

    override suspend fun getClientImage(clientId: Int): HttpResponse =
        clientApi.getClientImage(clientId = clientId)
}
