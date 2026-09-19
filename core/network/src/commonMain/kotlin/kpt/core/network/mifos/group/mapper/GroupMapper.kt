/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.network.mifos.group.mapper

import kpt.core.network.data.AbstractMapper
import kpt.core.network.mifos.group.dto.GetGroupsPageItems
import kpt.core.network.mifos.group.dto.GetGroupsStatus
import kpt.core.database.client.entity.ClientStatusEntity
import kpt.core.database.group.entity.GroupEntity

object GroupMapper : AbstractMapper<GetGroupsPageItems, GroupEntity>() {

    override fun mapFromEntity(entity: GetGroupsPageItems): GroupEntity {
        return GroupEntity(
            id = entity.id?.toInt(),
            name = entity.name,
            active = entity.active,
            officeId = entity.officeId?.toInt(),
            officeName = entity.officeName,
            hierarchy = entity.hierarchy,
            status = ClientStatusEntity(
                id = entity.status?.id!!.toInt(),
                code = entity.status?.code,
                value = entity.status?.description,
            ),
        )
    }

    override fun mapToEntity(domainModel: GroupEntity): GetGroupsPageItems {
        return GetGroupsPageItems(
            id = domainModel.id?.toLong(),
            name = domainModel.name,
            active = domainModel.active,
            officeId = domainModel.officeId?.toLong(),
            officeName = domainModel.officeName,
            hierarchy = domainModel.hierarchy,
            status = GetGroupsStatus(
                id = domainModel.status?.id?.toLong(),
                code = domainModel.status?.code,
                description = domainModel.status?.value,
            ),
        )
    }
}
