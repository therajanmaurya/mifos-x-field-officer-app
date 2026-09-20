/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.data.center

import kpt.core.model.objects.databaseobjects.OfflineCenter
import kpt.core.network.mifos.center.dto.GetCentersResponse

/**
 * Center reads that are deliberately NOT cached.
 *
 * Everything else in this module is offline-first; these are the exceptions, and each one is an
 * exception for a stated reason rather than an omission. They fail with no connectivity — a caller
 * must treat that as expected, not as an error worth retrying forever.
 */
interface CenterLiveReadRepository {

    /** Online-only: filtered query — the key space is a cross-product of a dozen optional filters, so caching per combination fills the table with rows nobody asks for twice. */
    suspend fun retrieveAll23(officeId: Long? = null, staffId: Long? = null, externalId: String? = null, name: String? = null, underHierarchy: String? = null, paged: Boolean? = null, offset: Int? = null, limit: Int? = null, orderBy: String? = null, sortOrder: String? = null, meetingDate: String? = null, dateFormat: String? = null, locale: String? = null): GetCentersResponse

    /** Online-only: `OfflineCenter` is not serializable and making it so cascades through MeetingCenter -> Status/MeetingDate/CollectionMeetingCalendar; not worth it for one read. */
    suspend fun getCenterList(dateFormat: String?, locale: String?, meetingDate: String?, officeId: Int, staffId: Int): List<OfflineCenter>
}
