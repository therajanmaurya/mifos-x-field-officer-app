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

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.room3.ColumnTypeConverter

@DbConverters
class DueDateConverter {
    @ColumnTypeConverter
    fun fromListToString(dueDate: List<Int>): String {
        return Json.encodeToString(dueDate)
    }

    @ColumnTypeConverter
    fun fromStringToList(dueDateString: String): List<Int> {
        return Json.decodeFromString(dueDateString)
    }
}
