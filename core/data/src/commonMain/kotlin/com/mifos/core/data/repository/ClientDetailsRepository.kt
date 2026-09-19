/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package com.mifos.core.data.repository

import com.mifos.core.common.utils.DataState
import kpt.core.model.objects.account.share.ShareAccounts
import kpt.core.network.mifos.client.dto.ClientCloseTemplateResponse
import kpt.core.model.shared.CollateralItem
import kpt.core.model.shared.CollateralItemResult
import kpt.core.network.mifos.savings.dto.SavingAccountOption
import kpt.core.network.mifos.staff.dto.StaffOption
import kpt.core.database.client.entity.ClientAccounts
import kpt.core.database.client.entity.ClientEntity
import io.ktor.client.request.forms.MultiPartFormDataContent
import kotlinx.coroutines.flow.Flow

/**
 * Created by Aditya Gupta on 06/08/23.
 */
interface ClientDetailsRepository {

    suspend fun uploadClientImage(clientId: Int, image: MultiPartFormDataContent)

    suspend fun deleteClientImage(clientId: Int)

    suspend fun getClientAccounts(clientId: Int): ClientAccounts

    suspend fun getSavingsAccounts(clientId: Int): List<SavingAccountOption>

    suspend fun getShareAccounts(clientId: Int): List<ShareAccounts>

    suspend fun getClientStaffOptions(clientId: Int): List<StaffOption>

    suspend fun getClientCloseTemplate(): DataState<ClientCloseTemplateResponse>

    suspend fun getCollateralItems(): DataState<List<CollateralItem>>

    suspend fun getClientCollaterals(clientId: Int): DataState<List<CollateralItemResult>>

    suspend fun getClient(clientId: Int): ClientEntity

    fun getImage(clientId: Int): Flow<DataState<String>>

    suspend fun assignStaff(clientId: Int, staffId: Int): DataState<Unit>

    suspend fun unassignStaff(clientId: Int, staffId: Int): DataState<Unit>

    suspend fun proposeTransfer(
        clientId: Int,
        destinationOfficeId: Int,
        transferDate: String,
        note: String,
    ): DataState<Unit>

    suspend fun updateDefaultSavingsAccount(clientId: Int, accountId: Long): DataState<Unit>

    suspend fun closeClient(
        clientId: Int,
        closureDate: String,
        closureReasonId: Int,
    ): DataState<Unit>

    suspend fun createCollateral(
        clientId: Int,
        collateralId: Int,
        quantity: String,
    ): DataState<Unit>

    val clientUpdateEvents: Flow<Unit>
    suspend fun triggerClientUpdate()
}
