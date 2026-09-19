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

import kotlinx.serialization.json.JsonObject
import kpt.core.database.payment.entity.PaymentTypeOptionEntity
import kpt.core.model.loan.LoanType
import kpt.core.model.objects.clients.Address

import kpt.core.base.database.annotation.DbConverters

import kpt.core.database.savings.entity.Charge
import kpt.core.database.savings.entity.SavingsAccountTransactionEntity
import kpt.core.database.center.entity.CenterEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.room3.ColumnTypeConverter

@DbConverters
class ListTypeConverters {

    @ColumnTypeConverter
    fun fromIntList(value: String): ArrayList<Int?> {
        return Json.decodeFromString(value)
    }

    @ColumnTypeConverter
    fun toIntList(list: ArrayList<Int?>): String {
        return Json.encodeToString(list)
    }

    @ColumnTypeConverter
    fun fromCenterList(centers: List<CenterEntity?>): String {
        return centers.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toCenterList(json: String): List<CenterEntity?> {
        return json.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun toListOfInts(json: String?): List<Int?>? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromListOfTransactions(list: List<SavingsAccountTransactionEntity>?): String? {
        return list?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toListOfTransactions(json: String?): List<SavingsAccountTransactionEntity>? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromListOfCharges(list: List<Charge?>?): String? {
        return list?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toListOfCharges(json: String?): List<Charge?>? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromGroupActivationDateListInt(date: List<Int>?): String? {
        return date?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toGroupActivationDateListInt(json: String?): List<Int>? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromMap(map: Map<String, Any>?): String? {
        return map?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toMap(json: String?): Map<String, Any>? {
        return json?.let {
            Json.decodeFromString<JsonObject>(it)
                .mapValues { entry -> entry.value }
        }
    }

    @ColumnTypeConverter
    fun fromType(type: LoanType?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toType(json: String?): LoanType? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromListPaymentTypeOptions(type: List<PaymentTypeOptionEntity>): String {
        return type.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toListPaymentTypeOptions(json: String): List<PaymentTypeOptionEntity> {
        return json.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromAddressList(addressList: List<Address>?): String? =
        addressList?.let { Json.encodeToString(it) }

    @ColumnTypeConverter
    fun toAddressList(json: String?): List<Address>? =
        json?.let { Json.decodeFromString(it) }
}
