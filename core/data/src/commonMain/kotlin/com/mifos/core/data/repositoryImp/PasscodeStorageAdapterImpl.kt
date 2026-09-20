/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mifos-x-field-officer-app/blob/master/LICENSE.md
 */
package com.mifos.core.data.repositoryImp

import com.russhwolf.settings.Settings
import org.mifos.authenticator.passcode.PasscodeStorageAdapter

const val MIFOS_PASSCODE = "com.mifos.passcode"

/**
 * [PasscodeStorageAdapter] impl backed by [Settings] (multiplatform key-value).
 *
 * The passcode is stored verbatim under [MIFOS_PASSCODE]. [loadPasscode] returns
 * `null` for both the "key absent" and "value blank" cases so the manager's
 * consumer logic can treat them uniformly as "no passcode set."
 */
/**
 * TODO(phase-04): fold into `ProjectPreferencesRepository` — the template already stores a passcode
 *  on the user record via `setPasscode`/`passcode`, which lives in SECURE settings. This adapter
 *  writes its own key into whichever `Settings` is injected, so the app currently has two passcode
 *  homes and this one is not guaranteed to be the secure store.
 *
 * The unused `UserPreferencesRepository` constructor parameter was dropped with the fork's legacy
 * datastore: it was injected and never read.
 */
class PasscodeStorageAdapterImpl(
    private val settings: Settings,
) : PasscodeStorageAdapter {

    override fun savePasscode(passcode: String) {
        settings.putString(MIFOS_PASSCODE, passcode)
    }

    override fun loadPasscode(): String? {
        val passcode = settings.getString(MIFOS_PASSCODE, "")
        if (passcode.isBlank()) return null
        return passcode
    }

    override fun deletePasscode() {
        settings.remove(MIFOS_PASSCODE)
    }
}
