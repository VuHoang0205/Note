package com.muamuathu.ui.tab

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.muamuathu.ui.icon.JcIcon
import com.muamuathu.ui.model.JcMenuItem

@Composable
fun JcScrollableTabRow(
    modifier: Modifier,
    selectedTabIndex: Int,
    menuItems: List<JcMenuItem>,
    selectedColor: Color,
    contentColor: Color,
    containerColor: Color,
    selectedContainerColor: Color,
    onTabSelected: (Int, JcMenuItem) -> Unit
) {
    ScrollableTabRow(
        modifier = modifier,
        selectedTabIndex = selectedTabIndex,
        contentColor = contentColor,
        containerColor = containerColor,
        divider = {},
        edgePadding = 0.dp,
        tabs = {
            menuItems.forEachIndexed { position, item ->
                JcTabItem(
                    modifier = Modifier,
                    menuItem = item,
                    selected = position == selectedTabIndex,
                    onTabSelected = {
                        onTabSelected(position, it)
                    },
                    selectedColor = selectedColor,
                    contentColor = contentColor,
                    selectedContainerColor = selectedContainerColor
                )
            }
        }
    )
}

@Composable
private fun JcTabItem(
    modifier: Modifier,
    menuItem: JcMenuItem,
    selected: Boolean,
    onTabSelected: (JcMenuItem) -> Unit,
    selectedColor: Color,
    contentColor: Color,
    selectedContainerColor: Color,
) {
    Tab(
        modifier = modifier,
        selected = selected,
        onClick = { onTabSelected(menuItem) },
        selectedContentColor = selectedColor,
        unselectedContentColor = contentColor,
        text =
        menuItem.text?.let {
            {
                Text(
                    modifier = Modifier,
                    text = menuItem.text,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        },
        icon =
        menuItem.icon?.let {
            {
                JcIcon(
                    modifier = Modifier.size(24.dp),
                    iconSource = menuItem.icon,
                    tint = Color.Unspecified
                )
            }
        }

    )
}