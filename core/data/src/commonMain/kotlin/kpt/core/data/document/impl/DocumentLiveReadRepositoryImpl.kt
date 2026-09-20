/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.data.document.impl

import io.ktor.client.statement.HttpResponse
import kpt.core.base.data.annotation.RepositoryBinding
import kpt.core.data.document.DocumentLiveReadRepository
import kpt.core.network.mifos.document.api.DocumentApi

@RepositoryBinding(binds = DocumentLiveReadRepository::class)
internal class DocumentLiveReadRepositoryImpl(
    private val documentApi: DocumentApi,
) : DocumentLiveReadRepository {

    override suspend fun downloadDocument(entityType: String, entityId: Int, documentId: Int): HttpResponse =
        documentApi.downloadDocument(entityType = entityType, entityId = entityId, documentId = documentId)
}
