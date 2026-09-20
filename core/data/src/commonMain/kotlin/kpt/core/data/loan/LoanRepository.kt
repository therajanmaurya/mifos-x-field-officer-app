/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.data.loan

import kotlinx.coroutines.CoroutineScope
import kpt.core.base.store.screen.ScreenDataStream
import kpt.core.model.objects.account.loan.loanWithAssociations.LoanWithAssociations

interface LoanRepository {
    /** One loan with its full associations — summary, timeline, schedule, transactions. */
    fun loanStream(loanId: Int, scope: CoroutineScope): ScreenDataStream<LoanWithAssociations>
}
