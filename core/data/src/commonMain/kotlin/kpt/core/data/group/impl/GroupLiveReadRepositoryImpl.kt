/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.data.group.impl

import kpt.core.base.data.annotation.RepositoryBinding
import kpt.core.data.group.GroupLiveReadRepository
import kpt.core.network.mifos.group.api.GroupApi
import kpt.core.network.mifos.group.dto.GetGroupsResponse

@RepositoryBinding(binds = GroupLiveReadRepository::class)
internal class GroupLiveReadRepositoryImpl(
    private val groupApi: GroupApi,
) : GroupLiveReadRepository {

    override suspend fun retrieveAll24(officeId: Long?, staffId: Long?, externalId: String?, name: String?, underHierarchy: String?, paged: Boolean?, offset: Int?, limit: Int?, orderBy: String?, sortOrder: String?, orphansOnly: Boolean?): GetGroupsResponse =
        groupApi.retrieveAll24(officeId = officeId, staffId = staffId, externalId = externalId, name = name, underHierarchy = underHierarchy, paged = paged, offset = offset, limit = limit, orderBy = orderBy, sortOrder = sortOrder, orphansOnly = orphansOnly)
}
