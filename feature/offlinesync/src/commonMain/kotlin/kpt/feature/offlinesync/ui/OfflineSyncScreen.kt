/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.feature.offlinesync.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kpt.core.base.designsystem.theme.KptTheme
import kpt.core.base.store.screen.ScreenState
import kpt.core.base.ui.effects.EventsEffect
import kpt.core.designsystem.component.AppScaffold
import kpt.core.designsystem.component.FloatingActionButtonContent
import kpt.core.designsystem.icon.AppIcons
import kpt.core.designsystem.theme.AccentColors
import kpt.core.model.offline.OfflineRecord
import kpt.core.model.offline.RecordKind
import kpt.core.model.offline.RecordSyncStatus
import kpt.core.model.offline.RefreshMode
import kpt.core.model.offline.RefreshSettings
import kpt.core.model.offline.SyncJob
import kpt.feature.offlinesync.generated.resources.Res
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_add_records
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_cancel
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_charging_only
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_collapse
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_count_accounts
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_count_clients
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_count_documents
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_count_groups
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_count_loans
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_count_savings
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_drafts_link
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_drafts_note
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_empty_body
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_empty_title
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_every_hours
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_expand
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_header_subtitle
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_job_downloading
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_job_reads
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_job_records
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_job_result
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_job_result_body
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_last_synced
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_level_centres
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_level_clients
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_level_groups
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_level_label_centres
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_level_label_clients
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_level_label_groups
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_next_in_hours
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_not_synced
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_offline
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_offline_banner
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_on_device
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_online
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_open
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_picker_download
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_picker_empty
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_picker_error
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_picker_hint
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_picker_search
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_picker_title
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_records_title
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_refresh
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_refresh_automatic
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_refresh_automatic_note
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_refresh_data_note
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_refresh_manual
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_refresh_manual_note
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_refresh_title
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_remove
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_remove_confirm
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_resync_label
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_retry
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_retry_failed
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_search_local
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_stat_failed
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_stat_to_sync
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_status_failed
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_status_out_of_date
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_status_synced
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_status_syncing
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_status_waiting
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_sync_now
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_sync_when_online
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_syncing_button
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_tab_centres
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_tab_clients
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_tab_groups
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_time_days
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_time_hours
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_time_just_now
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_time_minutes
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_title
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_via_parent
import kpt.feature.offlinesync.generated.resources.feature_offlinesync_wifi_only
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun OfflineSyncScreen(
    onNavigateBack: () -> Unit,
    onOpenRecord: (RecordKind, Int) -> Unit,
    onOpenDrafts: () -> Unit,
    viewModel: OfflineSyncViewModel = koinViewModel(),
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val pickerRows by viewModel.pickerRows.collectAsStateWithLifecycle()
    EventsEffect(eventFlow = viewModel.eventFlow) { event ->
        when (event) {
            OfflineSyncEvent.NavigateBack -> onNavigateBack()
            OfflineSyncEvent.OpenDrafts -> onOpenDrafts()
            is OfflineSyncEvent.OpenRecord -> onOpenRecord(event.kind, event.id)
        }
    }
    OfflineSyncScreenContent(state = state, pickerRows = pickerRows, onAction = viewModel::trySendAction)
}

@Composable
internal fun OfflineSyncScreenContent(
    state: OfflineSyncState,
    pickerRows: ScreenState<List<PickerRow>>,
    onAction: (OfflineSyncAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val screen = state.screen
    val showFab = state.online && screen != OfflineScreen.LOADING && screen != OfflineScreen.EMPTY
    AppScaffold(
        title = stringResource(Res.string.feature_offlinesync_title),
        onBackPressed = { onAction(OfflineSyncAction.NavigateBack) },
        modifier = modifier.testTag(OfflineSyncTestTags.SCREEN),
        floatingActionButtonContent = if (showFab) {
            FloatingActionButtonContent(
                onClick = { onAction(OfflineSyncAction.AddRecords) },
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Row(
                    Modifier.padding(horizontal = KptTheme.spacing.md).testTag(OfflineSyncTestTags.ADD_RECORDS),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm),
                ) {
                    Icon(AppIcons.Add, contentDescription = null)
                    Text(stringResource(Res.string.feature_offlinesync_add_records))
                }
            }
        } else {
            null
        },
    ) { padding ->
        if (screen == OfflineScreen.LOADING) {
            LoadingSkeleton(Modifier.padding(padding))
            return@AppScaffold
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
                .padding(horizontal = KptTheme.spacing.md)
                .padding(bottom = KptTheme.spacing.xxl + KptTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.md),
        ) {
            if (!state.online) OfflineBanner()
            SummaryCard(state, onAction)
            when (screen) {
                OfflineScreen.SYNCING -> state.job?.let { ActiveJobCard(it, onAction) }
                OfflineScreen.PARTIAL_FAILURE -> state.job?.let { ResultCard(it, state.online, onAction) }
                else -> Unit
            }
            if (screen == OfflineScreen.EMPTY) {
                EmptyCard(state.online, onAction)
            } else {
                RecordsCard(state, onAction)
            }
            if (state.online) RefreshCard(state.refresh, onAction) else DraftsCard(onAction)
        }
    }
    state.picker?.let { RecordPickerSheet(it, pickerRows, state, onAction) }
    state.confirmRemove?.let { RemoveDialog(it, onAction) }
}

// ───────────────────────────── header ─────────────────────────────

@Composable
private fun SummaryCard(state: OfflineSyncState, onAction: (OfflineSyncAction) -> Unit) {
    OutlinedCard(Modifier.testTag(OfflineSyncTestTags.HEADER)) {
        Column(Modifier.padding(KptTheme.spacing.md), verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm + KptTheme.spacing.xs)) {
            Row(horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm)) {
                if (state.online) {
                    StatusChip(AppIcons.Sync, stringResource(Res.string.feature_offlinesync_online), MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer)
                } else {
                    StatusChip(AppIcons.WifiOff, stringResource(Res.string.feature_offlinesync_offline), MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer)
                }
                val failures = state.job?.failures?.size ?: 0
                when {
                    state.screen == OfflineScreen.PARTIAL_FAILURE && failures > 0 -> StatusChip(
                        AppIcons.Error,
                        stringResource(Res.string.feature_offlinesync_stat_failed, failures),
                        MaterialTheme.colorScheme.errorContainer,
                        MaterialTheme.colorScheme.onErrorContainer,
                    )
                    state.toSync > 0 -> StatusChip(
                        AppIcons.Update,
                        stringResource(Res.string.feature_offlinesync_stat_to_sync, state.toSync),
                        MaterialTheme.colorScheme.tertiaryContainer,
                        MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm)) {
                LevelTile(state.count(RecordKind.CENTRE), Res.string.feature_offlinesync_level_label_centres, AppIcons.Groups, Modifier.weight(1f))
                LevelTile(state.count(RecordKind.GROUP), Res.string.feature_offlinesync_level_label_groups, AppIcons.Group, Modifier.weight(1f))
                LevelTile(state.count(RecordKind.CLIENT), Res.string.feature_offlinesync_level_label_clients, AppIcons.Person, Modifier.weight(1f))
            }
            Text(headerSubtitle(state), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (state.records.orEmpty().isNotEmpty()) {
                Button(
                    onClick = { onAction(OfflineSyncAction.SyncNow) },
                    enabled = state.canSync,
                    modifier = Modifier.fillMaxWidth().heightIn(min = KptTheme.spacing.xl + KptTheme.spacing.md).testTag(OfflineSyncTestTags.SYNC_NOW),
                ) {
                    Icon(AppIcons.Sync, contentDescription = null)
                    Spacer(Modifier.size(KptTheme.spacing.sm))
                    Text(
                        stringResource(
                            when {
                                state.syncing -> Res.string.feature_offlinesync_syncing_button
                                !state.online -> Res.string.feature_offlinesync_sync_when_online
                                else -> Res.string.feature_offlinesync_sync_now
                            },
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun headerSubtitle(state: OfflineSyncState): String {
    val last = state.lastSyncedAtMs?.let { stringResource(Res.string.feature_offlinesync_last_synced, relative(it, state.nowMs)) }
        ?: stringResource(Res.string.feature_offlinesync_not_synced)
    val mode = when (state.refresh.mode) {
        RefreshMode.MANUAL -> stringResource(Res.string.feature_offlinesync_refresh_manual)
        RefreshMode.AUTOMATIC -> state.nextSyncAtMs?.let { next ->
            val hours = ((next - state.nowMs).coerceAtLeast(0) / RefreshSettings.HOUR_MS).toInt().coerceAtLeast(1)
            stringResource(Res.string.feature_offlinesync_next_in_hours, hours)
        } ?: stringResource(Res.string.feature_offlinesync_refresh_automatic)
    }
    return stringResource(Res.string.feature_offlinesync_header_subtitle, last, mode)
}

@Composable
private fun LevelTile(count: Int, label: StringResource, icon: ImageVector, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainerLow, MaterialTheme.shapes.medium)
            .padding(KptTheme.spacing.sm + KptTheme.spacing.xs),
    ) {
        Text(count.toString(), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.xs)) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(KptTheme.spacing.md), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(stringResource(label), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun OfflineBanner() {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.shapes.medium)
            .padding(KptTheme.spacing.sm + KptTheme.spacing.xs)
            .testTag(OfflineSyncTestTags.OFFLINE_BANNER),
        horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm + KptTheme.spacing.xs),
    ) {
        Icon(AppIcons.CloudOff, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiaryContainer)
        Text(stringResource(Res.string.feature_offlinesync_offline_banner), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onTertiaryContainer)
    }
}

// ───────────────────────────── job ─────────────────────────────

@Composable
private fun ActiveJobCard(job: SyncJob, onAction: (OfflineSyncAction) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().border(KptTheme.spacing.xs / 2, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.large)
            .testTag(OfflineSyncTestTags.ACTIVE_JOB),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(Modifier.padding(KptTheme.spacing.md), verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm + KptTheme.spacing.xs)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm + KptTheme.spacing.xs)) {
                IconTile(AppIcons.Sync, MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer)
                Column(Modifier.weight(1f)) {
                    Text(pluralStringResource(Res.plurals.feature_offlinesync_job_downloading, job.total, job.total), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(
                        job.targets.joinToString { it.name },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                OutlinedButton(onClick = { onAction(OfflineSyncAction.CancelJob) }, modifier = Modifier.testTag(OfflineSyncTestTags.CANCEL_JOB)) {
                    Text(stringResource(Res.string.feature_offlinesync_cancel))
                }
            }
            val progressLabel = pluralStringResource(Res.plurals.feature_offlinesync_job_records, job.total, job.done, job.total)
            // A record's tree is discovered as it is walked, so until the first record finishes there is
            // no honest fraction — moving, not stuck at 0%.
            if (job.done == 0) {
                LinearProgressIndicator(Modifier.fillMaxWidth().semantics { contentDescription = progressLabel })
            } else {
                LinearProgressIndicator(
                    progress = { job.done.toFloat() / job.total },
                    modifier = Modifier.fillMaxWidth().semantics { contentDescription = progressLabel },
                )
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(progressLabel, style = MaterialTheme.typography.bodySmall)
                Text(pluralStringResource(Res.plurals.feature_offlinesync_job_reads, job.reads, job.reads), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            job.currentLabel?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun ResultCard(job: SyncJob, online: Boolean, onAction: (OfflineSyncAction) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag(OfflineSyncTestTags.JOB_RESULT),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.errorContainer).padding(KptTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm + KptTheme.spacing.xs),
        ) {
            IconTile(AppIcons.Error, MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onErrorContainer)
            Column {
                Text(
                    stringResource(Res.string.feature_offlinesync_job_result, job.reads, job.failures.size),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
                Text(stringResource(Res.string.feature_offlinesync_job_result_body), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onErrorContainer)
            }
        }
        job.failures.take(MAX_FAILURE_ROWS).forEach { failure ->
            Row(
                Modifier.fillMaxWidth().padding(horizontal = KptTheme.spacing.md, vertical = KptTheme.spacing.sm + KptTheme.spacing.xs),
                horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm + KptTheme.spacing.xs),
            ) {
                Icon(AppIcons.Error, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(KptTheme.spacing.md + KptTheme.spacing.xs))
                Column {
                    Text(failure.item, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    failure.reason?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                }
            }
        }
        Row(Modifier.fillMaxWidth().padding(KptTheme.spacing.md), horizontalArrangement = Arrangement.End) {
            Button(
                onClick = { onAction(OfflineSyncAction.RetryFailed) },
                enabled = online,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.testTag(OfflineSyncTestTags.RETRY_FAILED),
            ) {
                Icon(AppIcons.Refresh, contentDescription = null)
                Spacer(Modifier.size(KptTheme.spacing.xs))
                Text(stringResource(Res.string.feature_offlinesync_retry_failed))
            }
        }
    }
}

// ───────────────────────────── records ─────────────────────────────

@Composable
private fun RecordsCard(state: OfflineSyncState, onAction: (OfflineSyncAction) -> Unit) {
    OutlinedCard(Modifier.testTag(OfflineSyncTestTags.RECORDS)) {
        Column(Modifier.padding(top = KptTheme.spacing.md)) {
            Text(
                stringResource(Res.string.feature_offlinesync_records_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = KptTheme.spacing.md),
            )
            Row(
                Modifier.padding(horizontal = KptTheme.spacing.md, vertical = KptTheme.spacing.sm).testTag(OfflineSyncTestTags.RECORDS_LEVEL),
                horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm),
            ) {
                LevelChip(state, RecordKind.CENTRE, Res.string.feature_offlinesync_level_centres, onAction)
                LevelChip(state, RecordKind.GROUP, Res.string.feature_offlinesync_level_groups, onAction)
                LevelChip(state, RecordKind.CLIENT, Res.string.feature_offlinesync_level_clients, onAction)
            }
            if (state.level == RecordKind.CLIENT && state.count(RecordKind.CLIENT) > 0) {
                OutlinedTextField(
                    value = state.search,
                    onValueChange = { onAction(OfflineSyncAction.Search(it)) },
                    placeholder = { Text(pluralStringResource(Res.plurals.feature_offlinesync_search_local, state.count(RecordKind.CLIENT), state.count(RecordKind.CLIENT))) },
                    leadingIcon = { Icon(AppIcons.Search, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = KptTheme.spacing.md).testTag(OfflineSyncTestTags.RECORDS_SEARCH),
                )
            }
            state.levelRows.forEach { record ->
                RecordRow(record, state, indent = false, onAction)
                if ((record.kind to record.id) in state.expanded) {
                    state.childrenOf(record).forEach { child ->
                        RecordRow(child, state, indent = true, onAction)
                        if ((child.kind to child.id) in state.expanded) {
                            state.childrenOf(child).forEach { grandchild -> RecordRow(grandchild, state, indent = true, onAction) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LevelChip(state: OfflineSyncState, kind: RecordKind, label: StringResource, onAction: (OfflineSyncAction) -> Unit) {
    FilterChip(
        selected = state.level == kind,
        colors = selectedChipColors(),
        onClick = { onAction(OfflineSyncAction.Level(kind)) },
        label = { Text(stringResource(label, state.count(kind))) },
        trailingIcon = if (state.hasFailures(kind)) {
            { Box(Modifier.size(KptTheme.spacing.sm).background(MaterialTheme.colorScheme.error, MaterialTheme.shapes.extraSmall)) }
        } else {
            null
        },
    )
}

@Composable
private fun RecordRow(record: OfflineRecord, state: OfflineSyncState, indent: Boolean, onAction: (OfflineSyncAction) -> Unit) {
    val expandable = record.kind != RecordKind.CLIENT && state.childrenOf(record).isNotEmpty()
    val expanded = (record.kind to record.id) in state.expanded
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(if (indent) MaterialTheme.colorScheme.surfaceContainerLowest else Color.Transparent)
            .clickable { onAction(OfflineSyncAction.OpenRecord(record)) }
            .padding(start = if (indent) KptTheme.spacing.xl + KptTheme.spacing.lg else KptTheme.spacing.md, end = KptTheme.spacing.xs)
            .heightIn(min = KptTheme.spacing.xxl)
            .testTag(OfflineSyncTestTags.RECORD_ROW),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm + KptTheme.spacing.xs),
    ) {
        Initials(record, small = indent)
        Column(Modifier.weight(1f).padding(vertical = KptTheme.spacing.sm)) {
            Text(record.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            countsLine(record)?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            StatusLine(record, state)
        }
        if (record.chosen && !state.syncing) {
            if (record.status != RecordSyncStatus.WAITING) {
                IconButton(
                    onClick = { onAction(OfflineSyncAction.RefreshRecord(record)) },
                    enabled = state.online,
                    modifier = Modifier.testTag(OfflineSyncTestTags.RECORD_REFRESH),
                ) {
                    Icon(AppIcons.Refresh, contentDescription = stringResource(Res.string.feature_offlinesync_refresh))
                }
            }
            IconButton(onClick = { onAction(OfflineSyncAction.AskRemove(record)) }, modifier = Modifier.testTag(OfflineSyncTestTags.RECORD_REMOVE)) {
                Icon(AppIcons.Delete, contentDescription = stringResource(Res.string.feature_offlinesync_remove))
            }
        }
        if (expandable) {
            IconButton(onClick = { onAction(OfflineSyncAction.ToggleExpand(record)) }) {
                Icon(
                    if (expanded) AppIcons.ChevronUp else AppIcons.ChevronDown,
                    contentDescription = stringResource(if (expanded) Res.string.feature_offlinesync_collapse else Res.string.feature_offlinesync_expand),
                )
            }
        } else if (!record.chosen) {
            Icon(
                AppIcons.ChevronRight,
                contentDescription = stringResource(Res.string.feature_offlinesync_open),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(KptTheme.spacing.sm + KptTheme.spacing.xs),
            )
        }
    }
}

@Composable
private fun countsLine(record: OfflineRecord): String? {
    val parts = buildList {
        if (record.kind == RecordKind.CENTRE) add(plural(Res.plurals.feature_offlinesync_count_groups, record.groups))
        if (record.kind != RecordKind.CLIENT) add(plural(Res.plurals.feature_offlinesync_count_clients, record.clients))
        if (record.kind == RecordKind.CLIENT) {
            if (record.loans > 0) add(plural(Res.plurals.feature_offlinesync_count_loans, record.loans))
            if (record.savings > 0) add(plural(Res.plurals.feature_offlinesync_count_savings, record.savings))
            if (record.documents > 0) add(plural(Res.plurals.feature_offlinesync_count_documents, record.documents))
        } else if (record.loans + record.savings > 0) {
            add(plural(Res.plurals.feature_offlinesync_count_accounts, record.loans + record.savings))
        }
        if (!record.chosen) record.parentName?.let { add(stringResource(Res.string.feature_offlinesync_via_parent, it)) }
    }
    return parts.takeIf { it.isNotEmpty() }?.joinToString(separator = SEPARATOR)
}

@Composable
private fun plural(resource: PluralStringResource, count: Int) = pluralStringResource(resource, count, count)

@Composable
private fun StatusLine(record: OfflineRecord, state: OfflineSyncState) {
    val synced = record.refreshedAtMs?.let { relative(it, state.nowMs) }.orEmpty()
    val (text, color, icon) = when {
        record.status == RecordSyncStatus.SYNCING ->
            Triple(stringResource(Res.string.feature_offlinesync_status_syncing), MaterialTheme.colorScheme.primary, AppIcons.CloudDownload)
        record.status == RecordSyncStatus.WAITING ->
            Triple(stringResource(Res.string.feature_offlinesync_status_waiting), MaterialTheme.colorScheme.primary, AppIcons.Update)
        record.status == RecordSyncStatus.FAILED ->
            Triple(stringResource(Res.string.feature_offlinesync_status_failed, record.failedCount, synced), MaterialTheme.colorScheme.error, AppIcons.Error)
        record.isOutOfDate(state.nowMs, state.refresh.maxAgeMs) ->
            Triple(stringResource(Res.string.feature_offlinesync_status_out_of_date, synced), MaterialTheme.colorScheme.tertiary, AppIcons.Update)
        else ->
            Triple(stringResource(Res.string.feature_offlinesync_status_synced, synced), MaterialTheme.colorScheme.secondary, AppIcons.RightTick)
    }
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.xs)) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(KptTheme.spacing.md - KptTheme.spacing.xs / 2))
        Text(text, style = MaterialTheme.typography.labelMedium, color = color, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun Initials(record: OfflineRecord, small: Boolean) {
    val (container, content) = when (record.kind) {
        RecordKind.CENTRE -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        RecordKind.GROUP -> AccentColors.purpleContainer to AccentColors.purple
        RecordKind.CLIENT -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
    }
    val size: Dp = if (small) KptTheme.spacing.xl else KptTheme.spacing.xl + KptTheme.spacing.sm
    Box(Modifier.size(size).background(container, MaterialTheme.shapes.medium), contentAlignment = Alignment.Center) {
        Text(
            record.name.split(' ').filter { it.isNotBlank() }.take(2).joinToString("") { it.take(1).uppercase() },
            style = if (small) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = content,
        )
    }
}

// ───────────────────────────── settings ─────────────────────────────

@Composable
private fun RefreshCard(settings: RefreshSettings, onAction: (OfflineSyncAction) -> Unit) {
    OutlinedCard(Modifier.testTag(OfflineSyncTestTags.REFRESH_MODE)) {
        Column(Modifier.padding(KptTheme.spacing.md), verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm + KptTheme.spacing.xs)) {
            Text(stringResource(Res.string.feature_offlinesync_refresh_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth().testTag(OfflineSyncTestTags.REFRESH_MODE_TOGGLE)) {
                RefreshMode.entries.forEachIndexed { index, mode ->
                    SegmentedButton(
                        selected = settings.mode == mode,
                        onClick = { onAction(OfflineSyncAction.SetMode(mode)) },
                        shape = SegmentedButtonDefaults.itemShape(index, RefreshMode.entries.size),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        ),
                    ) {
                        Text(
                            stringResource(
                                if (mode == RefreshMode.MANUAL) Res.string.feature_offlinesync_refresh_manual else Res.string.feature_offlinesync_refresh_automatic,
                            ),
                        )
                    }
                }
            }
            if (settings.mode == RefreshMode.AUTOMATIC) {
                Text(stringResource(Res.string.feature_offlinesync_resync_label), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(Modifier.testTag(OfflineSyncTestTags.REFRESH_INTERVAL), horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm)) {
                    RefreshSettings.INTERVALS.forEach { hours ->
                        FilterChip(
                            selected = settings.intervalHours == hours,
                            colors = selectedChipColors(),
                            onClick = { onAction(OfflineSyncAction.SetInterval(hours)) },
                            label = { Text(stringResource(Res.string.feature_offlinesync_every_hours, hours)) },
                        )
                    }
                }
                SwitchRow(Res.string.feature_offlinesync_wifi_only, settings.wifiOnly, OfflineSyncTestTags.WIFI_ONLY) { onAction(OfflineSyncAction.SetWifiOnly(it)) }
                SwitchRow(Res.string.feature_offlinesync_charging_only, settings.chargingOnly, OfflineSyncTestTags.CHARGING_ONLY) {
                    onAction(OfflineSyncAction.SetChargingOnly(it))
                }
            }
            Note(
                AppIcons.Info,
                if (settings.mode == RefreshMode.MANUAL) Res.string.feature_offlinesync_refresh_manual_note else Res.string.feature_offlinesync_refresh_automatic_note,
            )
            Note(AppIcons.CloudDownload, Res.string.feature_offlinesync_refresh_data_note)
        }
    }
}

@Composable
private fun SwitchRow(label: StringResource, checked: Boolean, tag: String, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = KptTheme.spacing.xl + KptTheme.spacing.md).testTag(tag), verticalAlignment = Alignment.CenterVertically) {
        Text(stringResource(label), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onChange)
    }
}

@Composable
private fun Note(icon: ImageVector, text: StringResource) {
    Row(horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm)) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(KptTheme.spacing.md + KptTheme.spacing.xs / 2))
        Text(stringResource(text), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun DraftsCard(onAction: (OfflineSyncAction) -> Unit) {
    OutlinedCard(Modifier.testTag(OfflineSyncTestTags.DRAFTS_LINK)) {
        Row(
            Modifier.padding(horizontal = KptTheme.spacing.md, vertical = KptTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm + KptTheme.spacing.xs),
        ) {
            Icon(AppIcons.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(
                stringResource(Res.string.feature_offlinesync_drafts_note),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            TextButton(onClick = { onAction(OfflineSyncAction.OpenDrafts) }) { Text(stringResource(Res.string.feature_offlinesync_drafts_link)) }
        }
    }
}

@Composable
private fun EmptyCard(online: Boolean, onAction: (OfflineSyncAction) -> Unit) {
    OutlinedCard(Modifier.testTag(OfflineSyncTestTags.EMPTY)) {
        Column(
            Modifier.fillMaxWidth().padding(KptTheme.spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm + KptTheme.spacing.xs),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm)) {
                IconTile(AppIcons.Groups, MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer)
                IconTile(AppIcons.Group, AccentColors.purpleContainer, AccentColors.purple)
                IconTile(AppIcons.Person, MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer)
            }
            Text(stringResource(Res.string.feature_offlinesync_empty_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text(stringResource(Res.string.feature_offlinesync_empty_body), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Button(onClick = { onAction(OfflineSyncAction.AddRecords) }, enabled = online, modifier = Modifier.testTag(OfflineSyncTestTags.ADD_RECORDS)) {
                Icon(AppIcons.Add, contentDescription = null)
                Spacer(Modifier.size(KptTheme.spacing.sm))
                Text(stringResource(Res.string.feature_offlinesync_add_records))
            }
        }
    }
}

// ───────────────────────────── picker ─────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordPickerSheet(picker: PickerState, rows: ScreenState<List<PickerRow>>, state: OfflineSyncState, onAction: (OfflineSyncAction) -> Unit) {
    ModalBottomSheet(onDismissRequest = { onAction(OfflineSyncAction.ClosePicker) }, modifier = Modifier.testTag(OfflineSyncTestTags.PICKER)) {
        PickerSheetBody(picker, rows, state, onAction)
    }
}

@Composable
internal fun PickerSheetBody(picker: PickerState, rows: ScreenState<List<PickerRow>>, state: OfflineSyncState, onAction: (OfflineSyncAction) -> Unit) {
    Column {
        Column(Modifier.fillMaxWidth().padding(horizontal = KptTheme.spacing.md), verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm)) {
            Text(stringResource(Res.string.feature_offlinesync_picker_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Row(Modifier.testTag(OfflineSyncTestTags.PICKER_TABS), horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm)) {
                PickerTabChip(picker, RecordKind.CENTRE, Res.string.feature_offlinesync_tab_centres, onAction)
                PickerTabChip(picker, RecordKind.GROUP, Res.string.feature_offlinesync_tab_groups, onAction)
                PickerTabChip(picker, RecordKind.CLIENT, Res.string.feature_offlinesync_tab_clients, onAction)
            }
            OutlinedTextField(
                value = picker.query,
                onValueChange = { onAction(OfflineSyncAction.PickerSearch(it)) },
                placeholder = { Text(stringResource(Res.string.feature_offlinesync_picker_search)) },
                leadingIcon = { Icon(AppIcons.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag(OfflineSyncTestTags.PICKER_SEARCH),
            )
            if (picker.tab == RecordKind.CENTRE) {
                Text(stringResource(Res.string.feature_offlinesync_picker_hint), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Box(Modifier.fillMaxWidth().heightIn(min = KptTheme.spacing.xxl * 2, max = KptTheme.spacing.xxl * PICKER_LIST_HEIGHT_UNITS)) {
            when (rows) {
                is ScreenState.Content -> PickerList(rows.data, picker, state, onAction)
                is ScreenState.Error, is ScreenState.NoNetwork -> Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    PickerMessage(Res.string.feature_offlinesync_picker_error)
                    TextButton(onClick = { onAction(OfflineSyncAction.PickerRetry) }) { Text(stringResource(Res.string.feature_offlinesync_retry)) }
                }
                ScreenState.Empty -> PickerMessage(Res.string.feature_offlinesync_picker_empty)
                ScreenState.Loading, ScreenState.Unauthenticated -> LinearProgressIndicator(Modifier.fillMaxWidth().padding(KptTheme.spacing.md))
            }
        }
        Button(
            onClick = { onAction(OfflineSyncAction.PickerDownload) },
            enabled = picker.selected.isNotEmpty() && state.online,
            modifier = Modifier.fillMaxWidth().padding(KptTheme.spacing.md).heightIn(min = KptTheme.spacing.xl + KptTheme.spacing.md + KptTheme.spacing.xs)
                .testTag(OfflineSyncTestTags.PICKER_DOWNLOAD),
        ) {
            Icon(AppIcons.CloudDownload, contentDescription = null)
            Spacer(Modifier.size(KptTheme.spacing.sm))
            Text(stringResource(Res.string.feature_offlinesync_picker_download, picker.selected.size))
        }
    }
}

@Composable
private fun PickerList(all: List<PickerRow>, picker: PickerState, state: OfflineSyncState, onAction: (OfflineSyncAction) -> Unit) {
    val query = picker.query.trim()
    val shown = all.filter { row ->
        query.isEmpty() || row.target.name.contains(query, ignoreCase = true) || row.accountNo?.contains(query, ignoreCase = true) == true
    }
    if (shown.isEmpty()) {
        PickerMessage(Res.string.feature_offlinesync_picker_empty)
        return
    }
    LazyColumn(Modifier.testTag(OfflineSyncTestTags.PICKER_LIST), contentPadding = PaddingValues(vertical = KptTheme.spacing.sm)) {
        items(shown, key = { "${it.target.kind}:${it.target.id}" }) { row ->
            PickerRowItem(row, selected = row.target in picker.selected, onDevice = state.isOnDevice(row.target), onAction = onAction)
        }
    }
}

@Composable
private fun PickerTabChip(picker: PickerState, kind: RecordKind, label: StringResource, onAction: (OfflineSyncAction) -> Unit) {
    FilterChip(selected = picker.tab == kind, colors = selectedChipColors(), onClick = { onAction(OfflineSyncAction.PickerTab(kind)) }, label = { Text(stringResource(label)) })
}

@Composable
private fun PickerRowItem(row: PickerRow, selected: Boolean, onDevice: Boolean, onAction: (OfflineSyncAction) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onAction(OfflineSyncAction.PickerSelect(row.target)) }
            .padding(horizontal = KptTheme.spacing.md, vertical = KptTheme.spacing.xs)
            .testTag(OfflineSyncTestTags.PICKER_ROW),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm),
    ) {
        Checkbox(checked = selected, onCheckedChange = { onAction(OfflineSyncAction.PickerSelect(row.target)) })
        Column(Modifier.weight(1f)) {
            Text(row.target.name, style = MaterialTheme.typography.bodyLarge, maxLines = 1, overflow = TextOverflow.Ellipsis)
            row.accountNo?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
        if (onDevice) {
            StatusChip(AppIcons.RightTick, stringResource(Res.string.feature_offlinesync_on_device), MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }
}

@Composable
private fun PickerMessage(text: StringResource) {
    Text(
        stringResource(text),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.fillMaxWidth().padding(KptTheme.spacing.lg),
    )
}

@Composable
private fun RemoveDialog(record: OfflineRecord, onAction: (OfflineSyncAction) -> Unit) {
    AlertDialog(
        onDismissRequest = { onAction(OfflineSyncAction.DismissRemove) },
        text = { Text(stringResource(Res.string.feature_offlinesync_remove_confirm, record.name)) },
        confirmButton = {
            TextButton(onClick = { onAction(OfflineSyncAction.ConfirmRemove) }, modifier = Modifier.testTag(OfflineSyncTestTags.REMOVE_CONFIRM)) {
                Text(stringResource(Res.string.feature_offlinesync_remove))
            }
        },
        dismissButton = { TextButton(onClick = { onAction(OfflineSyncAction.DismissRemove) }) { Text(stringResource(Res.string.feature_offlinesync_cancel)) } },
    )
}

// ───────────────────────────── shared pieces ─────────────────────────────

/** Selected = the design's indigo container, not Material's default secondary tint. */
@Composable
private fun selectedChipColors() = FilterChipDefaults.filterChipColors(
    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
)

@Composable
private fun OutlinedCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder(),
    ) { content() }
}

@Composable
private fun StatusChip(icon: ImageVector, text: String, container: Color, content: Color) {
    Row(
        Modifier.background(container, MaterialTheme.shapes.extraLarge)
            .padding(horizontal = KptTheme.spacing.sm + KptTheme.spacing.xs, vertical = KptTheme.spacing.xs + KptTheme.spacing.xs / 2),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(KptTheme.spacing.xs + KptTheme.spacing.xs / 2),
    ) {
        Icon(icon, contentDescription = null, tint = content, modifier = Modifier.size(KptTheme.spacing.md))
        Text(text, style = MaterialTheme.typography.labelMedium, color = content)
    }
}

@Composable
private fun IconTile(icon: ImageVector, container: Color, content: Color) {
    Box(Modifier.size(KptTheme.spacing.xl + KptTheme.spacing.sm).background(container, MaterialTheme.shapes.medium), contentAlignment = Alignment.Center) {
        Icon(icon, contentDescription = null, tint = content)
    }
}

@Composable
private fun LoadingSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxSize().padding(KptTheme.spacing.md).testTag(OfflineSyncTestTags.SKELETON),
        verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.md),
    ) {
        repeat(SKELETON_CARDS) {
            Box(Modifier.fillMaxWidth().heightIn(min = KptTheme.spacing.xxl * 2).background(MaterialTheme.colorScheme.surfaceContainerHigh, MaterialTheme.shapes.large))
        }
    }
}

/** "just now" · "5 min ago" · "2 h ago" · "3 days ago". */
@Composable
internal fun relative(atMs: Long, nowMs: Long): String {
    val minutes = ((nowMs - atMs).coerceAtLeast(0) / MINUTE_MS).toInt()
    return when {
        minutes < 1 -> stringResource(Res.string.feature_offlinesync_time_just_now)
        minutes < MINUTES_PER_HOUR -> stringResource(Res.string.feature_offlinesync_time_minutes, minutes)
        minutes < MINUTES_PER_DAY -> stringResource(Res.string.feature_offlinesync_time_hours, minutes / MINUTES_PER_HOUR)
        else -> (minutes / MINUTES_PER_DAY).let { pluralStringResource(Res.plurals.feature_offlinesync_time_days, it, it) }
    }
}

private const val MAX_FAILURE_ROWS = 5
private const val SKELETON_CARDS = 3
private const val PICKER_LIST_HEIGHT_UNITS = 7
private const val MINUTE_MS = 60_000L
private const val MINUTES_PER_HOUR = 60
private const val MINUTES_PER_DAY = 1_440

/** Joins a row's facts ("3 groups · 31 clients"); the separator is punctuation, not copy. */
private const val SEPARATOR = " · "

internal object OfflineSyncTestTags {
    const val SCREEN = "offline_sync_screen"
    const val HEADER = "offline_sync_header"
    const val SYNC_NOW = "offline_sync_sync_now"
    const val OFFLINE_BANNER = "offline_sync_offline_banner"
    const val ACTIVE_JOB = "offline_sync_active_job"
    const val CANCEL_JOB = "offline_sync_cancel_job"
    const val JOB_RESULT = "offline_sync_job_result"
    const val RETRY_FAILED = "offline_sync_retry_failed"
    const val RECORDS = "offline_sync_records"
    const val RECORDS_LEVEL = "offline_sync_records_level"
    const val RECORDS_SEARCH = "offline_sync_records_search"
    const val RECORD_ROW = "offline_sync_record_row"
    const val RECORD_REFRESH = "offline_sync_record_refresh"
    const val RECORD_REMOVE = "offline_sync_record_remove"
    const val REMOVE_CONFIRM = "offline_sync_remove_confirm"
    const val REFRESH_MODE = "offline_sync_refresh_mode"
    const val REFRESH_MODE_TOGGLE = "offline_sync_refresh_mode_toggle"
    const val REFRESH_INTERVAL = "offline_sync_refresh_interval"
    const val WIFI_ONLY = "offline_sync_wifi_only"
    const val CHARGING_ONLY = "offline_sync_charging_only"
    const val DRAFTS_LINK = "offline_sync_drafts_link"
    const val EMPTY = "offline_sync_empty"
    const val ADD_RECORDS = "offline_sync_add_records"
    const val SKELETON = "offline_sync_skeleton"
    const val PICKER = "offline_sync_picker"
    const val PICKER_TABS = "offline_sync_picker_tabs"
    const val PICKER_SEARCH = "offline_sync_picker_search"
    const val PICKER_LIST = "offline_sync_picker_list"
    const val PICKER_ROW = "offline_sync_picker_row"
    const val PICKER_DOWNLOAD = "offline_sync_picker_download"
}
