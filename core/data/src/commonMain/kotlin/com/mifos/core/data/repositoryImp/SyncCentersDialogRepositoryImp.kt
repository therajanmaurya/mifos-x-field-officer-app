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
import com.mifos.core.data.repository.SyncCentersDialogRepository
import com.mifos.core.model.objects.account.loan.loanWithAssociations.LoanWithAssociations
import com.mifos.core.network.datamanager.DataManagerCenter
import com.mifos.core.network.datamanager.DataManagerClient
import com.mifos.core.network.datamanager.DataManagerGroups
import com.mifos.core.network.datamanager.DataManagerLoan
import com.mifos.core.network.datamanager.DataManagerSavings
import kpt.core.database.center.entity.CenterAccounts
import kpt.core.database.client.entity.ClientAccounts
import kpt.core.database.group.entity.GroupAccounts
import kpt.core.database.savings.entity.SavingsAccountWithAssociationsEntity
import kpt.core.database.client.entity.ClientEntity
import kpt.core.database.center.entity.CenterEntity
import kpt.core.database.center.entity.CenterWithAssociations
import kpt.core.database.group.entity.GroupEntity
import kpt.core.database.group.entity.GroupWithAssociations
import kpt.core.database.loan.entity.LoanRepaymentTemplateEntity
import kpt.core.database.savings.entity.SavingsAccountTransactionTemplateEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Aditya Gupta on 16/08/23.
 */
class SyncCentersDialogRepositoryImp(
    private val dataManagerCenter: DataManagerCenter,
    private val dataManagerLoan: DataManagerLoan,
    private val dataManagerSavings: DataManagerSavings,
    private val dataManagerGroups: DataManagerGroups,
    private val dataManagerClient: DataManagerClient,
) : SyncCentersDialogRepository {

    override fun syncCenterAccounts(centerId: Int): Flow<DataState<CenterAccounts>> {
        return dataManagerCenter.syncCenterAccounts(centerId)
            .asDataStateFlow()
    }

    override fun syncLoanById(loanId: Int): Flow<DataState<LoanWithAssociations>> {
        return dataManagerLoan.syncLoanById(loanId).asDataStateFlow()
    }

    override fun syncLoanRepaymentTemplate(loanId: Int): Flow<DataState<LoanRepaymentTemplateEntity>> {
        return dataManagerLoan.syncLoanRepaymentTemplate(loanId)
            .asDataStateFlow()
    }

    override fun getCenterWithAssociations(centerId: Int): Flow<DataState<CenterWithAssociations>> {
        return dataManagerCenter.getCenterWithAssociations(centerId)
            .asDataStateFlow()
    }

    override fun getGroupWithAssociations(groupId: Int): Flow<DataState<GroupWithAssociations>> {
        return dataManagerGroups.getGroupWithAssociations(groupId)
            .asDataStateFlow()
    }

    override fun syncGroupAccounts(groupId: Int): Flow<DataState<GroupAccounts>> {
        return dataManagerGroups.syncGroupAccounts(groupId)
            .asDataStateFlow()
    }

    override suspend fun syncClientAccounts(clientId: Int): ClientAccounts {
        return dataManagerClient.syncClientAccounts(clientId)
    }

    override suspend fun syncGroupInDatabase(group: GroupEntity) {
        dataManagerGroups.syncGroupInDatabase(group)
    }

    override suspend fun syncClientInDatabase(client: ClientEntity) {
        dataManagerClient.syncClientInDatabase(client)
    }

    override suspend fun syncCenterInDatabase(center: CenterEntity) {
        dataManagerCenter.syncCenterInDatabase(center)
    }

    override fun syncSavingsAccount(
        type: String,
        savingsAccountId: Int,
        association: String?,
    ): Flow<DataState<SavingsAccountWithAssociationsEntity>> {
        return dataManagerSavings.syncSavingsAccount(type, savingsAccountId, association)
            .asDataStateFlow()
    }

    override fun syncSavingsAccountTransactionTemplate(
        savingsAccountType: String,
        savingsAccountId: Int,
        transactionType: String?,
    ): Flow<DataState<SavingsAccountTransactionTemplateEntity>> {
        return dataManagerSavings.syncSavingsAccountTransactionTemplate(
            savingsAccountType,
            savingsAccountId,
            transactionType,
        ).asDataStateFlow()
    }
}
