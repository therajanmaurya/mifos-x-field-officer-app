/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package com.mifos.core.network.mappers.note

import com.mifos.core.network.dto.note.NoteDto
import kpt.core.database.note.entity.NoteEntity

/**
 * Wire → Room. `NoteDto.note` is the body text; `NoteEntity` names the same column
 * `noteContent`, which is the one field whose name differs across the boundary.
 * `noteType` is dropped: it is Fineract classification metadata the app never reads.
 */
fun NoteDto.toEntity(): NoteEntity = NoteEntity(
    id = id,
    clientId = clientId,
    noteContent = note,
    createdById = createdById,
    createdByUsername = createdByUsername,
    createdOn = createdOn,
    updatedById = updatedById,
    updatedByUsername = updatedByUsername,
    updatedOn = updatedOn,
)
