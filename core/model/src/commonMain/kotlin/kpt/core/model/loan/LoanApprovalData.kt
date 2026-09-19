/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.model.loan

import kpt.core.model.objects.account.loan.loanWithAssociations.LoanWithAssociations

data class LoanApprovalData(
    val loanID: Int,
    val loanWithAssociations: LoanWithAssociations,
)
