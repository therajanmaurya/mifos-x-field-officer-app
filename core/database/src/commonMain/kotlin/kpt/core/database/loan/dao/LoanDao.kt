/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.database.loan.dao

import kpt.core.base.database.annotation.DbDao

import kpt.core.database.payment.entity.PaymentTypeOptionEntity
import kpt.core.database.loan.entity.LoanRepaymentRequestEntity
import kpt.core.database.loan.entity.LoanWithAssociationsEntity
import kpt.core.database.loan.entity.LoanRepaymentTemplateEntity
import kotlinx.coroutines.flow.Flow
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import androidx.room3.Transaction
import kotlinx.coroutines.flow.map
import kpt.core.database.loan.entity.ActualDisbursementDateEntity
import kpt.core.database.utils.getCurrentTimeInMillis

@DbDao
@Dao
interface LoanDao {

    @Insert(entity = LoanWithAssociationsEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLoanWithAssociations(loanWithAssociations: LoanWithAssociationsEntity)

    @Query("SELECT * FROM LoanWithAssociations WHERE id = :loanId LIMIT 1")
    fun getLoanById(loanId: Int): Flow<LoanWithAssociationsEntity?>

    @Insert(entity = LoanRepaymentRequestEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoanRepaymentTransaction(request: LoanRepaymentRequestEntity)

    @Insert(entity = PaymentTypeOptionEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentTypeOption(paymentTypeOption: PaymentTypeOptionEntity)

    @Query("SELECT * FROM PaymentTypeOption")
    fun getPaymentTypeOptions(): Flow<List<PaymentTypeOptionEntity>>

    @Query("SELECT * FROM LoanRepaymentRequestEntity WHERE loanId = :loanId LIMIT 1")
    suspend fun getLoanRepaymentRequest(loanId: Int): LoanRepaymentRequestEntity?

    @Update(entity = LoanRepaymentRequestEntity::class, onConflict = OnConflictStrategy.NONE)
    suspend fun updateLoanRepaymentRequest(loanRepaymentRequest: LoanRepaymentRequestEntity)

    @Query("SELECT * FROM LoanRepaymentRequestEntity ORDER BY timeStamp ASC")
    fun readAllLoanRepaymentTransaction(): Flow<List<LoanRepaymentRequestEntity>>

    @Insert(entity = LoanRepaymentTemplateEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoanRepaymentTemplate(template: LoanRepaymentTemplateEntity)

    @Query("SELECT * FROM LoanRepaymentTemplate WHERE loanId = :loanId LIMIT 1")
    suspend fun getLoanRepaymentTemplate(loanId: Int): LoanRepaymentTemplateEntity?

    @Query("DELETE FROM LoanRepaymentTemplate WHERE loanId = :loanId")
    suspend fun deleteLoanRepaymentByLoanId(loanId: Int)

    /**
     * Persist a loan, stamping its id onto the summary and timeline rows and splitting the
     * timeline's `actualDisbursementDate` into columns. Was `LoanDaoHelper.saveLoanById`.
     */
    @Transaction
    suspend fun saveLoanWithAssociationsShaped(loan: LoanWithAssociationsEntity) {
        saveLoanWithAssociations(
            loan.copy(
                summary = loan.summary.copy(loanId = loan.id),
                timeline = loan.timeline.copy(
                    loanId = loan.id,
                    actualDisburseDate = loan.timeline.actualDisbursementDate?.let {
                        ActualDisbursementDateEntity(
                            loanId = loan.id,
                            year = it.getOrNull(0),
                            month = it.getOrNull(1),
                            date = it.getOrNull(2),
                        )
                    },
                ),
            ),
        )
    }

    /**
     * Read a loan back with `actualDisbursementDate` rebuilt — the inverse of
     * [saveLoanWithAssociationsShaped]. Was `LoanDaoHelper.getLoanById`.
     */
    fun observeLoanWithAssociationsShaped(loanId: Int): Flow<LoanWithAssociationsEntity?> =
        getLoanById(loanId).map { loan ->
            loan?.copy(
                timeline = loan.timeline.copy(
                    actualDisbursementDate = listOf(
                        loan.timeline.actualDisburseDate?.year,
                        loan.timeline.actualDisburseDate?.month,
                        loan.timeline.actualDisburseDate?.date,
                    ),
                ),
            )
        }

    /**
     * Queue an offline repayment, stamping the loan and the submission time (seconds, which is what
     * the sync payload expects). Was `LoanDaoHelper.saveLoanRepaymentTransaction`.
     */
    suspend fun saveLoanRepaymentTransaction(loanId: Int, request: LoanRepaymentRequestEntity) {
        insertLoanRepaymentTransaction(
            request.copy(loanId = loanId, timeStamp = getCurrentTimeInMillis() / 1000),
        )
    }

    /**
     * Persist a repayment template with its payment-type options.
     * Was `LoanDaoHelper.saveLoanRepaymentTemplate` — now atomic, so the template cannot land
     * without the options a repayment form needs to render.
     */
    @Transaction
    suspend fun saveLoanRepaymentTemplateFor(
        loanId: Int,
        template: LoanRepaymentTemplateEntity,
    ): LoanRepaymentTemplateEntity {
        val stamped = template.copy(loanId = loanId)
        stamped.paymentTypeOptions?.forEach { insertPaymentTypeOption(it) }
        insertLoanRepaymentTemplate(stamped)
        return stamped
    }

    /**
     * Read a repayment template back with its payment-type options attached — they live in a
     * separate table. Was `LoanDaoHelper.getLoanRepayTemplate`.
     */
    fun observeLoanRepaymentTemplate(loanId: Int): Flow<LoanRepaymentTemplateEntity?> =
        getPaymentTypeOptions().map { options ->
            getLoanRepaymentTemplate(loanId)?.copy(paymentTypeOptions = options.toMutableList())
        }
}
