package com.muamuathu.ui.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.muamuathu.ui.icon.IconSource

@Composable
fun JcSettingSwitch(
    modifier: Modifier,
    icon: IconSource? = null,
    title: String,
    summary: String? = null,
    checked: Boolean,
    onCheckChanged: (Boolean) -> Unit = {}
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
}