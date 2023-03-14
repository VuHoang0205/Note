package com.muamuathu.ui.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun JcLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.Black.copy(0.2f)
            )
            .clickable { }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(80.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(12.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 4.dp
            )
        }
    }
}