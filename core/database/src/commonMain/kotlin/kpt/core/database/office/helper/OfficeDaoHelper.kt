/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.database.office.helper

import kpt.core.database.office.dao.OfficeDao
import kpt.core.database.office.entity.OfficeEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfficeDaoHelper(
    private val officeDao: OfficeDao,
) {

    suspend fun saveAllOffices(offices: List<OfficeEntity>) {
        val officeEntities = offices.map { it }
        officeDao.insertOffices(officeEntities)
    }

    fun readAllOffices(): Flow<List<OfficeEntity>> {
        return officeDao.getAllOffices()
            .map { entities -> entities.map { it } }
    }
}
