package com.muamuathu.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.muamuathu.ui.icon.IconSource

@Composable
fun JcSettingText(
    modifier: Modifier,
    icon: IconSource? = null,
    title: String,
    summary: String? = null,
    onClicked: () -> Unit = {}
) {
    JcSettingItem(
        modifier = modifier.clickable { onClicked() },
        title = title,
        summary = summary,
        icon = icon
    )
}