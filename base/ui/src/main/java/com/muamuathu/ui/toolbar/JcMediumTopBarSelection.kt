package com.muamuathu.ui.toolbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.muamuathu.ui.icon.IconSource
import com.muamuathu.ui.model.JcMenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JcMediumTopBarSelection(
    modifier: Modifier,
    title: String,
    colors: TopAppBarColors = TopAppBarDefaults.mediumTopAppBarColors(),
    closeIcon: IconSource,
    actionMenuItem: JcMenuItem,
    onCloseClicked: () -> Unit = {},
    onActionClicked: () -> Unit = {}
) {
    JcMediumTopBar(
        modifier = modifier,
        title = title,
        navIcon = closeIcon,
        onNavigationClicked = onCloseClicked,
        colors = colors,
        menuItems = listOf(actionMenuItem),
        onMenuItemClicked = { onActionClicked() }
    )
}