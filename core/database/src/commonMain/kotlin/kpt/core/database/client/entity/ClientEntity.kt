/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.database.client.entity

import kpt.core.database.shared.entity.Timeline

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.PrimaryKey
import com.mifos.core.model.utils.Parcelable
import com.mifos.core.model.utils.Parcelize
import kotlinx.serialization.Serializable
import kpt.core.base.database.annotation.DbEntity
import kpt.core.database.group.entity.GroupEntity



@Parcelize
@DbEntity
@Entity(
    tableName = "Client",
    indices = [],
    inheritSuperIndices = false,
    primaryKeys = [],
    ignoredColumns = [],
    foreignKeys = [
        ForeignKey(
            entity = ClientStatusEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION,
            deferred = false,
        ),
        ForeignKey(
            entity = ClientDateEntity::class,
            parentColumns = ["clientId"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION,
            deferred = false,
        ),
    ],
)
@Serializable
data class ClientEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = ColumnInfo.INHERIT_FIELD_NAME, typeAffinity = ColumnInfo.UNDEFINED, collate = ColumnInfo.UNSPECIFIED, defaultValue = ColumnInfo.VALUE_UNSPECIFIED)
    val id: Int = 0,

    val groupId: Int? = 0,

    val groupName: String? = null,

    val accountNo: String? = null,

    val clientId: Int? = null,

    val status: ClientStatusEntity? = null,

    val sync: Boolean = false,

    val active: Boolean = false,

    val clientDate: ClientDateEntity? = null,

    val activationDate: List<Int?> = emptyList(),

    val dateOfBirth: List<Int?> = emptyList(),

    val groups: List<GroupEntity>? = emptyList(),

    val mobileNo: String? = null,

    val firstname: String? = null,

    val middlename: String? = null,

    val lastname: String? = null,

    val displayName: String? = null,

    val officeId: Int = 0,

    val officeName: String? = null,

    val staffId: Int = 0,

    val staffName: String? = null,

    val timeline: Timeline? = null,

    val fullname: String? = null,

    val savingsAccountId: Long? = null,

    val imageId: Int = 0,

    val imagePresent: Boolean = false,

    val externalId: String? = null,

    val emailAddress: String? = null,

    val legalForm: ClientStatusEntity? = null,

    val gender: ClientGenderEntity? = null,

    val clientType: ClientTypeEntity? = null,

    val clientClassification: ClientClassificationEntity? = null,
) : Parcelable
