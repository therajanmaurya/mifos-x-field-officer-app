/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.network.mifos.client.api

import kpt.core.base.network.annotation.ApiBinding

import kpt.core.database.client.entity.ClientAccounts
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path

/**
 * @author fomenkoo
 */
@ApiBinding("mifos")
interface ClientAccountsApi {
    @GET("clients/{clientId}/accounts")
    suspend fun getAllAccountsOfClient(@Path("clientId") clientId: Int): ClientAccounts
}
