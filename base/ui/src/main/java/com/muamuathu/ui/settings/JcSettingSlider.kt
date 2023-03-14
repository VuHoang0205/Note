package com.muamuathu.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muamuathu.ui.icon.IconSource
import com.muamuathu.ui.slider.JcSlider

@Composable
fun JcSettingSlider(
    modifier: Modifier,
    icon: IconSource? = null,
    title: String,
    summary: String? = null,
    value: Float,
    enable: Boolean,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    colors: SliderColors = SliderDefaults.colors(
        activeTrackColor = MaterialTheme.colorScheme.onSurfaceVariant,
        activeTickColor = MaterialTheme.colorScheme.surfaceVariant,
        inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
        inactiveTickColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        thumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ),
    onValueChange: (Float) -> Unit,
) {
    Column(modifier = modifier) {
        JcSettingItem(
            modifier = Modifier.fillMaxWidth(),
            title = title,
            summary = summary,
            icon = icon
        )

        JcSlider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp - 8.dp, end = 16.dp),
            value = value,
            onValueChange = onValueChange,
            enable = enable,
            valueRange = valueRange,
            steps = steps,
            colors = colors
        )
    }
}