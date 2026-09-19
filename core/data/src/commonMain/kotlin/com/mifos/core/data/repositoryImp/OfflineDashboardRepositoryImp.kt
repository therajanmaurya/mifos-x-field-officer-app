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

import com.mifos.core.common.utils.DataState
import com.mifos.core.common.utils.asDataStateFlow
import com.mifos.core.data.repository.OfflineDashboardRepository
import kpt.core.network.center.datamanager.DataManagerCenter
import kpt.core.network.client.datamanager.DataManagerClient
import kpt.core.network.group.datamanager.DataManagerGroups
import kpt.core.network.loan.datamanager.DataManagerLoan
import kpt.core.network.savings.datamanager.DataManagerSavings
import kpt.core.database.loan.entity.LoanRepaymentRequestEntity
import kpt.core.database.savings.entity.SavingsAccountTransactionRequestEntity
import kpt.core.database.center.entity.CenterPayloadEntity
import kpt.core.database.client.entity.ClientPayloadEntity
import kpt.core.database.group.entity.GroupPayloadEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Aditya Gupta on 16/08/23.
 */
class OfflineDashboardRepositoryImp(
    private val dataManagerClient: DataManagerClient,
    private val dataManagerGroups: DataManagerGroups,
    private val dataManagerCenter: DataManagerCenter,
    private val dataManagerLoan: DataManagerLoan,
    private val dataManagerSavings: DataManagerSavings,
) : OfflineDashboardRepository {

    override fun allDatabaseClientPayload(): Flow<DataState<List<ClientPayloadEntity>>> {
        return dataManagerClient.allDatabaseClientPayload
            .asDataStateFlow()
    }

    override fun allDatabaseGroupPayload(): Flow<DataState<List<GroupPayloadEntity>>> {
        return dataManagerGroups.allDatabaseGroupPayload
            .asDataStateFlow()
    }

    override fun allDatabaseCenterPayload(): Flow<DataState<List<CenterPayloadEntity>>> {
        return dataManagerCenter.getAllDatabaseCenterPayload
            .asDataStateFlow()
    }

    override fun databaseLoanRepayments(): Flow<DataState<List<LoanRepaymentRequestEntity>>> {
        return dataManagerLoan.databaseLoanRepayments
            .asDataStateFlow()
    }

    override fun allSavingsAccountTransactions(): Flow<DataState<List<SavingsAccountTransactionRequestEntity>>> {
        return dataManagerSavings.allSavingsAccountTransactions
            .asDataStateFlow()
    }
}
