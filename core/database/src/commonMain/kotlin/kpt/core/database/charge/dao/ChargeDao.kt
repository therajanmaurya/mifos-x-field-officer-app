/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.database.charge.dao

import kpt.core.base.database.annotation.DbDao

import kpt.core.database.charge.entity.ChargesEntity
import kotlinx.coroutines.flow.Flow
import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query

/**
 * Created by Pronay Sarker on 14/02/2025 (3:32 PM)
 */
@DbDao
@Dao
interface ChargeDao {

    @Query("SELECT * FROM Charges where clientId = :clientId")
    fun getClientCharges(clientId: Int): Flow<List<ChargesEntity>>

    @Insert(entity = ChargesEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCharges(charges: List<ChargesEntity>)
}
