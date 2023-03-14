package com.muamuathu.ui.toolbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muamuathu.ui.icon.IconSource
import com.muamuathu.ui.icon.JcIcon
import com.muamuathu.ui.model.JcMenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JcLargeTopBar(
    modifier: Modifier,
    title: String,
    menuItems: List<JcMenuItem> = emptyList(),
    navIcon: IconSource? = null,
    colors: TopAppBarColors = TopAppBarDefaults.largeTopAppBarColors(),
    onMenuItemClicked: (JcMenuItem) -> Unit = {},
    onNavigationClicked: () -> Unit = {}
) {
    LargeTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        navigationIcon = {
            if (navIcon != null) {
                JcIcon(
                    modifier = Modifier
                        .clickable { onNavigationClicked() }
                        .size(48.dp)
                        .padding(12.dp),
                    iconSource = navIcon,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        actions = {
            menuItems.forEach {
                JcToolbarMenuItem(item = it, onMenuItemClicked = onMenuItemClicked)
            }
        },
        colors = colors
    )
}

