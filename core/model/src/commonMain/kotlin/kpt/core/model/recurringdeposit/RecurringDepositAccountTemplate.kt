/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.model.recurringdeposit

import kpt.core.model.objects.template.recurring.AccountChart
import kpt.core.model.objects.template.recurring.Currency
import kpt.core.model.objects.template.recurring.FieldOfficerOption
import kpt.core.model.objects.template.recurring.Timeline
import kpt.core.model.objects.template.recurring.WithdrawalFeeTypeOption
import kpt.core.model.objects.template.recurring.charge.ChargeOption
import kpt.core.model.objects.template.recurring.deposit.DepositType
import kpt.core.model.objects.template.recurring.deposit.InMultiplesOfDepositTermType
import kpt.core.model.objects.template.recurring.deposit.MaxDepositTermType
import kpt.core.model.objects.template.recurring.deposit.MinDepositTermType
import kpt.core.model.objects.template.recurring.interest.InterestCalculationDaysInYearType
import kpt.core.model.objects.template.recurring.interest.InterestCalculationDaysInYearTypeOption
import kpt.core.model.objects.template.recurring.interest.InterestCalculationType
import kpt.core.model.objects.template.recurring.interest.InterestCalculationTypeOption
import kpt.core.model.objects.template.recurring.interest.InterestCompoundingPeriodType
import kpt.core.model.objects.template.recurring.interest.InterestCompoundingPeriodTypeOption
import kpt.core.model.objects.template.recurring.interest.InterestPostingPeriodType
import kpt.core.model.objects.template.recurring.interest.InterestPostingPeriodTypeOption
import kpt.core.model.objects.template.recurring.interest.PreClosurePenalInterestOnTypeOption
import kpt.core.model.objects.template.recurring.period.LockinPeriodFrequencyType
import kpt.core.model.objects.template.recurring.period.LockinPeriodFrequencyTypeOption
import kpt.core.model.objects.template.recurring.period.PeriodFrequencyTypeOption
import kpt.core.model.objects.template.recurring.period.ProductOption
import kpt.core.model.utils.IgnoredOnParcel
import kpt.core.model.utils.Parcelable
import kpt.core.model.utils.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class RecurringDepositAccountTemplate(
    @IgnoredOnParcel val accountChart: AccountChart? = null,
    val adjustAdvanceTowardsFuturePayments: Boolean? = null,
    val allowWithdrawal: Boolean? = null,
    @IgnoredOnParcel val chargeOptions: List<ChargeOption>? = null,
    val clientId: Int? = null,
    val clientName: String? = null,
    @IgnoredOnParcel val currency: Currency? = null,
    val depositProductId: Int? = null,
    val depositProductName: String? = null,
    @IgnoredOnParcel val depositType: DepositType? = null,
    @IgnoredOnParcel val fieldOfficerOptions: List<FieldOfficerOption>? = null,
    @IgnoredOnParcel val inMultiplesOfDepositTermType: InMultiplesOfDepositTermType? = null,
    @IgnoredOnParcel val interestCalculationDaysInYearType: InterestCalculationDaysInYearType? = null,
    @IgnoredOnParcel val interestCalculationDaysInYearTypeOptions: List<InterestCalculationDaysInYearTypeOption>? = null,
    @IgnoredOnParcel val interestCalculationType: InterestCalculationType? = null,
    @IgnoredOnParcel val interestCalculationTypeOptions: List<InterestCalculationTypeOption>? = null,
    @IgnoredOnParcel val interestCompoundingPeriodType: InterestCompoundingPeriodType? = null,
    @IgnoredOnParcel val interestCompoundingPeriodTypeOptions: List<InterestCompoundingPeriodTypeOption>? = null,
    @IgnoredOnParcel val interestPostingPeriodType: InterestPostingPeriodType? = null,
    @IgnoredOnParcel val interestPostingPeriodTypeOptions: List<InterestPostingPeriodTypeOption>? = null,
    val isCalendarInherited: Boolean? = null,
    val isMandatoryDeposit: Boolean? = null,
    val lockinPeriodFrequency: Int? = null,
    @IgnoredOnParcel val lockinPeriodFrequencyType: LockinPeriodFrequencyType? = null,
    @IgnoredOnParcel val lockinPeriodFrequencyTypeOptions: List<LockinPeriodFrequencyTypeOption>? = null,
    @IgnoredOnParcel val maxDepositTermType: MaxDepositTermType? = null,
    val minDepositTerm: Int? = null,
    @IgnoredOnParcel val minDepositTermType: MinDepositTermType? = null,
    val nominalAnnualInterestRate: Double? = null,
    @IgnoredOnParcel val periodFrequencyTypeOptions: List<PeriodFrequencyTypeOption>? = null,
    val preClosurePenalApplicable: Boolean? = null,
    @IgnoredOnParcel val preClosurePenalInterestOnTypeOptions: List<PreClosurePenalInterestOnTypeOption>? = null,
    @IgnoredOnParcel val productOptions: List<ProductOption>? = null,
    @IgnoredOnParcel val timeline: Timeline? = null,
    val withHoldTax: Boolean? = null,
    val withdrawalFeeForTransfers: Boolean? = null,
    @IgnoredOnParcel val withdrawalFeeTypeOptions: List<WithdrawalFeeTypeOption>? = null,
) : Parcelable
