/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package com.mifos.core.store.group

import com.mifos.core.network.services.GroupService
import com.mifos.room.dao.GroupsDao
import com.mifos.room.entities.group.GroupEntity
import kotlinx.coroutines.flow.first
import kpt.core.base.store.annotation.CacheKey
import kpt.core.base.store.annotation.StoreProvider
import kpt.core.base.store.infra.StoreFactory
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store

/**
 * Groups — READ-CACHE (`createStore`). Same shape as centers; groups sit under a center in the
 * Fineract hierarchy and are read on the same offline paths.
 */
@StoreProvider(id = "groups")
@CacheKey(name = "LIST", key = "groups")
fun provideGroupStore(
    service: GroupService,
    dao: GroupsDao,
): Store<Unit, List<GroupEntity>> = StoreFactory.createStore(
    fetcher = Fetcher.of { _: Unit ->
        service.getGroups(b = true, offset = 0, limit = GROUP_PAGE_LIMIT).first().pageItems
    },
    sourceOfTruth = SourceOfTruth.of(
        reader = { _: Unit -> dao.getAllGroups() },
        writer = { _: Unit, groups: List<GroupEntity> -> groups.forEach { dao.insertGroup(it) } },
    ),
)

private const val GROUP_PAGE_LIMIT = 100
