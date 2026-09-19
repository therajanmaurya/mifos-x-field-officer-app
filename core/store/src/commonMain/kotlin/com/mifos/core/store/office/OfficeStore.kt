/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package com.mifos.core.store.office

import com.mifos.core.network.services.OfficeService
import com.mifos.room.dao.OfficeDao
import com.mifos.room.entities.organisation.OfficeEntity
import kotlinx.coroutines.flow.first
import kpt.core.base.store.annotation.CacheKey
import kpt.core.base.store.annotation.StoreProvider
import kpt.core.base.store.infra.StoreFactory
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store

/**
 * Offices — READ-CACHE archetype (`createStore`).
 *
 * The first vertical of the DataManager→Store5 collapse. It replaces the
 * `Repository → DataManagerOffice → { OfficeService , OfficeDaoHelper }` chain, in which the
 * Room branch was commented out and every read went to the network — so this store is not a
 * re-wrapping of the old path, it is the offline-first read that path intended.
 *
 * `fetcher` is the Ktorfit service; `sourceOfTruth` is the Room DAO, so a field officer with no
 * connectivity still renders the office list from cache and a reconnect refreshes it. Offices are
 * reference data — server-authoritative, low-churn, read-only on the device — which is exactly
 * the read-cache shape rather than a ledger (no delta/paging) or a mutable store (no local writes).
 */
@StoreProvider(id = "offices")
@CacheKey(name = "LIST", key = "offices")
fun provideOfficeStore(
    service: OfficeService,
    dao: OfficeDao,
): Store<Unit, List<OfficeEntity>> = StoreFactory.createStore(
    fetcher = Fetcher.of { _: Unit -> service.allOffices().first() },
    sourceOfTruth = SourceOfTruth.of(
        reader = { _: Unit -> dao.getAllOffices() },
        writer = { _: Unit, offices: List<OfficeEntity> -> dao.insertOffices(offices) },
    ),
)
