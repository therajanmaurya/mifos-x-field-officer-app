/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.network.mifos.search.api

import kpt.core.base.network.annotation.ApiBinding

import com.mifos.core.model.objects.SearchedEntity
import kpt.core.database.basemodel.APIEndPoint
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

/**
 * @author fomenkoo
 */
@ApiBinding("mifos")
interface SearchApi {

    @GET(APIEndPoint.SEARCH)
    suspend fun searchResources(
        @Query("query") query: String,
        @Query("resource") resource: String?,
        @Query("exactMatch") exactMatch: Boolean?,
    ): List<SearchedEntity>
}
