/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.database.savings.entity

import kpt.core.base.database.annotation.DbEntity

import com.mifos.core.model.utils.Parcelable
import com.mifos.core.model.utils.Parcelize
import kpt.core.database.payment.entity.PaymentTypeOptionEntity
import kotlinx.serialization.Serializable
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Serializable
@Parcelize
@DbEntity
@Entity(
    indices = [],
    inheritSuperIndices = false,
    primaryKeys = [],
    foreignKeys = [],
    ignoredColumns = [],
    tableName = "SavingsAccountTransactionTemplate",
)
data class SavingsAccountTransactionTemplateEntity(
    @PrimaryKey(autoGenerate = true)
    val accountId: Int? = null,

    val accountNo: String? = null,

    val date: List<Int> = emptyList(),

    val reversed: Boolean? = null,

    val paymentTypeOptions: List<PaymentTypeOptionEntity> = emptyList(),
) : Parcelable {

    fun isReversed(): Boolean? {
        return reversed
    }
}
