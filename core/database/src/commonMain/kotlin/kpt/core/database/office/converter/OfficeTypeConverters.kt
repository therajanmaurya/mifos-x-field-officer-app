/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.database.office.converter

import kpt.core.database.office.entity.OfficeOpeningDateEntity

import kpt.core.base.database.annotation.DbConverters

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.room3.ColumnTypeConverter

@DbConverters
class OfficeTypeConverters {
    @ColumnTypeConverter
    fun fromOpeningDateList(list: List<Int?>?): String {
        return Json.encodeToString(list ?: emptyList())
    }

    @ColumnTypeConverter
    fun fromOfficeOpeningDate(status: OfficeOpeningDateEntity?): String? {
        return status?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toOfficeOpeningDate(json: String?): OfficeOpeningDateEntity? {
        return json?.let { Json.decodeFromString(it) }
    }
}
