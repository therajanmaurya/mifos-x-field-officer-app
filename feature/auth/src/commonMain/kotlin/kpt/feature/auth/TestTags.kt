/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.feature.auth

/** Stable handles for the login screen — asserted by UI tests, never shown to a user. */
object LoginTestTags {
    const val SCREEN = "login:screen"
    const val USERNAME = "login:username"
    const val PASSWORD = "login:password"
    const val SUBMIT = "login:submit"
    const val ERROR = "login:error"
    const val PROGRESS = "login:progress"
}
