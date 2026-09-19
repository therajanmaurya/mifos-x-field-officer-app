/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.network.mifos.office.api

import kpt.core.base.network.annotation.ApiBinding

import kpt.core.network.mifos.office.dto.GetOfficesResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.coroutines.flow.Flow
import kpt.core.database.basemodel.APIEndPoint
import kpt.core.database.office.entity.OfficeEntity

@ApiBinding("mifos")
interface OfficeApi {


    /**
     * List Offices
     * Example Requests:  offices   offices?fields&#x3D;id,name,openingDate
     * Responses:
     *  - 200: OK
     *
     * @param includeAllOffices includeAllOffices (optional, default to false)
     * @param orderBy orderBy (optional)
     * @param sortOrder sortOrder (optional)
     * @return [kotlin.collections.List<GetOfficesResponse]
     */
    @GET("offices")
    fun retrieveOffices(
        @Query("includeAllOffices") includeAllOffices: Boolean? = false,
        @Query("orderBy") orderBy: String? = null,
        @Query("sortOrder") sortOrder: String? = null,
    ): Flow<List<GetOfficesResponse>>

    /**
     * Fetches List of All the Offices
     *
     * @param listOfOfficesCallback
     */
    @GET(APIEndPoint.OFFICES)
    fun allOffices(): Flow<List<OfficeEntity>>
}
