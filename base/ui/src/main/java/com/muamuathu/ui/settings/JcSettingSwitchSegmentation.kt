package com.muamuathu.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muamuathu.ui.button.JcSegmentedButton
import com.muamuathu.ui.icon.IconSource
import com.muamuathu.ui.model.JcMenuItem

@Composable
fun JcSettingSwitchSegmentation(
    modifier: Modifier,
    icon: IconSource? = null,
    title: String,
    summary: String? = null,
    checked: Boolean,
    onCheckChanged: (Boolean) -> Unit = {},
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
            modifier = modifier,
            title = title,
            summary = summary,
            icon = icon,
            widget = {
                Switch(
                    checked = checked,
                    onCheckedChange = onCheckChanged,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        )

        if (checked) {
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

}