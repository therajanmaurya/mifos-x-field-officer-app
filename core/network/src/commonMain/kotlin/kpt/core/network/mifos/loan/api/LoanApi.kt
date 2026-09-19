/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.network.mifos.loan.api

import kpt.core.base.network.annotation.ApiBinding

import com.mifos.core.model.objects.account.loan.LoanApproval
import com.mifos.core.model.objects.account.loan.reschedules.LoanRescheduleApprovalRequest
import com.mifos.core.model.objects.account.loan.reschedules.LoanRescheduleRejectionRequest
import com.mifos.core.model.objects.account.loan.reschedules.LoanRescheduleRequest
import com.mifos.core.model.objects.account.loan.reschedules.LoanRescheduleResponse
import com.mifos.core.model.objects.account.loan.reschedules.LoanRescheduleTemplate
import com.mifos.core.model.objects.account.loan.transfer.AccountTransferRequest
import com.mifos.core.model.objects.account.loan.transfer.AccountTransferTemplate
import com.mifos.core.model.objects.clients.Page
import com.mifos.core.model.objects.organisations.LoanProducts
import com.mifos.core.model.objects.payloads.GroupLoanPayload
import com.mifos.core.model.objects.template.loan.GroupLoanTemplate
import kpt.core.model.shared.GenericResponse
import kpt.core.network.mifos.loan.dto.LoanWithAssociationsDto
import kpt.core.network.mifos.loan.dto.CreateGuarantorResponseDto
import kpt.core.network.mifos.loan.dto.GuarantorRequestDto
import kpt.core.network.mifos.loan.dto.LoanChargeOffRequestDto
import kpt.core.network.mifos.loan.dto.LoanChargeOffResponseDto
import kpt.core.network.mifos.loan.dto.RejectLoanRequestDto
import kpt.core.network.mifos.loan.dto.RejectLoanResponseDto
import kpt.core.network.mifos.loan.dto.AssignLoanOfficerRequestDto
import kpt.core.network.mifos.loan.dto.AssignLoanOfficerResponseDto
import kpt.core.network.mifos.loan.dto.LoanDisburseRequestDto
import kpt.core.network.mifos.loan.dto.LoanDisburseResponseDto
import kpt.core.network.mifos.loan.dto.GuarantorAccountTemplateDto
import kpt.core.network.mifos.loan.dto.GuarantorTemplateDto
import kpt.core.network.mifos.loan.dto.LoanChargeOffTemplateDto
import kpt.core.network.mifos.loan.dto.LoanDisburseTemplateDto
import kpt.core.network.mifos.loan.dto.LoanOfficerOptionsTemplateDto
import kpt.core.network.mifos.loan.dto.LoansPayload
import kpt.core.database.basemodel.APIEndPoint
import kpt.core.database.loan.entity.Loan
import kpt.core.database.loan.entity.LoanRepaymentRequestEntity
import kpt.core.database.loan.entity.LoanRepaymentResponseEntity
import kpt.core.database.charge.entity.ChargesEntity
import kpt.core.database.loan.entity.LoanRepaymentTemplateEntity
import kpt.core.database.loan.entity.LoanTemplate
import kpt.core.database.loan.entity.LoanTransactionTemplate
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.statement.HttpResponse

/**
 * @author fomenkoo
 */
@ApiBinding("mifos")
interface LoanApi {
    @GET(APIEndPoint.LOANS + "/{loanId}?associations=all&exclude=guarantors,futureSchedule")
    suspend fun getLoanByIdWithAllAssociations(@Path("loanId") loanId: Int): LoanWithAssociationsDto

    @GET(APIEndPoint.LOANS + "/{loanId}/transactions/template?command=repayment")
    suspend fun getLoanRepaymentTemplate(@Path("loanId") loanId: Int): LoanRepaymentTemplateEntity

    //  Mandatory Fields
    //  1. String approvedOnDate
    @POST(APIEndPoint.LOANS + "/{loanId}?command=approve")
    suspend fun approveLoanApplication(
        @Path("loanId") loanId: Int,
        @Body loanApproval: LoanApproval?,
    ): GenericResponse

    @POST(APIEndPoint.LOANS + "/{loanId}/transactions?command=repayment")
    suspend fun submitPayment(
        @Path("loanId") loanId: Int,
        @Body loanRepaymentRequest: LoanRepaymentRequestEntity?,
    ): LoanRepaymentResponseEntity

    @GET(APIEndPoint.LOANS + "/{loanId}/transactions/template?command=charge-off")
    suspend fun getChargeOffTemplate(
        @Path("loanId") loanId: Int,
    ): LoanChargeOffTemplateDto

    @POST(APIEndPoint.LOANS + "/{loanId}/transactions?command=charge-off")
    suspend fun chargeOff(
        @Path("loanId") loanId: Int,
        @Body loanChargeOffRequest: LoanChargeOffRequestDto,
    ): LoanChargeOffResponseDto

    @POST(APIEndPoint.LOANS + "/{loanId}?command=reject")
    suspend fun rejectLoan(
        @Path("loanId") loanId: Int,
        @Body request: RejectLoanRequestDto,
    ): RejectLoanResponseDto

    @GET(APIEndPoint.LOANS + "/{loanId}?associations=repaymentSchedule")
    suspend fun getLoanRepaymentSchedule(@Path("loanId") loanId: Int): LoanWithAssociationsDto

    @GET(APIEndPoint.LOANS + "/{loanId}?associations=transactions")
    suspend fun getLoanWithTransactions(@Path("loanId") loanId: Int): LoanWithAssociationsDto

    @GET(APIEndPoint.LOANS + "/{loanId}/guarantors/template")
    suspend fun getGuarantorTemplate(@Path("loanId") loanId: Int): GuarantorTemplateDto

    @POST(APIEndPoint.LOANS + "/{loanId}/guarantors")
    suspend fun createGuarantor(
        @Path("loanId") loanId: Int,
        @Body request: GuarantorRequestDto,
    ): CreateGuarantorResponseDto

    @GET(APIEndPoint.LOANS + "/{loanId}/guarantors/accounts/template")
    suspend fun getGuarantorAccountTemplate(
        @Path("loanId") loanId: Int,
        @Query("clientId") clientId: Int,
    ): GuarantorAccountTemplateDto

    @GET(APIEndPoint.CREATE_LOANS_PRODUCTS)
    suspend fun getAllLoans(): List<LoanProducts>

    @POST(APIEndPoint.CREATE_LOANS_ACCOUNTS)
    suspend fun createLoansAccount(@Body loansPayload: LoansPayload?): HttpResponse

    /**
     * Calculate loan repayment schedule without creating the loan.
     * Used to preview the schedule before submitting the loan application.
     */
    @POST(APIEndPoint.CREATE_LOANS_ACCOUNTS + "?command=calculateLoanSchedule")
    suspend fun calculateLoanSchedule(@Body loansPayload: LoansPayload?): HttpResponse

    @GET(APIEndPoint.CREATE_LOANS_ACCOUNTS + "/template?templateType=individual")
    suspend fun getLoansAccountTemplate(
        @Query("clientId") clientId: Int,
        @Query("productId") productId: Int,
    ): LoanTemplate

    /**
     * For fetching any type of loan template.
     * Example:
     * 1. repayment
     * 2. disburse
     * 3. waiver
     * 4. refundbycash
     * 5. foreclosure
     *
     * @param loanId Loan Id
     * @param command Template Type
     * @return
     */
    @GET(APIEndPoint.LOANS + "/{loanId}/transactions/template")
    suspend fun getLoanTransactionTemplate(
        @Path("loanId") loanId: Int,
        @Query("command") command: String?,
    ): LoanTransactionTemplate

    @POST(APIEndPoint.CREATE_LOANS_ACCOUNTS)
    suspend fun createGroupLoansAccount(@Body loansPayload: GroupLoanPayload?): Loan

    @GET(APIEndPoint.CREATE_LOANS_ACCOUNTS + "/template?templateType=group")
    suspend fun getGroupLoansAccountTemplate(
        @Query("groupId") groupId: Int,
        @Query("productId") productId: Int,
    ): GroupLoanTemplate

    @GET(APIEndPoint.LOANS + "/{loanId}/" + APIEndPoint.CHARGES)
    suspend fun getListOfLoanCharges(@Path("loanId") loanId: Int): List<ChargesEntity>

    @GET(APIEndPoint.CLIENTS + "/{clientId}/" + APIEndPoint.CHARGES)
    suspend fun getListOfCharges(@Path("clientId") clientId: Int): Page<ChargesEntity>

    /**
     * Account Transfer API Endpoints
     */

    /**
     * Retrieve account transfer template for populating UI dropdowns
     *
     * @param fromOfficeId Source office ID
     * @param fromClientId Source client ID
     * @param fromAccountType Source account type ID
     * @param fromAccountId Source account ID
     * @return AccountTransferTemplate with available options
     */
    @GET(APIEndPoint.ACCOUNT_TRANSFERS + "/template")
    suspend fun getAccountTransferTemplate(
        @Query("fromClientId") fromClientId: Int,
        @Query("fromAccountType") fromAccountType: Int,
        @Query("fromAccountId") fromAccountId: Int,
        @Query("fromOfficeId") fromOfficeId: Int? = null,
        @Query("toOfficeId") toOfficeId: Int? = null,
        @Query("toClientId") toClientId: Int? = null,
        @Query("toAccountType") toAccountType: Int? = null,
        @Query("toAccountId") toAccountId: Int? = null,
    ): AccountTransferTemplate

    /**
     * Submit an account transfer
     *
     * @param request Account transfer request payload
     * @return HttpResponse to check status and handle error/success appropriately
     */
    @POST(APIEndPoint.ACCOUNT_TRANSFERS)
    suspend fun submitAccountTransfer(
        @Body request: AccountTransferRequest,
    ): HttpResponse

    /**
     * Loan Reschedule API Endpoints
     */

    @GET(APIEndPoint.RESCHEDULE_LOANS)
    suspend fun getLoanReschedules(
        @Query("loanId") loanId: Int,
    ): List<LoanRescheduleResponse>

    @GET(APIEndPoint.RESCHEDULE_LOANS + "/template")
    suspend fun getLoanRescheduleTemplate(): LoanRescheduleTemplate

    @POST(APIEndPoint.RESCHEDULE_LOANS)
    suspend fun submitLoanReschedule(
        @Body request: LoanRescheduleRequest,
    ): HttpResponse

    @POST(APIEndPoint.RESCHEDULE_LOANS + "/{scheduleId}?command=approve")
    suspend fun approveLoanReschedule(
        @Path("scheduleId") scheduleId: Int,
        @Body request: LoanRescheduleApprovalRequest,
    ): HttpResponse

    @POST(APIEndPoint.RESCHEDULE_LOANS + "/{scheduleId}?command=reject")
    suspend fun rejectLoanReschedule(
        @Path("scheduleId") scheduleId: Int,
        @Body request: LoanRescheduleRejectionRequest,
    ): HttpResponse

    @GET(APIEndPoint.LOANS + "/{loanId}")
    suspend fun getLoanOfficerTemplate(
        @Path("loanId") loanId: Int,
        @Query("fields") fields: String = "id,loanOfficerId,loanOfficerOptions",
        @Query("staffInSelectedOfficeOnly") staffInSelectedOfficeOnly: Boolean = true,
        @Query("template") template: Boolean = true,
    ): LoanOfficerOptionsTemplateDto

    @POST(APIEndPoint.LOANS + "/{loanId}")
    suspend fun assignLoanOfficer(
        @Path("loanId") loanId: Int,
        @Query("command") command: String = "assignLoanOfficer",
        @Body request: AssignLoanOfficerRequestDto,
    ): AssignLoanOfficerResponseDto

    @GET(APIEndPoint.LOANS + "/{loanId}/transactions/template?command=disburse")
    suspend fun getDisburseTemplate(
        @Path("loanId") loanId: Int,
    ): LoanDisburseTemplateDto

    @POST(APIEndPoint.LOANS + "/{loanId}?command=disburse")
    suspend fun disburse(
        @Path("loanId") loanId: Int,
        @Body loanDisburseRequest: LoanDisburseRequestDto,
    ): LoanDisburseResponseDto
}
