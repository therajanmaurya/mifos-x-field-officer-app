/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.data.staff.impl

import kotlinx.coroutines.CoroutineScope
import kpt.core.base.data.annotation.FromStore
import kpt.core.base.data.annotation.RepositoryBinding
import kpt.core.base.store.screen.ScreenDataStream
import kpt.core.base.store.screen.asScreenStream
import kpt.core.data.staff.StaffRepository
import kpt.core.store.config.AppCacheKeys
import kpt.core.store.config.AppStoreIds
import kpt.core.store.config.AppStoreRegistry
import org.mobilenativefoundation.store.store5.Store
import kpt.core.database.staff.entity.StaffEntity

@RepositoryBinding(binds = StaffRepository::class)
internal class StaffRepositoryImpl(
    @FromStore(AppStoreIds.Staff) private val store: Store<Int, List<StaffEntity>>,
) : StaffRepository {

    // CACHE_FIRST_SWR is the asScreenStream default and is NOT overridden: offline with an empty
    // cache must render this app's own Empty, not a blocking NoNetwork. The ttl is the declared
    // freshness bound — it drives the "updated N ago" indicator, never whether cache is served.
    override fun staffStream(officeId: Int, scope: CoroutineScope): ScreenDataStream<List<StaffEntity>> =
        store.asScreenStream(
            key = officeId,
            cacheKey = AppCacheKeys.Staff.forOffice(officeId),
            scope = scope,
        isEmpty = { it.isEmpty() },
            ttl = AppStoreRegistry.Ttl.STAFF,
        )
}
