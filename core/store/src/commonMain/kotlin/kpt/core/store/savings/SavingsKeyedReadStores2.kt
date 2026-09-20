/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.store.savings

import kpt.core.base.store.annotation.CacheKey
import kpt.core.base.store.annotation.StoreProvider
import kpt.core.database.cache.dao.ApiResponseCacheDao
import kpt.core.database.savings.entity.SavingsAccountTransactionTemplateEntity
import kpt.core.network.mifos.savings.api.SavingsAccountApi
import kpt.core.store.cache.cachedRead
import kpt.core.store.config.AppCacheKeys
import org.mobilenativefoundation.store.store5.Store

/**
 * Keyed reads for savings with no table of their own — cached through the shared read cache so the
 * last-known answer still renders with no signal.
 */


/** Addresses one `getSavingsAccountTransactionTemplate` read. */
data class GetSavingsAccountTransactionTemplateKey(
    val savingsAccountType: String,
    val savingsAccountId: Int,
    val transactionType: String?,
)
@StoreProvider(id = "savingsGetSavingsAccountTransactionTemplate", ttl = "12h")
@CacheKey(fn = "forKey", key = "savingsGetSavingsAccountTransactionTemplate:{key}", params = ["key:String"])
fun provideGetSavingsAccountTransactionTemplateStore(
    savingsAccountApi: SavingsAccountApi,
    cache: ApiResponseCacheDao,
): Store<GetSavingsAccountTransactionTemplateKey, SavingsAccountTransactionTemplateEntity> = cachedRead(
    dao = cache,
    keyOf = { key -> AppCacheKeys.SavingsGetSavingsAccountTransactionTemplate.forKey("${key.savingsAccountType}:${key.savingsAccountId}:${key.transactionType}") },
    fetch = { key -> savingsAccountApi.getSavingsAccountTransactionTemplate(savingsAccountType = key.savingsAccountType, savingsAccountId = key.savingsAccountId, transactionType = key.transactionType) },
)
