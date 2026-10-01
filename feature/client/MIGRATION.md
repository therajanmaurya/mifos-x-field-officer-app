# feature/client — migration ledger

> Written at S3 per `FEATURE_LAYER_MIGRATION_SPEC.md` §7. Original reference: `6b66e8a43`.
> Retired `core/domain` reference: `897ffdac1`.

**Scope.** The largest slice: 163 `.kt` in `feature/client` plus the shared drill-downs it is the
first consumer of — `note` (8), `document` (8), `data-table` (20), `search-record` (4), `activate`
(5). **208 files, ~36% of the whole migration.**

**Status:** inventory complete. Port in progress, subdivided `3a` list → `3b` detail+charges →
`3c` actions → `3d` shared drill-downs.

---

## 1. Original inventory

40 sub-packages under `6b66e8a43:feature/client/src/commonMain/kotlin/com/mifos/feature/client/`.
By size: `newFixedDepositAccount` 9 · `utils` 7 · `createShareAccount` 7 · `clientProfile` 5 ·
`clientDetailsProfile` 5 · `clientAddress` 5 · `clientGeneral` 4 · `charges` 4 · then 32 packages of
1–3 files each.

### Do NOT port — dead in the original

| package | files | evidence |
|---|---:|---|
| `clientList` (singular) | 3 — `ClientListScreen`, `ClientListUiState`, `ClientListViewModel` (76 lines) | **Zero references** anywhere at `6b66e8a43`. The live list is `clientsList` (plural, 349-line VM), which is what `ClientNavigation`, `ClientModule` and `SyncClientsDialogScreen` import. A near-identical name one character apart. |

Porting it would manufacture the `G-IMPL-DUP` defect by faithfully copying a defect that already
existed. **160 live `.kt`, not 163.**

### Feature-local repository — delete, do not port

`DocumentSelectAndUploadRepository` + `…Impl` live inside `feature/client`. A repository inside a
feature module is the same defect as `feature/settings`'s `SyncSurveysDialogRepository` (S0b closed
spec §11 on exactly this). Document upload belongs in `core/data` alongside
`DocumentCommandRepository`, which already exists.

## 2. Use-case classification

265 `DataState` references and 80 `UseCase` references — the densest in the app. Per spec §2 every
one resolves to a direct `core/data` call; **no use-case is revived.** The 26 `core.domain` imports
are all `DROP-to-repository`: `ActivateClientUseCase`, `CreateClientIdentifierUseCase`,
`DeleteIdentifierUseCase`, `AddClientPinpointLocationUseCase`, `DeleteClientAddressPinpointUseCase`,
`CreateSignatureUseCase`, `DownloadDocumentUseCase` and siblings are each a single repository call.

The one validator consumer — `clientAddress/addAddress/AddAddressScreen` used `ValidationResult` —
now reads `kpt.core.domain.validation` (spec §2).

## 3. Substrate mapping

| concern | original | new |
|---|---|---|
| client list | `ClientListRepository` + manual DB/API branch | `ClientRepository.clientsStream(scope)` |
| client detail | `ClientDetailsRepository` | `ClientRepository` + `ClientLiveReadRepository` **(deleted in S0b)** → the entity-returning `getClient` store |
| writes | 26 use-cases | `ClientCommandRepository` (already wired, `MutationGateway`) |
| charges | `charges/**` | `ClientChargeRepository` + `ChargeCommandRepository` |
| notes · documents · data-tables | feature-local + use-cases | `NoteRepository` · `DocumentRepository`/`DocumentCommandRepository` · `DataTableRepository` |
| identifiers, pinpoint, signature, collateral, survey | use-cases | `ClientCommandRepository`, `SurveyRepository` |

### The read-path defect inside the feature

`ClientListViewModel.loadClients()` reads
`userPreferencesRepository.userInfo.first().userStatus` and branches:

```kotlin
if (userStatus) processClientsFromDb() else processClientsFromApi()
```

Two code paths for one read, chosen by a persisted flag — S5-2 inside the feature rather than in
`core/data`. `clientsStream()` already serves cache-first with a background refresh, so **both
branches collapse into one** and the `userStatus` read disappears from the screen entirely. The
`DataState` → `ScreenState` change is not cosmetic here: it is what removes the branch.

## 4. Write policy

| mutation | policy | why |
|---|---|---|
| create / edit client, address, identifier, collateral | **`Optimistic`** | a field officer registers clients with no signal; queueing is the entire point of the app |
| activate client | **`Optimistic`** | same — activation is a field action |
| upload document / signature / image | **`OnlineRequired`** | binary payloads are not queueable through the outbox as it stands; queueing a multi-MB body with no bounded retry is a different feature. Recorded as a deviation, not an oversight. |
| client transfer, closure | **`OnlineRequired`** | both are irreversible server-side state changes a stale local view must not initiate |
| survey submit | **`Optimistic`** | user-authored content, same as `feature/settings` survey sync |

## 5. New-state decisions

| state | decision |
|---|---|
| `Empty` | "No clients in this office yet" + Create-client CTA. The original rendered an empty list indistinguishable from a failed load. |
| `NoNetwork` | cached clients still render with the stale-data banner; actions requiring connectivity disable rather than fail on tap |
| `Conflicted` | routes to the shell's `ConflictInboxViewModel` — a surface the original had no equivalent for |
| `Failed(rolledBack)` | Snackbar naming what was rolled back; the form keeps its input |
| image load | a client with no photo is `Empty`, not `Error` — the original's `fetchClientImage` treated both as failure |

## 6. Nav registration

`ClientNavigation.kt` registers ~38 destinations through one `clientNavGraph`. Under the template it
becomes `@FeatureDestination` on the graph builder; nested drill-downs stay nested and are NOT
annotated (spec §4 — only top-level entries are).

Client becomes a bottom-nav tab, so `TabRegistry.tabs` gains it **by hand** in slice order
Home · Profile · Client · Centers · Groups — `@FeatureTab` collects alphabetically and tab order is a
layout decision.

Build edge: `settings.local.gradle.kts` include **plus** the root `feature-deps.gradle.kts` entry —
three steps, not two (spec §4).

## 7. Platform capabilities (§3a)

`feature/client` carries 25 of the 40 misplaced platform files — the largest concentration:

| capability | files | disposition |
|---|---:|---|
| `PlatformCameraLauncher` | 5 | → `core/platform`. Note `core/common` already wraps filekit's `openCameraPicker`; check for redundancy before writing a second bridge. |
| `PdfViewer` | 5 | → `core/platform`, unified with `savings`' copy (8 files total, duplicated across two features) |
| `OpenFileInDefaultExternalApp` | 5 | **delete** — `core-base/platform` already ships `IntentManager` + `UrlLauncher` |
| `PhoneNumberUtil` | 5 | → `core/common` if pure, else `core/platform` |
| `PinpointClientScreen.*` | 5 | expect/actual **Composable** → collapse to one commonMain composable over a `core/platform` bridge |

Plus two Android resource files that must travel WITH their capability:

- `androidMain/res/xml/feature_client_file_provider_paths.xml` — FileProvider paths for
  `filekit_cache`/`filekit_files`, manifest-referenced. Separated from `PlatformCameraLauncher` or its
  `<provider>` entry, camera capture fails at runtime while the build stays green.
- `androidMain/res/values/strings.xml` — **Android** resources, not `composeResources`. Must migrate
  to `commonMain/composeResources/values/strings.xml`.

## 8. Deviations

| deviation | reason |
|---|---|
| `clientList` (singular) not ported | dead package, zero references (§1) |
| `DocumentSelectAndUploadRepository` deleted | feature-local repository; `core/data` owns document writes |
| manual DB/API branch removed | the store serves both; keeping it would re-create S5-2 (§3) |
| binary uploads `OnlineRequired` | queueing multi-MB bodies needs bounded retry the outbox does not yet have (§4) |
| result rows in Home search gain their tap here | S2 deferred it so as not to ship a dead clickable; client detail is the first destination that can receive one |

---

## Audit verdict

**Not started; inventory complete and two defects caught before any code was written** — a dead
duplicate package that would have become `G-IMPL-DUP`, and a feature-local repository that would have
re-created the read-path split S0b just removed. The `userStatus` branch is the clearest evidence that
`DataState` → `ScreenState` is a behaviour change, not a rename.
