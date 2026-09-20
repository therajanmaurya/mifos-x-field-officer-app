/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.data.charge.impl

import io.ktor.client.statement.HttpResponse
import kpt.core.base.data.annotation.RepositoryBinding
import kpt.core.data.charge.ChargeLiveReadRepository
import kpt.core.network.mifos.charge.api.ChargeApi

@RepositoryBinding(binds = ChargeLiveReadRepository::class)
internal class ChargeLiveReadRepositoryImpl(
    private val chargeApi: ChargeApi,
) : ChargeLiveReadRepository {

    override suspend fun listAllCharges(): HttpResponse =
        chargeApi.listAllCharges()
}
