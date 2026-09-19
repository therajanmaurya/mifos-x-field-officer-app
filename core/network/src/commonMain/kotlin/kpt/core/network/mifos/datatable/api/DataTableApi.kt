/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.network.mifos.datatable.api

import com.mifos.core.model.objects.users.UserLocation
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonArray
import kpt.core.base.network.annotation.ApiBinding
import kpt.core.database.basemodel.APIEndPoint
import kpt.core.database.datatable.entity.DataTableEntity
import kpt.core.network.GenericResponse
import kpt.core.network.mifos.datatable.dto.DeleteDataTablesDatatableAppTableIdDatatableIdResponse
import kpt.core.network.mifos.datatable.dto.GetDataTablesResponse
import kpt.core.network.mifos.shared.dto.Payload



@ApiBinding("mifos")
interface DataTableApi {


    /**
     * List Data Tables
     * Lists registered data tables and the Apache Fineract Core application table they are registered to.  ARGUMENTS  apptable  - optional The Apache Fineract core application table.  Example Requests:  datatables?apptable&#x3D;m_client   datatables
     * Responses:
     *  - 200: OK
     *
     * @param apptable apptable (optional)
     * @return [kotlin.collections.List<GetDataTablesResponse>]
     */
    @GET("datatables")
    fun getDatatables(@Query("apptable") apptable: String? = null): Flow<List<GetDataTablesResponse>>

    /**
     * Delete Entry in Datatable (One to Many)
     * Deletes the entry (if it exists) for data tables that are one to many with the application table.
     * Responses:
     *  - 200: OK
     *
     * @param datatable datatable
     * @param apptableId apptableId
     * @param datatableId datatableId
     * @return [DeleteDataTablesDatatableAppTableIdDatatableIdResponse]
     */
    @DELETE("datatables/{datatable}/{apptableId}/{datatableId}")
    suspend fun deleteDatatableEntry(
        @Path("datatable") datatable: String,
        @Path("apptableId") apptableId: Long,
        @Path("datatableId") datatableId: Long,
    ): DeleteDataTablesDatatableAppTableIdDatatableIdResponse

    @GET(APIEndPoint.DATATABLES)
    fun getTableOf(@Query("apptable") table: String?): Flow<List<DataTableEntity>>

    @GET(APIEndPoint.DATATABLES + "/{dataTableName}/{entityId}/")
    suspend fun getDataOfDataTable(
        @Path("dataTableName") dataTableName: String,
        @Path("entityId") entityId: Int,
    ): JsonArray

    // TODO Improve Body Implementation with Payload
    @POST(APIEndPoint.DATATABLES + "/{dataTableName}/{entityId}/")
    suspend fun createEntryInDataTable(
        @Path("dataTableName") dataTableName: String,
        @Path("entityId") entityId: Int,
        @Body requestPayload: Map<String, String>,
    ): GenericResponse

    @DELETE(APIEndPoint.DATATABLES + "/{dataTableName}/{entityId}/{dataTableRowId}")
    fun deleteEntryOfDataTableManyToMany(
        @Path("dataTableName") dataTableName: String,
        @Path("entityId") entityId: Int,
        @Path("dataTableRowId") dataTableRowId: Int,
    ): Flow<GenericResponse>

    @POST(APIEndPoint.DATATABLES + "/m_staff_path_tracking/{userId}")
    fun addUserPathTracking(
        @Path("userId") userId: Int,
        @Body userLocation: UserLocation?,
    ): Flow<GenericResponse>

    @GET(APIEndPoint.DATATABLES + "/m_staff_path_tracking/{userId}")
    fun getUserPathTracking(@Path("userId") userId: Int): Flow<List<UserLocation>>
}
