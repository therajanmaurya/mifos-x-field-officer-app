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

import kotlinx.serialization.Serializable
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import androidx.room3.Ignore

@DbEntity
@Entity(
    indices = [],
    inheritSuperIndices = false,
    primaryKeys = [],
    foreignKeys = [],
    ignoredColumns = [],
    tableName = "SavingAccountDepositType",
)
@Serializable
data class SavingAccountDepositTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val code: String? = null,

    val value: String? = null,
) {

    @Ignore
    val isRecurring: Boolean
        get() = ServerTypes.RECURRING.id == id
    @Ignore
    val endpoint: String
        get() = ServerTypes.fromId(id).endpoint
    @Ignore
    val serverType: ServerTypes
        get() = ServerTypes.fromId(id)

    enum class ServerTypes(val id: Int, val code: String, val endpoint: String) {
        SAVINGS(100, "depositAccountType.savingsDeposit", "savingsaccounts"),
        FIXED(200, "depositAccountType.fixedDeposit", "fixeddepositaccounts"),
        RECURRING(300, "depositAccountType.recurringDeposit", "recurringdepositaccounts"),
        ;

        companion object {
            fun fromId(id: Int?): ServerTypes {
                for (type in entries) {
                    if (type.id == id) {
                        return type
                    }
                }
                return SAVINGS
            }
        }
    }
}
