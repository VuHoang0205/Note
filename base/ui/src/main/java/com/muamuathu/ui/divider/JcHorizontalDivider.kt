package com.muamuathu.ui.divider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun JcHorizontalDivider(modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.outlineVariant) {
    Divider(
        modifier = modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = color,
    )
}

@Composable
fun JcHorizontalDashedLine(
    thickness: Dp,
    color: Color = MaterialTheme.colorScheme.outlineVariant
) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Canvas(Modifier.height(thickness).fillMaxWidth()) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
    }
}
