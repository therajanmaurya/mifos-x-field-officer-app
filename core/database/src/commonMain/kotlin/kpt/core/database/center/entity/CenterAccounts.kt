/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.database.center.entity

import com.mifos.core.model.utils.Parcelable
import com.mifos.core.model.utils.Parcelize
import kpt.core.database.loan.entity.LoanAccountEntity
import kpt.core.database.savings.entity.SavingsAccountEntity
import kotlinx.serialization.Serializable

/**
 * Created by mayankjindal on 11/07/17.
 */
@Parcelize
@Serializable
data class CenterAccounts(
    val loanAccounts: List<LoanAccountEntity> = emptyList(),

    val savingsAccounts: List<SavingsAccountEntity> = emptyList(),

    val memberLoanAccounts: List<LoanAccountEntity> = emptyList(),
) : Parcelable
