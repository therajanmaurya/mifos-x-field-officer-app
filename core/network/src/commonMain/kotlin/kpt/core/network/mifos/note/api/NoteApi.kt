/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.network.mifos.note.api

import kpt.core.base.network.annotation.ApiBinding

import kpt.core.network.mifos.note.dto.CreateNoteResponseDto
import kpt.core.network.mifos.note.dto.DeleteNoteResponseDto
import kpt.core.network.mifos.note.dto.NoteDto
import kpt.core.network.mifos.note.dto.NoteRequestDto
import kpt.core.network.mifos.note.dto.UpdateNoteResponseDto
import kpt.core.database.basemodel.APIEndPoint
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path

@ApiBinding("mifos")
interface NoteApi {

    /**
     * Add a Resource Note
     * Adds a new note to a supported resource.  Example Requests:  clients/1/notes   groups/1/notes
     * Responses:
     *  - 200: OK
     *
     * @param resourceType resourceType, eg : Client, Loan, Group, Savings Account
     * @param resourceId resourceId, eg : ClientId, LoanId, GroupId, Savings AccountId
     * @param noteRequestDto
     * @return [CreateNoteResponseDto]
     */
    @POST("{resourceType}/{resourceId}/" + APIEndPoint.NOTES)
    suspend fun addNewNote(
        @Path("resourceType") resourceType: String,
        @Path("resourceId") resourceId: Long,
        @Body noteRequestDto: NoteRequestDto,
    ): CreateNoteResponseDto

    /**
     * Delete a Resource Note
     * Deletes a Resource Note
     * Responses:
     *  - 200: OK
     *
     * @param resourceType resourceType, eg : Client, Loan, Group, Savings Account
     * @param resourceId resourceId, eg : ClientId, LoanId, GroupId, Savings AccountId
     * @param noteId noteId
     * @return [DeleteNoteResponseDto]
     */
    @DELETE("{resourceType}/{resourceId}/" + APIEndPoint.NOTES + "/{noteId}")
    suspend fun deleteNote(
        @Path("resourceType") resourceType: String,
        @Path("resourceId") resourceId: Long,
        @Path("noteId") noteId: Long,
    ): DeleteNoteResponseDto

    /**
     * Retrieve a single Note
     * Responses:
     *  - 200: OK
     *
     * @param resourceType resourceType, eg : Client, Loan, Group, Savings Account
     * @param resourceId resourceId, eg : ClientId, LoanId, GroupId, Savings AccountId
     * @param noteId noteId
     * @return [NoteDto]
     */
    @GET("{resourceType}/{resourceId}/" + APIEndPoint.NOTES + "/{noteId}")
    suspend fun retrieveNote(
        @Path("resourceType") resourceType: String,
        @Path("resourceId") resourceId: Long,
        @Path("noteId") noteId: Long,
    ): NoteDto

    /**
     * Retrieve List of notes
     * Note: Notes are returned in descending createOn order.
     * Responses:
     *  - 200: OK
     *
     * @param resourceType resourceType, eg : Client, Loan, Group, Savings Account
     * @param resourceId resourceId, eg : ClientId, LoanId, GroupId, Savings AccountId
     * @return [List<NoteDto>]
     */
    @GET("{resourceType}/{resourceId}/" + APIEndPoint.NOTES)
    suspend fun retrieveListNotes(
        @Path("resourceType") resourceType: String,
        @Path("resourceId") resourceId: Long,
    ): List<NoteDto>

    /**
     * Update a Note
     * Responses:
     *  - 200: OK
     *
     * @param resourceType resourceType, eg : Client, Loan, Group, Savings Account
     * @param resourceId resourceId, eg : ClientId, LoanId, GroupId, Savings AccountId
     * @param noteId noteId
     * @param noteRequestDto
     * @return UpdateNoteResponseDto
     */
    @PUT("{resourceType}/{resourceId}/" + APIEndPoint.NOTES + "/{noteId}")
    suspend fun updateNote(
        @Path("resourceType") resourceType: String,
        @Path("resourceId") resourceId: Long,
        @Path("noteId") noteId: Long,
        @Body noteRequestDto: NoteRequestDto,
    ): UpdateNoteResponseDto
}
