/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package com.mifos.core.data.repositoryImp

import com.mifos.core.data.repository.ActivateRepository
import com.mifos.core.model.objects.clients.ActivatePayload
import kpt.core.network.center.datamanager.DataManagerCenter
import kpt.core.network.client.datamanager.DataManagerClient
import kpt.core.network.group.datamanager.DataManagerGroups
import kpt.core.network.mifos.center.dto.PostCentersCenterIdResponse
import kpt.core.network.mifos.client.dto.PostClientsClientIdResponse
import io.ktor.client.statement.HttpResponse

/**
 * Created by Aditya Gupta on 06/08/23.
 */
class ActivateRepositoryImp(
    private val dataManagerClient: DataManagerClient,
    private val dataManagerCenter: DataManagerCenter,
    private val dataManagerGroups: DataManagerGroups,
) : ActivateRepository {

    override suspend fun activateClient(
        clientId: Int,
        clientActivate: ActivatePayload?,
    ): PostClientsClientIdResponse {
        return dataManagerClient.activateClient(clientId, clientActivate)
    }

    override suspend fun activateCenter(
        centerId: Int,
        activatePayload: ActivatePayload?,
    ): PostCentersCenterIdResponse {
        return dataManagerCenter.activateCenter(centerId, activatePayload)
    }

    override suspend fun activateGroup(
        groupId: Int,
        activatePayload: ActivatePayload,
    ): HttpResponse {
        return dataManagerGroups.activateGroup(groupId, activatePayload)
    }
}
