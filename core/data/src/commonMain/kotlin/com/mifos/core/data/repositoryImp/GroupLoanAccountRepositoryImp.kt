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
import com.mifos.core.data.repository.GroupLoanAccountRepository
import kpt.core.model.objects.payloads.GroupLoanPayload
import kpt.core.model.objects.template.loan.GroupLoanTemplate
import kpt.core.network.DataManager
import kpt.core.database.loan.entity.Loan
import kotlinx.coroutines.flow.Flow

/**
 * Created by Aditya Gupta on 12/08/23.
 */
class GroupLoanAccountRepositoryImp(
    private val dataManager: DataManager,
) : GroupLoanAccountRepository {

    override fun getGroupLoansAccountTemplate(
        groupId: Int,
        productId: Int,
    ): Flow<DataState<GroupLoanTemplate>> {
        return dataManager.getGroupLoansAccountTemplate(groupId, productId)
            .asDataStateFlow()
    }

    override fun createGroupLoansAccount(loansPayload: GroupLoanPayload): Flow<DataState<Loan>> {
        return dataManager.createGroupLoansAccount(loansPayload)
            .asDataStateFlow()
    }
}
