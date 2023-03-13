package com.muamuathu.ui.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.muamuathu.ui.icon.IconSource
import com.muamuathu.ui.others.JcUpDownItem

@Composable
fun JcSettingUpDown(
    modifier: Modifier,
    icon: IconSource? = null,
    title: String,
    summary: String? = null,
    min: Float,
    max: Float,
    value: Float,
    step: Float,
    onIncrease: (Float) -> Unit,
    onDecrease: (Float) -> Unit,
    onTextTransform: (Float) -> String
) {
    Row(modifier = modifier) {
        JcSettingItem(
            modifier = Modifier.weight(1f),
            title = title,
            summary = summary,
            icon = icon
        )

        JcUpDownItem(
            modifier = Modifier,
            value = value,
            min = min,
            max = max,
            step = step,
            onIncrease = onIncrease,
            onDecrease = onDecrease,
            onTextTransform = onTextTransform
        )
    }
}