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

import kpt.core.base.data.annotation.RepositoryBinding
import kpt.core.data.staff.StaffLiveReadRepository
import kpt.core.model.shared.RetrieveOneResponse
import kpt.core.network.mifos.staff.api.StaffApi

@RepositoryBinding(binds = StaffLiveReadRepository::class)
internal class StaffLiveReadRepositoryImpl(
    private val staffApi: StaffApi,
) : StaffLiveReadRepository {

    override suspend fun retrieveAll16(officeId: Long?, staffInOfficeHierarchy: Boolean?, loanOfficersOnly: Boolean?, status: String?): List<RetrieveOneResponse> =
        staffApi.retrieveAll16(officeId = officeId, staffInOfficeHierarchy = staffInOfficeHierarchy, loanOfficersOnly = loanOfficersOnly, status = status)
}
