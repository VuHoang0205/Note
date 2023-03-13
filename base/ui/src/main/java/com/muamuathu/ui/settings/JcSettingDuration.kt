package com.muamuathu.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.muamuathu.ui.icon.IconSource
import com.muamuathu.ui.slider.JcSlider
import java.time.Duration

@Composable
fun JcSettingDuration(
    modifier: Modifier,
    icon: IconSource? = null,
    title: String,
    summary: String? = null,
    duration: Duration,
    enable: Boolean,
    durationRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    colors: SliderColors = SliderDefaults.colors(
        activeTrackColor = MaterialTheme.colorScheme.onSurfaceVariant,
        activeTickColor = MaterialTheme.colorScheme.surfaceVariant,
        inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
        inactiveTickColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        thumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ),
    onDurationChanged: (Duration) -> Unit,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        JcSettingItem(
            modifier = Modifier.fillMaxWidth(),
            title = title,
            summary = summary,
            icon = icon,
            widget = {
                Text(
                    modifier = Modifier.padding(start = 8.dp).defaultMinSize(minWidth = 80.dp),
                    text = duration.format(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End
                )
            }
        )

        JcSlider(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.6f)
                .padding(start = 16.dp - 8.dp, end = 16.dp),
            value = duration.seconds.toFloat(),
            onValueChange = {
                onDurationChanged(Duration.ofSeconds(it.toLong()))
            },
            enable = enable,
            valueRange = durationRange,
            steps = steps,
            colors = colors
        )
    }
}

private fun Duration.format(): String {
    val hours = seconds / 3600
    val mins = (seconds % 3600) / 60
    val secs = (seconds % 3600) % 60
    return when (hours) {
        0L -> String.format("%dm %ds", mins, secs)
        else -> String.format("%dh %dm %ds", hours, mins, secs)
    }
}