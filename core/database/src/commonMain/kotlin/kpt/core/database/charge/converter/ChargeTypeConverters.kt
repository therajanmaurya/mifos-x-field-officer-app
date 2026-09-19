/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.database.charge.converter

import kotlinx.serialization.json.Json
import kpt.core.database.charge.entity.ChargeCalculationTypeEntity
import kpt.core.database.charge.entity.ChargeTimeTypeEntity
import kpt.core.database.charge.entity.ClientChargeCurrencyEntity

import androidx.room3.ColumnTypeConverter
import kpt.core.base.database.annotation.DbConverters

@DbConverters
object ChargeTypeConverters {

    @ColumnTypeConverter
    fun fromChargeTimeType(type: ChargeTimeTypeEntity?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toChargeTimeType(json: String?): ChargeTimeTypeEntity? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromChargeCalculationType(type: ChargeCalculationTypeEntity?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toChargeCalculationType(json: String?): ChargeCalculationTypeEntity? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromClientChargeCurrency(currency: ClientChargeCurrencyEntity?): String? {
        return currency?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toClientChargeCurrency(json: String?): ClientChargeCurrencyEntity? {
        return json?.let { Json.decodeFromString(it) }
    }
}
