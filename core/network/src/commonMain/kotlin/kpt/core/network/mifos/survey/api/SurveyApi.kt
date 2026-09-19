/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.network.mifos.survey.api

import kpt.core.base.network.annotation.ApiBinding

import com.mifos.core.model.objects.surveys.Scorecard
import kpt.core.common.APIEndPoint
import kpt.core.database.survey.entity.SurveyEntity
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

/**
 * @author
 */
@ApiBinding("mifos")
interface SurveyApi {
    @GET(APIEndPoint.SURVEYS)
    suspend fun allSurveys(): List<SurveyEntity>

    @GET(APIEndPoint.SURVEYS + "/{surveyId}")
    suspend fun getSurvey(@Path("surveyId") surveyId: Int): SurveyEntity

    @POST(APIEndPoint.SURVEYS + "/{surveyId}/scorecards")
    suspend fun submitScore(
        @Path("surveyId") surveyId: Int,
        @Body scorecardPayload: Scorecard?,
    ): Scorecard
}
