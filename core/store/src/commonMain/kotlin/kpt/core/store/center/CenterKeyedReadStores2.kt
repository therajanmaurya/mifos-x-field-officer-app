/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.store.center

import kpt.core.base.store.annotation.CacheKey
import kpt.core.base.store.annotation.StoreProvider
import kpt.core.database.cache.dao.ApiResponseCacheDao
import kpt.core.database.center.entity.CenterEntity
import kpt.core.network.mifos.center.api.CenterApi
import kpt.core.store.cache.cachedRead
import kpt.core.store.config.AppCacheKeys
import org.mobilenativefoundation.store.store5.Store

/**
 * Keyed reads for center with no table of their own — cached through the shared read cache so the
 * last-known answer still renders with no signal.
 */


/** Addresses one `getAllCentersInOffice` read. */
data class GetAllCentersInOfficeKey(
    val officeId: Int,
    val additionalParams: Map<String, String>,
)
@StoreProvider(id = "centerGetAllCentersInOffice", ttl = "12h")
@CacheKey(fn = "forKey", key = "centerGetAllCentersInOffice:{key}", params = ["key:String"])
fun provideGetAllCentersInOfficeStore(
    centerApi: CenterApi,
    cache: ApiResponseCacheDao,
): Store<GetAllCentersInOfficeKey, List<CenterEntity>> = cachedRead(
    dao = cache,
    keyOf = { key -> AppCacheKeys.CenterGetAllCentersInOffice.forKey("${key.officeId}:${key.additionalParams}") },
    fetch = { key -> centerApi.getAllCentersInOffice(officeId = key.officeId, additionalParams = key.additionalParams) },
)
