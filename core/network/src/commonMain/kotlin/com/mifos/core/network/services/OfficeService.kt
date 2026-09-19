/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package com.mifos.core.network.services

import kpt.core.database.basemodel.APIEndPoint
import kpt.core.database.office.entity.OfficeEntity
import de.jensklingenberg.ktorfit.http.GET
import kotlinx.coroutines.flow.Flow

/**
 * @author fomenkoo
 */
interface OfficeService {
    /**
     * Fetches List of All the Offices
     *
     * @param listOfOfficesCallback
     */
    @GET(APIEndPoint.OFFICES)
    fun allOffices(): Flow<List<OfficeEntity>>
}
