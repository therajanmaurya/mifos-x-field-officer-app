/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.data.report.impl

import kpt.core.base.data.annotation.RepositoryBinding
import kpt.core.data.report.ReportLiveReadRepository
import kpt.core.network.mifos.report.api.RunReportsApi

@RepositoryBinding(binds = ReportLiveReadRepository::class)
internal class ReportLiveReadRepositoryImpl(
    private val runReportsApi: RunReportsApi,
) : ReportLiveReadRepository {

    override suspend fun getSavingsAccountTransactionReceipt(transactionId: Int, dateFormat: String, outputType: String, locale: String): ByteArray =
        runReportsApi.getSavingsAccountTransactionReceipt(transactionId = transactionId, dateFormat = dateFormat, outputType = outputType, locale = locale)
}
