/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.network.di

import com.mifos.core.network.apis.CentersApi
import com.mifos.core.network.apis.ClientApi
import com.mifos.core.network.apis.ClientIdentifierApi
import com.mifos.core.network.apis.DataTablesApi
import com.mifos.core.network.apis.GroupsApi
import com.mifos.core.network.apis.OfficesApi
import com.mifos.core.network.apis.StaffApi
import com.mifos.core.network.apis.createCentersApi
import com.mifos.core.network.apis.createClientApi
import com.mifos.core.network.apis.createClientIdentifierApi
import com.mifos.core.network.apis.createDataTablesApi
import com.mifos.core.network.apis.createGroupsApi
import com.mifos.core.network.apis.createOfficesApi
import com.mifos.core.network.apis.createStaffApi
import com.mifos.core.network.services.CenterService
import com.mifos.core.network.services.ChargeService
import com.mifos.core.network.services.CheckerInboxService
import com.mifos.core.network.services.ClientAccountsService
import com.mifos.core.network.services.ClientService
import com.mifos.core.network.services.CollectionSheetService
import com.mifos.core.network.services.DataTableService
import com.mifos.core.network.services.DocumentService
import com.mifos.core.network.services.FixedDepositService
import com.mifos.core.network.services.GroupService
import com.mifos.core.network.services.LoanService
import com.mifos.core.network.services.NoteService
import com.mifos.core.network.services.OfficeService
import com.mifos.core.network.services.RecurringAccountService
import com.mifos.core.network.services.RunReportsService
import com.mifos.core.network.services.SavingsAccountService
import com.mifos.core.network.services.SearchService
import com.mifos.core.network.services.ShareAccountService
import com.mifos.core.network.services.StaffService
import com.mifos.core.network.services.SurveyService
import com.mifos.core.network.services.createCenterService
import com.mifos.core.network.services.createChargeService
import com.mifos.core.network.services.createCheckerInboxService
import com.mifos.core.network.services.createClientAccountsService
import com.mifos.core.network.services.createClientService
import com.mifos.core.network.services.createCollectionSheetService
import com.mifos.core.network.services.createDataTableService
import com.mifos.core.network.services.createDocumentService
import com.mifos.core.network.services.createFixedDepositService
import com.mifos.core.network.services.createGroupService
import com.mifos.core.network.services.createLoanService
import com.mifos.core.network.services.createNoteService
import com.mifos.core.network.services.createOfficeService
import com.mifos.core.network.services.createRecurringAccountService
import com.mifos.core.network.services.createRunReportsService
import com.mifos.core.network.services.createSavingsAccountService
import com.mifos.core.network.services.createSearchService
import com.mifos.core.network.services.createShareAccountService
import com.mifos.core.network.services.createStaffService
import com.mifos.core.network.services.createSurveyService
import kpt.core.base.network.restApi
import org.koin.dsl.module

/**
 * Fineract API bindings — the fork seam for non-derivable network singles.
 *
 * ## Why these are bound here and not by `@ApiBinding`
 * `@ApiBinding` derives ONE binding per access point, which fits a showcase endpoint that owns a
 * single API type (coingecko, frankfurter, fred). Fineract is a MONOLITH: one server, one access
 * point, and 27 resource-oriented API types over it — so `network-ksp` rejects the set with
 * "access point 'main' is bound by ...". Its stated hazard ("the second silently shadows the
 * first everywhere it is injected by qualifier") does not arise here: `restApi<T>` binds
 * `single<T>`, keyed by TYPE, so 27 distinct interfaces are 27 distinct singles.
 *
 * `ProjectNetworkModule` is the template's declared seam for exactly this — "a fork adds its own
 * non-derivable network singles here" — and it is `owner: fork`, so a template sync never
 * contends with it. Each binding still goes through the template's own `restApi(...)` DSL, so the
 * transport, headers and auth all come from the `main` access point declared in app-profile.
 *
 * Adding a Fineract API: declare the Ktorfit interface, then add one `restApi<T>` line below.
 */
private const val FINERACT = "main"

val ProjectNetworkModule = module {
    restApi<CenterService>(FINERACT) { it.createCenterService() }
    restApi<CentersApi>(FINERACT) { it.createCentersApi() }
    restApi<ChargeService>(FINERACT) { it.createChargeService() }
    restApi<CheckerInboxService>(FINERACT) { it.createCheckerInboxService() }
    restApi<ClientAccountsService>(FINERACT) { it.createClientAccountsService() }
    restApi<ClientApi>(FINERACT) { it.createClientApi() }
    restApi<ClientIdentifierApi>(FINERACT) { it.createClientIdentifierApi() }
    restApi<ClientService>(FINERACT) { it.createClientService() }
    restApi<CollectionSheetService>(FINERACT) { it.createCollectionSheetService() }
    restApi<DataTableService>(FINERACT) { it.createDataTableService() }
    restApi<DataTablesApi>(FINERACT) { it.createDataTablesApi() }
    restApi<DocumentService>(FINERACT) { it.createDocumentService() }
    restApi<FixedDepositService>(FINERACT) { it.createFixedDepositService() }
    restApi<GroupService>(FINERACT) { it.createGroupService() }
    restApi<GroupsApi>(FINERACT) { it.createGroupsApi() }
    restApi<LoanService>(FINERACT) { it.createLoanService() }
    restApi<NoteService>(FINERACT) { it.createNoteService() }
    restApi<OfficeService>(FINERACT) { it.createOfficeService() }
    restApi<OfficesApi>(FINERACT) { it.createOfficesApi() }
    restApi<RecurringAccountService>(FINERACT) { it.createRecurringAccountService() }
    restApi<RunReportsService>(FINERACT) { it.createRunReportsService() }
    restApi<SavingsAccountService>(FINERACT) { it.createSavingsAccountService() }
    restApi<SearchService>(FINERACT) { it.createSearchService() }
    restApi<ShareAccountService>(FINERACT) { it.createShareAccountService() }
    restApi<StaffApi>(FINERACT) { it.createStaffApi() }
    restApi<StaffService>(FINERACT) { it.createStaffService() }
    restApi<SurveyService>(FINERACT) { it.createSurveyService() }
}
