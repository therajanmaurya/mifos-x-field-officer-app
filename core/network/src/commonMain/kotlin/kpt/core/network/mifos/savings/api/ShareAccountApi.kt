/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.network.mifos.savings.api

import kpt.core.base.network.annotation.ApiBinding

import kpt.core.network.mifos.savings.dto.ShareAccountPayload
import kpt.core.network.mifos.savings.dto.ShareTemplate
import kpt.core.database.basemodel.APIEndPoint
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.statement.HttpResponse

@ApiBinding("mifos")
interface ShareAccountApi {
    @GET("accounts/" + APIEndPoint.SHARE + "/template")
    suspend fun shareProductTemplate(
        @Query("clientId") clientId: Int,
        @Query("productId") productId: Int?,
    ): ShareTemplate

    @POST("accounts/" + APIEndPoint.SHARE)
    suspend fun createShareAccount(
        @Body shareAccountPayload: ShareAccountPayload,
    ): HttpResponse
}
