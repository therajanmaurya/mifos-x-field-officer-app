/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.database.loan.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.PrimaryKey
import kpt.core.model.objects.account.loan.Currency
import kpt.core.model.utils.Parcelable
import kpt.core.model.utils.Parcelize
import kotlinx.serialization.Serializable
import kpt.core.base.database.annotation.DbEntity



@DbEntity
@Entity(
    tableName = "LoanAccountEntity",
    foreignKeys = [
        ForeignKey(
            entity = LoanStatusEntity::class,
            parentColumns = ["id"],
            childColumns = ["status"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION,
            deferred = false,
        ),
        ForeignKey(
            entity = LoanTypeEntity::class,
            parentColumns = ["id"],
            childColumns = ["loanType"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION,
            deferred = false,
        ),
    ],
    indices = [],
    inheritSuperIndices = false,
    primaryKeys = [],
    ignoredColumns = [],
)
@Parcelize
@Serializable
data class LoanAccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val clientId: Long = 0,

    val groupId: Long = 0,

    val centerId: Long = 0,

    val accountNo: String? = null,

    val externalId: String? = null,

    val productId: Int? = null,

    val productName: String? = null,

    val currency: Currency? = null,

    @ColumnInfo(index = true, name = ColumnInfo.INHERIT_FIELD_NAME, typeAffinity = ColumnInfo.UNDEFINED, collate = ColumnInfo.UNDEFINED, defaultValue = ColumnInfo.VALUE_UNSPECIFIED)
    val status: LoanStatusEntity? = null,

    @ColumnInfo(index = true, name = ColumnInfo.INHERIT_FIELD_NAME, typeAffinity = ColumnInfo.UNDEFINED, collate = ColumnInfo.UNSPECIFIED, defaultValue = ColumnInfo.VALUE_UNSPECIFIED)
    val loanType: LoanTypeEntity? = null,

    val loanCycle: Int? = null,

    val inArrears: Boolean? = null,

    val originalLoan: Double? = null,

    val loanBalance: Double? = null,

    val amountPaid: Double? = null,
) : Parcelable
