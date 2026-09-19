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

import kpt.core.model.shared.Timeline

import androidx.room3.ColumnTypeConverter
import com.mifos.core.model.objects.Changes
import com.mifos.core.model.objects.account.loan.AmortizationType
import com.mifos.core.model.objects.account.loan.Currency
import com.mifos.core.model.objects.account.loan.InterestCalculationPeriodType
import com.mifos.core.model.objects.account.loan.InterestRateFrequencyType
import com.mifos.core.model.objects.account.loan.InterestType
import com.mifos.core.model.objects.account.loan.Period
import com.mifos.core.model.objects.account.loan.RepaymentFrequencyType
import com.mifos.core.model.objects.account.loan.RepaymentSchedule
import com.mifos.core.model.objects.account.loan.TermPeriodFrequencyType
import com.mifos.core.model.objects.account.loan.Transaction
import com.mifos.core.model.objects.account.saving.InterestCalculationDaysInYearType
import com.mifos.core.model.objects.account.saving.InterestCalculationType
import com.mifos.core.model.objects.account.saving.InterestCompoundingPeriodType
import com.mifos.core.model.objects.account.saving.InterestPostingPeriodType
import com.mifos.core.model.objects.account.saving.LockinPeriodFrequencyType
import com.mifos.core.model.objects.clients.Address
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kpt.core.base.database.annotation.DbConverters
import kpt.core.database.center.entity.CenterDateEntity
import kpt.core.database.center.entity.CenterEntity
import kpt.core.database.charge.entity.ChargeCalculationTypeEntity
import kpt.core.database.charge.entity.ChargeTimeTypeEntity
import kpt.core.database.charge.entity.ClientChargeCurrencyEntity
import kpt.core.database.client.entity.ClientClassificationEntity
import kpt.core.database.client.entity.ClientDateEntity
import kpt.core.database.client.entity.ClientGenderEntity
import kpt.core.database.client.entity.ClientStatusEntity
import kpt.core.database.client.entity.ClientTypeEntity
import kpt.core.database.client.entity.InterestTypeEntity
import kpt.core.database.client.entity.OfficeOptionsEntity
import kpt.core.database.client.entity.OptionsEntity
import kpt.core.database.client.entity.SavingProductOptionsEntity
import kpt.core.database.client.entity.StaffOptionsEntity
import kpt.core.database.datatable.entity.ColumnHeader
import kpt.core.database.datatable.entity.ColumnValue
import kpt.core.database.datatable.entity.DataTableEntity
import kpt.core.database.datatable.entity.DataTablePayload
import kpt.core.database.group.entity.GroupDateEntity
import kpt.core.database.group.entity.GroupEntity
import kpt.core.database.loan.entity.ActualDisbursementDateEntity
import kpt.core.database.loan.entity.LoanAccountSummaryEntity
import kpt.core.database.loan.entity.LoanStatusEntity
import kpt.core.database.loan.entity.LoanTimelineEntity
import kpt.core.model.loan.LoanType
import kpt.core.database.loan.entity.LoanTypeEntity
import kpt.core.database.office.entity.OfficeOpeningDateEntity
import kpt.core.database.payment.entity.PaymentTypeOptionEntity
import kpt.core.database.savings.entity.Charge
import kpt.core.database.savings.entity.SavingAccountCurrencyEntity
import kpt.core.database.savings.entity.SavingAccountDepositTypeEntity
import kpt.core.database.savings.entity.SavingAccountDepositTypeEntity.ServerTypes
import kpt.core.database.savings.entity.SavingsAccountStatusEntity
import kpt.core.database.savings.entity.SavingsAccountSummaryEntity
import kpt.core.database.savings.entity.SavingsAccountTransactionEntity
import kpt.core.database.savings.entity.SavingsTransactionDateEntity
import kpt.core.database.savings.entity.SavingsTransactionTypeEntity
import kpt.core.database.survey.entity.ComponentDatasEntity
import kpt.core.database.survey.entity.QuestionDatasEntity
import kpt.core.database.survey.entity.ResponseDatasEntity



@Suppress("TooManyFunctions")
@DbConverters
class CustomTypeConverters {

    @ColumnTypeConverter
    fun fromCurrency(type: Currency?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toCurrency(json: String?): Currency? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromCenterDate(centerDate: CenterDateEntity?): String? {
        return centerDate?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toCenterDate(json: String?): CenterDateEntity? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromDepositType(type: SavingAccountDepositTypeEntity?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toDepositType(json: String?): SavingAccountDepositTypeEntity? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromSavingAccountStatus(type: SavingsAccountStatusEntity?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toSavingAccountStatus(json: String?): SavingsAccountStatusEntity? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromSavingAccountSummary(type: SavingsAccountSummaryEntity?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toSavingAccountSummary(json: String?): SavingsAccountSummaryEntity? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromGroupDate(groupDate: GroupDateEntity?): String? {
        return groupDate?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toGroupDate(json: String?): GroupDateEntity? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromChargeTimeType(type: ChargeTimeTypeEntity?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toChargeTimeType(json: String?): ChargeTimeTypeEntity? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromClientDate(date: ClientDateEntity?): String? {
        return date?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toClientDate(json: String?): ClientDateEntity? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromDataTable(date: DataTableEntity?): String? {
        return date?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toDataTable(json: String?): DataTableEntity? {
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

    @ColumnTypeConverter
    fun fromClientStatus(status: ClientStatusEntity?): String? {
        return status?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toClientStatus(json: String?): ClientStatusEntity? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromClientGenderEntity(gender: ClientGenderEntity?): String? {
        return gender?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toClientGenderEntity(json: String?): ClientGenderEntity? {
        return json?.let { Json.decodeFromString<ClientGenderEntity>(it) }
    }

    @ColumnTypeConverter
    fun fromClientTypeEntity(clientType: ClientTypeEntity?): String? {
        return clientType?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toClientTypeEntity(json: String?): ClientTypeEntity? {
        return json?.let { Json.decodeFromString<ClientTypeEntity>(it) }
    }

    @ColumnTypeConverter
    fun fromClientClassificationEntity(clientClassification: ClientClassificationEntity?): String? {
        return clientClassification?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toClientClassificationEntity(json: String?): ClientClassificationEntity? {
        return json?.let { Json.decodeFromString<ClientClassificationEntity>(it) }
    }

    @ColumnTypeConverter
    fun fromOfficeOpeningDate(status: OfficeOpeningDateEntity?): String? {
        return status?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toOfficeOpeningDate(json: String?): OfficeOpeningDateEntity? {
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
    fun fromListGroup(date: List<GroupEntity>): String {
        return date.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toListGroup(json: String): List<GroupEntity> {
        return json.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromListOfficeOptions(date: List<OfficeOptionsEntity>): String {
        return date.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toListOfficeOptions(json: String): List<OfficeOptionsEntity> {
        return json.let { Json.decodeFromString(it) } ?: emptyList()
    }

    @ColumnTypeConverter
    fun fromListStaffOptions(date: List<StaffOptionsEntity>): String {
        return date.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toListStaffOptions(json: String): List<StaffOptionsEntity> {
        return json.let { Json.decodeFromString(it) } ?: emptyList()
    }

    @ColumnTypeConverter
    fun fromListDataTable(date: List<DataTableEntity>): String {
        return date.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toListDataTable(json: String): List<DataTableEntity> {
        return json.let { Json.decodeFromString(it) } ?: emptyList()
    }

    @ColumnTypeConverter
    fun fromListOptions(date: List<OptionsEntity>): String {
        return date.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toListOptions(json: String): List<OptionsEntity> {
        return json.let { Json.decodeFromString(it) } ?: emptyList()
    }

    @ColumnTypeConverter
    fun fromListInterestType(date: List<InterestTypeEntity>): String {
        return date.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toListInterestType(json: String): List<InterestTypeEntity> {
        return json.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromListSavingProductOptions(date: List<SavingProductOptionsEntity>): String {
        return date.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toListSavingProductOptions(json: String): List<SavingProductOptionsEntity> {
        return json.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromListColumnValue(date: List<ColumnValue>): String {
        return date.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toListColumnValue(json: String?): List<ColumnValue> {
        return json?.let { Json.decodeFromString(it) } ?: emptyList()
    }

    @ColumnTypeConverter
    fun fromListColumnHeader(date: List<ColumnHeader>): String {
        return date.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toListColumnHeader(json: String?): List<ColumnHeader> {
        return json?.let { Json.decodeFromString(it) } ?: emptyList()
    }

    @ColumnTypeConverter
    fun fromListDataTablePayload(date: List<DataTablePayload>): String {
        return date.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toListDataTablePayload(json: String?): List<DataTablePayload> {
        return json?.let { Json.decodeFromString(it) } ?: emptyList()
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
    fun fromListToString(dueDate: List<Int>): String {
        return Json.encodeToString(dueDate)
    }

    @ColumnTypeConverter
    fun fromStringToList(dueDateString: String): List<Int> {
        return Json.decodeFromString(dueDateString)
    }

    @ColumnTypeConverter
    fun fromTimeline(timeline: Timeline?): String? {
        return timeline?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toTimeline(json: String?): Timeline? {
        return json?.let { Json.decodeFromString(it) }
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
    fun fromStatus(status: LoanStatusEntity?): String? {
        return status?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toStatus(json: String?): LoanStatusEntity? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromLoanType(type: LoanTypeEntity?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toLoanType(json: String?): LoanTypeEntity? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromSavingAccountCurrency(currency: SavingAccountCurrencyEntity?): String? {
        return currency?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toSavingAccountCurrency(json: String?): SavingAccountCurrencyEntity? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromTermPeriodFrequencyType(type: TermPeriodFrequencyType?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toTermPeriodFrequencyType(json: String?): TermPeriodFrequencyType? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromRepaymentFrequencyType(type: RepaymentFrequencyType?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toRepaymentFrequencyType(json: String?): RepaymentFrequencyType? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromInterestRateFrequencyType(type: InterestRateFrequencyType?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toInterestRateFrequencyType(json: String?): InterestRateFrequencyType? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromSummary(summary: LoanAccountSummaryEntity?): String? {
        return summary?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toSummary(json: String?): LoanAccountSummaryEntity? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromAmortizationType(type: AmortizationType?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toAmortizationType(json: String?): AmortizationType? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromInterestType(type: InterestType?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toInterestType(json: String?): InterestType? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromInterestCalculationPeriodType(type: InterestCalculationPeriodType?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toInterestCalculationPeriodType(json: String?): InterestCalculationPeriodType? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromLoanTimeline(timeline: LoanTimelineEntity?): String? {
        return timeline?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toLoanTimeline(json: String?): LoanTimelineEntity? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromRepaymentSchedule(schedule: RepaymentSchedule?): String? {
        return schedule?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toRepaymentSchedule(json: String?): RepaymentSchedule? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromTransactionList(transactions: List<Transaction>?): String? {
        return transactions?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toTransactionList(json: String?): List<Transaction>? {
        return json?.let { Json.decodeFromString(it) }
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
    fun fromListInt(date: List<Int?>?): String? {
        return date?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toListInt(json: String?): List<Int?>? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromActualDisbursementDate(date: ActualDisbursementDateEntity?): String? {
        return date?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toActualDisbursementDate(json: String?): ActualDisbursementDateEntity? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromMutableListInt(date: MutableList<Int>?): String? {
        return date?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toMutableListInt(json: String?): MutableList<Int>? {
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
    fun fromPeriodList(json: String?): List<Period>? {
        return json?.let { Json.decodeFromString<List<Period>>(it) }
    }

    @ColumnTypeConverter
    fun toPeriodList(periodList: List<Period>?): String? {
        return periodList?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toChanges(changes: Changes?): String {
        return Json.encodeToString(changes)
    }

    @ColumnTypeConverter
    fun fromChanges(changes: String?): Changes? {
        return changes?.let { Json.decodeFromString(changes) }
    }

    @ColumnTypeConverter
    fun fromTransactionType(type: SavingsTransactionTypeEntity?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toTransactionType(json: String?): SavingsTransactionTypeEntity? {
        return json?.let { Json.decodeFromString<SavingsTransactionTypeEntity>(it) }
    }

    @ColumnTypeConverter
    fun fromSavingsTransactionDate(date: SavingsTransactionDateEntity?): String? {
        return date?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toSavingsTransactionDate(json: String?): SavingsTransactionDateEntity? {
        return json?.let { Json.decodeFromString<SavingsTransactionDateEntity>(it) }
    }

    @ColumnTypeConverter
    fun fromInterestCalculationDaysInYearType(
        type: InterestCalculationDaysInYearType?,
    ): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toInterestCalculationDaysInYearType(json: String?): InterestCalculationDaysInYearType? {
        return json?.let { Json.decodeFromString<InterestCalculationDaysInYearType>(it) }
    }

    @ColumnTypeConverter
    fun fromInterestCalculationType(type: InterestCalculationType?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toInterestCalculationType(json: String?): InterestCalculationType? {
        return json?.let { Json.decodeFromString<InterestCalculationType>(it) }
    }

    @ColumnTypeConverter
    fun fromInterestCompoundingPeriodType(
        type: InterestCompoundingPeriodType?,
    ): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toInterestCompoundingPeriodType(
        json: String?,
    ): InterestCompoundingPeriodType? {
        return json?.let { Json.decodeFromString<InterestCompoundingPeriodType>(it) }
    }

    @ColumnTypeConverter
    fun fromInterestPostingPeriodType(
        type: InterestPostingPeriodType?,
    ): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toInterestPostingPeriodType(
        json: String?,
    ): InterestPostingPeriodType? {
        return json?.let { Json.decodeFromString<InterestPostingPeriodType>(it) }
    }

    @ColumnTypeConverter
    fun fromLockinPeriodFrequencyType(type: LockinPeriodFrequencyType?): String? {
        return type?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toLockinPeriodFrequencyType(json: String?): LockinPeriodFrequencyType? {
        return json?.let { Json.decodeFromString<LockinPeriodFrequencyType>(it) }
    }

    @ColumnTypeConverter
    fun fromPaymentTypeOption(paymentTypeOption: PaymentTypeOptionEntity?): String? {
        return paymentTypeOption?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toPaymentTypeOption(json: String?): PaymentTypeOptionEntity? {
        return json?.let { Json.decodeFromString<PaymentTypeOptionEntity>(it) }
    }

    @ColumnTypeConverter
    fun toServerTypes(id: Int?): ServerTypes? {
        return id?.let { ServerTypes.fromId(it) }
    }

    @ColumnTypeConverter
    fun fromServerTypes(serverTypes: ServerTypes?): Int? {
        return serverTypes?.id
    }

    @ColumnTypeConverter
    fun fromQuestionDatasList(questionDatas: List<QuestionDatasEntity>?): String? {
        return questionDatas?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toQuestionDatasList(json: String?): List<QuestionDatasEntity>? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromComponentDatasList(componentDatas: List<ComponentDatasEntity>?): String? {
        return componentDatas?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toComponentDatasList(json: String?): List<ComponentDatasEntity>? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromResponseDatasList(responseDatas: List<ResponseDatasEntity>?): String? {
        return responseDatas?.let { Json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toResponseDatasList(json: String?): List<ResponseDatasEntity>? {
        return json?.let { Json.decodeFromString(it) }
    }

    @ColumnTypeConverter
    fun fromAddressList(addressList: List<Address>?): String? =
        addressList?.let { Json.encodeToString(it) }

    @ColumnTypeConverter
    fun toAddressList(json: String?): List<Address>? =
        json?.let { Json.decodeFromString(it) }
}
