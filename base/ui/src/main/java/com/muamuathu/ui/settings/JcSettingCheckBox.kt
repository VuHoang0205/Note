package com.muamuathu.ui.settings

import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.muamuathu.ui.icon.IconSource

@Composable
fun JcSettingCheckBox(
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
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckChanged
            )
        }
    )
}