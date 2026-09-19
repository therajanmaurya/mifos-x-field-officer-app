/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.database.di

import kpt.core.common.network.MifosDispatchers
import kpt.core.database.center.helper.CenterDaoHelper
import kpt.core.database.charge.helper.ChargeDaoHelper
import kpt.core.database.client.helper.ClientDaoHelper
import kpt.core.database.group.helper.GroupsDaoHelper
import kpt.core.database.loan.helper.LoanDaoHelper
import kpt.core.database.office.helper.OfficeDaoHelper
import kpt.core.database.savings.helper.SavingsDaoHelper
import kpt.core.database.staff.helper.StaffDaoHelper
import kpt.core.database.survey.helper.SurveyDaoHelper
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val ioDispatcher = named(MifosDispatchers.IO.name)

val HelperModule = module {
    single { CenterDaoHelper(get(), get(ioDispatcher)) }
    single { ChargeDaoHelper(get(), get(ioDispatcher)) }
    single { ClientDaoHelper(get(), get(ioDispatcher)) }
    single { GroupsDaoHelper(get(), get(ioDispatcher)) }
    single { LoanDaoHelper(get()) }
    single { OfficeDaoHelper(get()) }
    single { SavingsDaoHelper(get(), get(ioDispatcher)) }
    single { StaffDaoHelper(get()) }
    single { SurveyDaoHelper(get()) }
}
