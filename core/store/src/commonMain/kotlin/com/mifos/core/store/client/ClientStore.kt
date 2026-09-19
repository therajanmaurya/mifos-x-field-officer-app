/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package com.mifos.core.store.client

import com.mifos.core.network.services.ClientService
import com.mifos.room.dao.ClientDao
import com.mifos.room.entities.client.ClientEntity
import kotlinx.coroutines.flow.first
import kpt.core.base.store.annotation.CacheKey
import kpt.core.base.store.annotation.StoreProvider
import kpt.core.base.store.infra.StoreFactory
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store

/**
 * Clients — READ-CACHE (`createStore`).
 *
 * The app's hub entity: most other features are reached THROUGH a client, so this is the store a
 * field officer depends on most when offline. Room answers from cache immediately and a reconnect
 * refreshes it.
 */
@StoreProvider(id = "clients")
@CacheKey(name = "LIST", key = "clients")
fun provideClientStore(
    service: ClientService,
    dao: ClientDao,
): Store<Unit, List<ClientEntity>> = StoreFactory.createStore(
    fetcher = Fetcher.of { _: Unit ->
        service.getAllClients(b = true, offset = 0, limit = CLIENT_PAGE_LIMIT).first().pageItems
    },
    sourceOfTruth = SourceOfTruth.of(
        reader = { _: Unit -> dao.getAllClients() },
        writer = { _: Unit, clients: List<ClientEntity> -> clients.forEach { dao.insertClient(it) } },
    ),
)

private const val CLIENT_PAGE_LIMIT = 100
