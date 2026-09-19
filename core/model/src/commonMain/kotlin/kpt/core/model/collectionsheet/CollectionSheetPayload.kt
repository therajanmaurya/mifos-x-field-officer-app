/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.model.collectionsheet

import kpt.core.model.utils.ApiDateFormatter
import kpt.core.model.objects.collectionsheets.BulkSavingsDueTransaction
import kpt.core.model.client.ClientsAttendance
import kpt.core.model.shared.BulkRepaymentTransactions
import kotlinx.serialization.Serializable

/**
 * Created by Tarun on 31-07-17.
 */
@Serializable
data class CollectionSheetPayload(
    var actualDisbursementDate: String? = null,

    var bulkRepaymentTransactions: MutableList<BulkRepaymentTransactions> = ArrayList(),
    var bulkSavingsDueTransactions: MutableList<BulkSavingsDueTransaction> = ArrayList(),

    var calendarId: Int? = 0,

    var clientsAttendance: MutableList<ClientsAttendance> = ArrayList(),

    var dateFormat: String = ApiDateFormatter.DATE_FORMAT,

    var locale: String = ApiDateFormatter.LOCALE,

    var transactionDate: String? = null,

    var accountNumber: String? = null,

    var bankNumber: String? = null,

    var checkNumber: String? = null,

    var paymentTypeId: Int = 0,

    var receiptNumber: String? = null,

    var routingCode: String? = null,
)