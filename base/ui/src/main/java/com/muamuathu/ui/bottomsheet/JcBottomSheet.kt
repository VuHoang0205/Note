package com.muamuathu.ui.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun JcBottomSheet(modifier: Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.surface
        )
    ) {
        Spacer(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(vertical = 16.dp)
                .width(32.dp)
                .height(4.dp)
                .background(
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(100.dp)
                )
        )

        content()
    }
}