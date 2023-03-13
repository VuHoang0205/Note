package com.muamuathu.ui.tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.muamuathu.ui.icon.JcIcon
import com.muamuathu.ui.model.JcMenuItem

@Composable
fun JcTabRow(
    modifier: Modifier,
    selectedTabIndex: Int,
    menuItems: List<JcMenuItem>,
    selectedColor: Color,
    contentColor: Color,
    containerColor: Color,
    selectedContainerColor: Color,
    onTabSelected: (Int, JcMenuItem) -> Unit
) {
    TabRow(
        modifier = modifier.clip(RoundedCornerShape(100.dp)),
        selectedTabIndex = selectedTabIndex,
        contentColor = contentColor,
        containerColor = containerColor,
        divider = {},
        indicator = {},
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
    val tabModifier = remember(selected) {
        if (selected) modifier.background(
            color = selectedContainerColor,
            shape = RoundedCornerShape(100.dp)
        )
        else modifier
    }
    Tab(
        modifier = tabModifier,
        selected = selected,
        onClick = { onTabSelected(menuItem) },
        selectedContentColor = selectedColor,
        unselectedContentColor = contentColor,
        text = menuItem.text?.let {
            {
                Text(
                    modifier = Modifier,
                    text = menuItem.text,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        icon = menuItem.icon?.let {
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