package com.muamuathu.ui.slider

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.muamuathu.ui.icon.IconSource
import com.muamuathu.ui.icon.JcIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JcSlider(
    modifier: Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    enable: Boolean,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    colors: SliderColors = SliderDefaults.colors(),
) {
    Slider(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        enabled = enable,
        valueRange = valueRange,
        steps = steps,
        colors = colors,
        thumb = {
            JcIcon(
                modifier = Modifier,
                iconSource = IconSource.Vector(Icons.Rounded.Circle),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )
}