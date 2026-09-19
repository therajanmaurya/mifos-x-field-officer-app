/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.network.mifos.checkerinbox.api

import kpt.core.base.network.annotation.ApiBinding

import com.mifos.core.model.objects.checkerinboxtask.CheckerInboxSearchTemplate
import com.mifos.core.model.objects.checkerinboxtask.CheckerTask
import com.mifos.core.model.objects.checkerinboxtask.RescheduleLoansTask
import kpt.core.model.shared.GenericResponse
import kpt.core.database.basemodel.APIEndPoint
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

@ApiBinding("mifos")
interface CheckerInboxApi {

    @GET(APIEndPoint.MAKER_CHECKER)
    suspend fun getCheckerList(
        @Query("actionName") actionName: String? = null,
        @Query("entityName") entityName: String? = null,
        @Query("resourceId") resourceId: Int? = null,
    ): List<CheckerTask>

    @POST(APIEndPoint.MAKER_CHECKER + "/{auditId}?command=approve")
    suspend fun approveCheckerEntry(@Path("auditId") auditId: Int): GenericResponse

    @POST(APIEndPoint.MAKER_CHECKER + "/{auditId}?command=reject")
    suspend fun rejectCheckerEntry(@Path("auditId") auditId: Int): GenericResponse

    @DELETE(APIEndPoint.MAKER_CHECKER + "/{auditId}")
    suspend fun deleteCheckerEntry(@Path("auditId") auditId: Int): GenericResponse

    @GET("rescheduleloans?command=pending")
    suspend fun getRescheduleLoansTaskList(): List<RescheduleLoansTask>

    @GET(APIEndPoint.MAKER_CHECKER + "/searchtemplate?fields=entityNames,actionNames")
    suspend fun getCheckerInboxSearchTemplate(): CheckerInboxSearchTemplate

    @GET(APIEndPoint.MAKER_CHECKER)
    suspend fun getCheckerTasksFromResourceId(
        @Query("actionName") actionName: String? = null,
        @Query("entityName") entityName: String? = null,
        @Query("resourceId") resourceId: Int? = null,
    ): List<CheckerTask>
}
