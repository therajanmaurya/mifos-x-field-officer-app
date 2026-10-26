/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.network.config

import kpt.core.base.network.AccessPoint
import kpt.core.base.network.AccessPointKind
import kpt.core.base.network.AccessPointRegistry
import kpt.core.base.network.HeaderSpec
import kpt.core.base.network.InMemoryNetworkConfigStore
import kpt.core.base.network.NetworkConfig
import kpt.core.base.network.NetworkConfigOverrides
import kpt.core.base.network.NetworkConfigRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

/**
 * core-base runtime network configs: N declared in app-profile, one main, all editable and switchable
 * at runtime, persisted. Exercised against a registry shaped like a real app-profile — a main REST
 * point, a staging REST point, and a Supabase point.
 */
class NetworkConfigRepositoryTest {

    private val main = AccessPoint(
        id = "mifos", kind = AccessPointKind.REST, baseUrl = "https://apis.mifos.community/", basePath = "1.0/field/v1/",
        loggableHost = "apis.mifos.community", isMain = true,
        headers = listOf(HeaderSpec(name = "Fineract-Platform-TenantId", value = "mifos-bank-1"), HeaderSpec(name = "Authorization", runtimeKey = "mifos.auth")),
    )
    private val staging = AccessPoint(
        id = "staging", kind = AccessPointKind.REST, baseUrl = "https://sandbox.mifos.community/", basePath = "fineract-provider/api/v1/",
        loggableHost = "sandbox.mifos.community", headers = listOf(HeaderSpec(name = "Fineract-Platform-TenantId", value = "default")),
    )
    private val supabase = AccessPoint(id = "project", kind = AccessPointKind.SUPABASE, baseUrl = "https://abc.supabase.co", loggableHost = "abc.supabase.co")
    private val registry = AccessPointRegistry(listOf(main, staging, supabase))

    private fun repo(store: InMemoryNetworkConfigStore = InMemoryNetworkConfigStore()) = NetworkConfigRepository(registry, store)

    @Test
    fun withNoChangesEveryPointUsesWhatAppProfileDeclares() {
        val r = repo()
        assertEquals("mifos", r.mainId)
        assertEquals(main.effectiveUrl, r.urlFor(main))
        assertEquals(staging.effectiveUrl, r.urlFor(staging))
        assertEquals(mapOf("Fineract-Platform-TenantId" to "mifos-bank-1"), r.headersFor(main, mapOf("Fineract-Platform-TenantId" to "mifos-bank-1")))
    }

    @Test
    fun anEditIsAppliedPersistedAndReloadedAsARestartWould() {
        val store = InMemoryNetworkConfigStore()
        repo(store).update("mifos", baseUrl = "https://branch.example.org:8443/fineract-provider/api/v1/", headers = mapOf("Fineract-Platform-TenantId" to "branch-7"))

        val reloaded = repo(store)
        assertEquals("https://branch.example.org:8443/fineract-provider/api/v1/", reloaded.urlFor(main))
        // The declared runtime header (Authorization) is kept; the tenant is replaced.
        assertEquals(
            mapOf("Fineract-Platform-TenantId" to "branch-7", "Authorization" to "Basic x"),
            reloaded.headersFor(main, mapOf("Fineract-Platform-TenantId" to "mifos-bank-1", "Authorization" to "Basic x")),
        )
    }

    @Test
    fun switchingMainRepointsMainBoundClientsOnly() {
        val r = repo()
        r.setMain("staging")
        assertEquals(staging.effectiveUrl, r.urlFor(main), "main-bound clients follow the new main")
        assertEquals(staging.effectiveUrl, r.urlFor(staging), "the staging point's own clients are unchanged")
        assertEquals("default", r.headersFor(main, emptyMap())["Fineract-Platform-TenantId"], "the main's headers come with it")
        r.setMain("mifos")
        assertNull(r.configs.value.mainId, "back to the declared main stores nothing")
    }

    @Test
    fun aConfigAddedAtRuntimeCanBecomeMainAndBeRemoved() {
        val r = repo()
        r.add(NetworkConfig(id = "custom", kind = AccessPointKind.REST, baseUrl = "http://10.0.2.2:8080/fineract-provider/api/v1/"))
        r.setMain("custom")
        assertEquals("http://10.0.2.2:8080/fineract-provider/api/v1/", r.urlFor(main))
        assertFailsWith<IllegalArgumentException>("the main cannot be removed") { r.remove("custom") }
        r.setMain("mifos")
        r.remove("custom")
        assertNull(r.configFor("custom"))
        assertFailsWith<IllegalArgumentException>("declared configs are not removed") { r.remove("staging") }
    }

    @Test
    fun mainSwitchesOnlyBetweenConfigsOfTheSameKind() {
        assertFailsWith<IllegalArgumentException> { repo().setMain("project") }
    }

    @Test
    fun aDisabledConfigFailsItsRequestsAndCannotBeMain() {
        val r = repo()
        r.disable("staging")
        assertFailsWith<IllegalStateException> { r.urlFor(staging) }
        assertFailsWith<IllegalArgumentException> { r.setMain("staging") }
        assertFailsWith<IllegalArgumentException>("the main cannot be disabled") { r.disable("mifos") }
        r.enable("staging")
        assertEquals(staging.effectiveUrl, r.urlFor(staging))
    }

    @Test
    fun resetReturnsToAppProfileAndAStaleMainFallsBack() {
        val store = InMemoryNetworkConfigStore()
        val r = repo(store)
        r.update("mifos", baseUrl = "https://x.example/")
        r.reset("mifos")
        assertEquals(main.effectiveUrl, r.urlFor(main))
        assertNull(store.load().configs["mifos"], "an unchanged config is not persisted")

        // A persisted main that app-profile no longer declares.
        val stale = repo(InMemoryNetworkConfigStore(NetworkConfigOverrides(mainId = "gone")))
        assertEquals("mifos", stale.mainId)
    }

    @Test
    fun appProfileMarkingTwoMainsIsRefused() {
        assertFailsWith<IllegalArgumentException> {
            NetworkConfigRepository(AccessPointRegistry(listOf(main, staging.copy(isMain = true))))
        }
    }
}
