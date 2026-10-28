/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package kpt.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kpt.core.base.designsystem.theme.KptTheme
import kpt.core.designsystem.icon.AppIcons
import kpt.core.designsystem.theme.AppTheme
import kpt.core.designsystem.utility.TabContent
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun AppPasswordField_preview_withoutInput_showPassword() {
    AppTheme {
        AppPasswordField(
            label = "Label",
            value = "",
            onValueChange = {},
            initialShowPassword = true,
            hint = "Hint",
        )
    }
}

@Preview
@Composable
private fun AppSweetErrorPreview() {
    AppTheme {
        AppSweetError(
            message = "Something went wrong. Please check your network.",
            buttonText = "Retry",
            onclick = {},
        )
    }
}

@Preview
@Composable
private fun AppSweetError_NoRetryPreview() {
    AppTheme {
        AppSweetError(
            message = "Failed to fetch data.",
            isRetryEnabled = false,
        )
    }
}

@Preview
@Composable
private fun AppPaginationSweetErrorPreview() {
    AppTheme {
        AppPaginationSweetError(
            onclick = {},
        )
    }
}

@Preview
@Composable
private fun AppTabPreview() {
    AppTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(KptTheme.spacing.sm),
            modifier = Modifier.padding(KptTheme.spacing.md),
        ) {
            AppTab(
                text = "Selected Tab",
                selected = true,
                onClick = {},
            )
            AppTab(
                text = "Unselected Tab",
                selected = false,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun AppTabRowPreview() {
    val pagerState = rememberPagerState { 2 }
    val tabContents = listOf(
        TabContent("Home") { Text("Home Content") },
        TabContent("Profile") { Text("Profile Content") },
    )

    AppTheme {
        AppTabRow(
            tabContents = tabContents,
            pagerState = pagerState,
        )
    }
}

@Preview
@Composable
private fun AppTextFieldDropdownPreview() {
    val options = listOf("Option 1", "Option 2", "Option 3")
    var selectedValue by remember { mutableStateOf("") }

    AppTheme {
        AppTextFieldDropdown(
            value = selectedValue,
            onValueChanged = { selectedValue = it },
            onOptionSelected = { _, option -> selectedValue = option },
            options = options,
            label = "Select Option",
        )
    }
}

@Preview
@Composable
private fun AppTopBarPreview() {
    AppTheme {
        AppTopBar(
            topBarTitle = "Sample Title",
            backPress = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AppTopAppBar_preview() {
    AppTheme {
        AppTopAppBar(
            title = "Title",
            scrollBehavior = TopAppBarDefaults
                .exitUntilCollapsedScrollBehavior(
                    rememberTopAppBarState(),
                ),
            navigationIcon = NavigationIcon(
                navigationIcon = AppIcons.Back,
                navigationIconContentDescription = "Back",
                onNavigationIconClick = { },
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AppTopAppBarOverflow_preview() {
    AppTheme {
        AppTopAppBar(
            title = "Title that is too long for the top line",
            scrollBehavior = TopAppBarDefaults
                .exitUntilCollapsedScrollBehavior(
                    rememberTopAppBarState(),
                ),
            navigationIcon = NavigationIcon(
                navigationIcon = AppIcons.Close,
                navigationIconContentDescription = "Close",
                onNavigationIconClick = { },
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun AppTopAppBarOverflowCutoff_preview() {
    AppTheme {
        AppTopAppBar(
            title = "Title that is too long for the top line and the bottom line",
            scrollBehavior = TopAppBarDefaults
                .exitUntilCollapsedScrollBehavior(
                    rememberTopAppBarState(),
                ),
            navigationIcon = NavigationIcon(
                navigationIcon = AppIcons.Close,
                navigationIconContentDescription = "Close",
                onNavigationIconClick = { },
            ),
        )
    }
}
