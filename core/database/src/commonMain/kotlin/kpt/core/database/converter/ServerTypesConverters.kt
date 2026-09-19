/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.database.converter

import kpt.core.base.database.annotation.DbConverters

import kpt.core.database.savings.entity.SavingAccountDepositTypeEntity.ServerTypes
import androidx.room3.ColumnTypeConverter

@DbConverters
class ServerTypesConverters {
    @ColumnTypeConverter
    fun toServerTypes(id: Int?): ServerTypes? {
        return id?.let { ServerTypes.fromId(it) }
    }

    @ColumnTypeConverter
    fun fromServerTypes(serverTypes: ServerTypes?): Int? {
        return serverTypes?.id
    }
}
