/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.network.mifos.staff.mapper

import kpt.core.network.data.AbstractMapper
import kpt.core.network.mifos.shared.dto.RetrieveOneResponse
import kpt.core.database.staff.entity.StaffEntity

object StaffMapper : AbstractMapper<RetrieveOneResponse, StaffEntity>() {
    override fun mapFromEntity(entity: RetrieveOneResponse): StaffEntity {
        return StaffEntity(
            id = entity.id!!.toInt(),
            firstname = entity.firstname,
            lastname = entity.lastname,
            displayName = entity.displayName,
            officeId = entity.officeId!!.toInt(),
            officeName = entity.officeName,
            isLoanOfficer = entity.isLoanOfficer,
            isActive = entity.isActive,
        )
    }

    override fun mapToEntity(domainModel: StaffEntity): RetrieveOneResponse {
        return RetrieveOneResponse(
            id = domainModel.id?.toLong(),
            firstname = domainModel.firstname,
            lastname = domainModel.lastname,
            displayName = domainModel.displayName,
            officeId = domainModel.officeId?.toLong(),
            officeName = domainModel.officeName,
            isLoanOfficer = domainModel.isLoanOfficer,
            isActive = domainModel.isActive,
        )
    }
}
