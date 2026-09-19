/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.database.center.entity

import kpt.core.database.shared.entity.Timeline

import com.mifos.core.model.objects.collectionsheets.CollectionMeetingCalendar
import com.mifos.core.model.utils.IgnoredOnParcel
import com.mifos.core.model.utils.Parcelable
import com.mifos.core.model.utils.Parcelize
import kotlinx.serialization.Serializable
import kpt.core.database.client.entity.ClientStatusEntity
import kpt.core.database.group.entity.GroupEntity


/**
 * Created by ishankhanna on 28/06/14.
 */
@Parcelize
@Serializable
data class CenterWithAssociations(
    var id: Int? = null,

    var accountNo: String? = null,

    var name: String? = null,

    var externalId: String? = null,

    var officeId: Int? = null,

    var officeName: String? = null,

    var staffId: Int? = null,

    var staffName: String? = null,

    var hierarchy: String? = null,

    var status: ClientStatusEntity? = null,

    var active: Boolean? = null,

    var activationDate: List<Int> = ArrayList(),

    var timeline: Timeline? = null,

    var groupMembers: List<GroupEntity> = ArrayList(),

    @IgnoredOnParcel
    var collectionMeetingCalendar: CollectionMeetingCalendar = CollectionMeetingCalendar(),
) : Parcelable
