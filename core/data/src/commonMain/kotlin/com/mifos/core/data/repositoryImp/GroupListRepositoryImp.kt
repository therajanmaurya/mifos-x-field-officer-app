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
import com.mifos.core.data.repository.GroupListRepository
import kpt.core.network.DataManager
import kpt.core.database.center.entity.CenterWithAssociations
import kpt.core.database.group.entity.GroupWithAssociations
import kotlinx.coroutines.flow.Flow

/**
 * Created by Aditya Gupta on 06/08/23.
 */
class GroupListRepositoryImp(
    private val dataManager: DataManager,
) : GroupListRepository {

    override fun getGroups(groupId: Int): Flow<DataState<GroupWithAssociations>> {
        return dataManager.getGroups(groupId).asDataStateFlow()
    }

    override fun getGroupsByCenter(id: Int): Flow<DataState<CenterWithAssociations>> {
        return dataManager.getGroupsByCenter(id).asDataStateFlow()
    }
}
