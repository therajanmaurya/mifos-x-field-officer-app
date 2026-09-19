/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package com.mifos.core.data.repository

import com.mifos.core.common.utils.DataState
import kpt.core.model.objects.responses.SaveResponse
import kpt.core.database.group.entity.GroupPayloadEntity
import kpt.core.database.office.entity.OfficeEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Aditya Gupta on 10/08/23.
 */
interface CreateNewGroupRepository {

    fun offices(): Flow<DataState<List<OfficeEntity>>>

    suspend fun createGroup(groupPayload: GroupPayloadEntity): SaveResponse
}
