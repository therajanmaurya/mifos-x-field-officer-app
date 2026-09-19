/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.model.objects.responses

import kpt.core.model.objects.Changes
import kpt.core.model.utils.IgnoredOnParcel
import kpt.core.model.utils.Parcelable
import kpt.core.model.utils.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
@Parcelize
class SaveResponse(
    var groupId: Int? = null,

    var resourceId: Int? = null,

    var officeId: Int? = null,

    @IgnoredOnParcel
    var changes: Changes? = null,
) : Parcelable {
    override fun toString(): String {
        return Json.encodeToString(serializer(), this)
    }
}
