package com.muamuathu.ui.others

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.muamuathu.ui.icon.IconSource
import com.muamuathu.ui.icon.JcIcon

@Composable
fun JcUpDownItem(
    modifier: Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    min: Float,
    max: Float,
    value: Float,
    step: Float,
    onIncrease: (Float) -> Unit,
    onDecrease: (Float) -> Unit,
    onTextTransform: (Float) -> String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        JcIcon(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable {
                    if (value - step >= min) {
                        onDecrease(step)
                    }
                }
                .padding(12.dp),
            iconSource = IconSource.Vector(Icons.Rounded.KeyboardArrowDown),
            tint = contentColor
        )

        Text(
            modifier = Modifier,
            text = onTextTransform(value),
            style = MaterialTheme.typography.bodyLarge,
            color = contentColor
        )

        JcIcon(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable {
                    if (value + step <= max) {
                        onIncrease(step)
                    }
                }
                .padding(12.dp),
            iconSource = IconSource.Vector(Icons.Rounded.KeyboardArrowUp),
            tint = contentColor
        )
    }
}