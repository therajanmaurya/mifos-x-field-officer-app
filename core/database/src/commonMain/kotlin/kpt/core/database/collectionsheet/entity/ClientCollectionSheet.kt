/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.database.collectionsheet.entity

import kpt.core.model.objects.collectionsheets.AttendanceTypeOption
import kpt.core.model.objects.collectionsheets.LoanCollectionSheet
import kpt.core.model.utils.IgnoredOnParcel
import kpt.core.model.utils.Parcelable
import kpt.core.model.utils.Parcelize
import kotlinx.serialization.Serializable

/**
 * Created by Tarun on 06-07-2017.
 */
@Parcelize
@Serializable
data class ClientCollectionSheet(
    var clientId: Int = 0,

    var clientName: String? = null,

    @IgnoredOnParcel
    var loans: ArrayList<LoanCollectionSheet>? = null,

    @IgnoredOnParcel
    var attendanceType: AttendanceTypeOption? = null,

    var savings: ArrayList<SavingsCollectionSheet> = ArrayList(),
) : Parcelable
