/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.network.mifos.loan.dto

import kpt.core.model.shared.CurrencyDto
import kpt.core.model.shared.PaymentTypeOptionDto
import kotlinx.serialization.Serializable

@Serializable
data class LoanDisburseTemplateDto(
    val netDisbursalAmount: Double,
    val date: List<Int>,
    val currency: CurrencyDto? = null,
    val paymentTypeOptions: List<PaymentTypeOptionDto> = emptyList(),
)
