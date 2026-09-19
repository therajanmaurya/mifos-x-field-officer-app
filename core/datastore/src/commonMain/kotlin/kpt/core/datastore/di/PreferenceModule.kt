/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package kpt.core.datastore.di

import kpt.core.common.network.MifosDispatchers
import kpt.core.datastore.UserPreferencesDataSource
import kpt.core.datastore.UserPreferencesRepository
import kpt.core.datastore.UserPreferencesRepositoryImpl
import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val PreferencesModule = module {
    factory<Settings> { Settings() }

    factory {
        UserPreferencesDataSource(
            settings = get(),
            dispatcher = get(named(MifosDispatchers.IO.name)),
        )
    }

    singleOf(::UserPreferencesRepositoryImpl) bind UserPreferencesRepository::class
}
