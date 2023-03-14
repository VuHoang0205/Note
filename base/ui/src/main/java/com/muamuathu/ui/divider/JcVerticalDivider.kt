package com.muamuathu.ui.divider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp

@Composable
fun JcVerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp,
    color: Color = MaterialTheme.colorScheme.outline,
) {
    Box(modifier.width(thickness).background(color))
}

@Composable
fun JcVerticalDashedLine(
    thickness: Dp,
    color: Color = MaterialTheme.colorScheme.outlineVariant
) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Canvas(Modifier.width(thickness).fillMaxHeight()) {
        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(0f, size.height),
            pathEffect = pathEffect
        )
    }
}
