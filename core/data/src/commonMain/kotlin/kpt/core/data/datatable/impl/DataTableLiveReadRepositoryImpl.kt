/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.data.datatable.impl

import kotlinx.serialization.json.JsonArray
import kpt.core.base.data.annotation.RepositoryBinding
import kpt.core.data.datatable.DataTableLiveReadRepository
import kpt.core.network.mifos.datatable.api.DataTableApi

@RepositoryBinding(binds = DataTableLiveReadRepository::class)
internal class DataTableLiveReadRepositoryImpl(
    private val dataTableApi: DataTableApi,
) : DataTableLiveReadRepository {

    override suspend fun getDataOfDataTable(dataTableName: String, entityId: Int): JsonArray =
        dataTableApi.getDataOfDataTable(dataTableName = dataTableName, entityId = entityId)
}
