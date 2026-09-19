/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.database.charge.entity

import kpt.core.base.database.annotation.DbEntity

import com.mifos.core.model.utils.Parcelable
import com.mifos.core.model.utils.Parcelize
import kotlinx.serialization.Serializable
import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Parcelize
@Serializable
@DbEntity
@Entity(
    indices = [],
    inheritSuperIndices = false,
    primaryKeys = [],
    foreignKeys = [],
    ignoredColumns = [],
    tableName = "ChargeCalculationType",
)
data class ChargeCalculationTypeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = ColumnInfo.INHERIT_FIELD_NAME, typeAffinity = ColumnInfo.UNDEFINED, collate = ColumnInfo.UNSPECIFIED, defaultValue = ColumnInfo.VALUE_UNSPECIFIED)
    val id: Int,

    val code: String? = null,

    val value: String? = null,
) : Parcelable
