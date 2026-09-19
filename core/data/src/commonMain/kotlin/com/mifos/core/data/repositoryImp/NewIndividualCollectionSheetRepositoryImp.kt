/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package com.mifos.core.data.repositoryImp

import com.mifos.core.common.utils.DataState
import com.mifos.core.common.utils.asDataStateFlow
import com.mifos.core.data.repository.NewIndividualCollectionSheetRepository
import kpt.core.network.DataManager
import kpt.core.network.collectionsheet.datamanager.DataManagerCollectionSheet
import kpt.core.network.mifos.collectionsheet.dto.RequestCollectionSheetPayload
import kpt.core.database.collectionsheet.entity.IndividualCollectionSheet
import kpt.core.database.office.entity.OfficeEntity
import kpt.core.database.staff.entity.StaffEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Aditya Gupta on 10/08/23.
 */
class NewIndividualCollectionSheetRepositoryImp(
    private val dataManager: DataManager,
    private val dataManagerCollection: DataManagerCollectionSheet,
) : NewIndividualCollectionSheetRepository {

    override suspend fun getIndividualCollectionSheet(payload: RequestCollectionSheetPayload?): IndividualCollectionSheet {
        return dataManagerCollection.getIndividualCollectionSheet(payload)
    }

    override fun offices(): Flow<DataState<List<OfficeEntity>>> {
        return dataManager.offices().asDataStateFlow()
    }

    override fun getStaffInOffice(officeId: Int): Flow<DataState<List<StaffEntity>>> {
        return dataManager.getStaffInOffice(officeId)
            .asDataStateFlow()
    }
}
