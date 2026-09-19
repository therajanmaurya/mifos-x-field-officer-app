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
import kpt.core.database.loan.entity.LoanRepaymentRequestEntity
import kpt.core.database.savings.entity.SavingsAccountTransactionRequestEntity
import kpt.core.database.center.entity.CenterPayloadEntity
import kpt.core.database.client.entity.ClientPayloadEntity
import kpt.core.database.group.entity.GroupPayloadEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Aditya Gupta on 16/08/23.
 */
interface OfflineDashboardRepository {

    fun allDatabaseClientPayload(): Flow<DataState<List<ClientPayloadEntity>>>

    fun allDatabaseGroupPayload(): Flow<DataState<List<GroupPayloadEntity>>>

    fun allDatabaseCenterPayload(): Flow<DataState<List<CenterPayloadEntity>>>

    fun databaseLoanRepayments(): Flow<DataState<List<LoanRepaymentRequestEntity>>>

    fun allSavingsAccountTransactions(): Flow<DataState<List<SavingsAccountTransactionRequestEntity>>>
}
