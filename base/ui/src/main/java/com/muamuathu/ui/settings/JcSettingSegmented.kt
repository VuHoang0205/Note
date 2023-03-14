package com.muamuathu.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muamuathu.ui.button.JcSegmentedButton
import com.muamuathu.ui.icon.IconSource
import com.muamuathu.ui.model.JcMenuItem

@Composable
fun JcSettingVerticalSegmented(
    modifier: Modifier,
    icon: IconSource? = null,
    title: String,
    summary: String? = null,
    items: List<JcMenuItem>,
    currentItem: JcMenuItem,
    onItemSelected: (JcMenuItem) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        JcSettingItem(
            modifier = Modifier.fillMaxWidth(),
            title = title,
            summary = summary,
            icon = icon
        )

        JcSegmentedButton(
            modifier = Modifier.padding(
                start = if (icon != null) 56.dp else 16.dp,
                end = 16.dp
            ),
            items = items,
            currentItem = currentItem,
            onItemSelected = onItemSelected
        )
    }
}


@Composable
fun JcSettingHorizontalSegmented(
    modifier: Modifier,
    icon: IconSource? = null,
    title: String,
    summary: String? = null,
    items: List<JcMenuItem>,
    currentItem: JcMenuItem,
    onItemSelected: (JcMenuItem) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        JcSettingItem(
            modifier = Modifier.weight(1f),
            title = title,
            summary = summary,
            icon = icon
        )

        JcSegmentedButton(
            modifier = Modifier.padding(
                end = 16.dp
            ),
            items = items,
            currentItem = currentItem,
            onItemSelected = onItemSelected
        )
    }
}