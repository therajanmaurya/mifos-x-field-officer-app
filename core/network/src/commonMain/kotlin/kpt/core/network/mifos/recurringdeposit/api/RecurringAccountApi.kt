/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.network.mifos.recurringdeposit.api

import kpt.core.base.network.annotation.ApiBinding

import com.mifos.core.model.objects.payloads.RecurringDepositAccountPayload
import kpt.core.common.APIEndPoint
import kpt.core.model.recurringdeposit.RecurringDepositAccountTemplate
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.statement.HttpResponse

@ApiBinding("mifos")
interface RecurringAccountApi {

    @POST(APIEndPoint.CREATE_RECURRING_DEPOSIT_ACCOUNTS)
    suspend fun createRecurringDepositAccount(
        @Body recurringDepositAccountPayload: RecurringDepositAccountPayload?,
    ): HttpResponse

    @GET(APIEndPoint.CREATE_RECURRING_DEPOSIT_ACCOUNTS + "/template")
    suspend fun getRecurringDepositAccountTemplate(
        @Query("clientId") clientId: Int,
        @Query("productId") productId: Int?,
    ): RecurringDepositAccountTemplate
}
