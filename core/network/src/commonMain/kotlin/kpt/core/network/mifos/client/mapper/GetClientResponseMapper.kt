/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.network.mifos.client.mapper

import kpt.core.common.utils.Page
import kpt.core.network.data.AbstractMapper
import kpt.core.network.mifos.client.dto.GetClientsResponse
import kpt.core.database.client.entity.ClientEntity

object GetClientResponseMapper : AbstractMapper<GetClientsResponse, Page<ClientEntity>>() {

    override fun mapFromEntity(entity: GetClientsResponse): Page<ClientEntity> {
        return Page<ClientEntity>().apply {
            totalFilteredRecords = entity.totalFilteredRecords!!
            pageItems = ClientMapper.mapFromEntityList(entity.pageItems!!.toList())
        }
    }

    override fun mapToEntity(domainModel: Page<ClientEntity>): GetClientsResponse {
        return GetClientsResponse(
            totalFilteredRecords = domainModel.totalFilteredRecords,
            pageItems = ClientMapper.mapToEntityList(domainModel.pageItems).toSet(),
        )
    }
}
