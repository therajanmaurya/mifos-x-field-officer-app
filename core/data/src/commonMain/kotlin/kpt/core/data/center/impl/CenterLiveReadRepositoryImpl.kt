/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.data.center.impl

import kpt.core.base.data.annotation.RepositoryBinding
import kpt.core.data.center.CenterLiveReadRepository
import kpt.core.model.objects.databaseobjects.OfflineCenter
import kpt.core.network.mifos.center.api.CenterApi
import kpt.core.network.mifos.center.dto.GetCentersResponse

@RepositoryBinding(binds = CenterLiveReadRepository::class)
internal class CenterLiveReadRepositoryImpl(
    private val centerApi: CenterApi,
) : CenterLiveReadRepository {

    override suspend fun retrieveAll23(officeId: Long?, staffId: Long?, externalId: String?, name: String?, underHierarchy: String?, paged: Boolean?, offset: Int?, limit: Int?, orderBy: String?, sortOrder: String?, meetingDate: String?, dateFormat: String?, locale: String?): GetCentersResponse =
        centerApi.retrieveAll23(officeId = officeId, staffId = staffId, externalId = externalId, name = name, underHierarchy = underHierarchy, paged = paged, offset = offset, limit = limit, orderBy = orderBy, sortOrder = sortOrder, meetingDate = meetingDate, dateFormat = dateFormat, locale = locale)

    override suspend fun getCenterList(dateFormat: String?, locale: String?, meetingDate: String?, officeId: Int, staffId: Int): List<OfflineCenter> =
        centerApi.getCenterList(dateFormat = dateFormat, locale = locale, meetingDate = meetingDate, officeId = officeId, staffId = staffId)
}
