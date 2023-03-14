package com.muamuathu.app.presentation.components.checkbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.muamuathu.app.R

@Composable
fun CircleCheckbox(
    selected: Boolean,
    enabled: Boolean = true,
    modifier: Modifier,
    onChecked: () -> Unit,
) {

    IconButton(onClick = { onChecked() },
        modifier = modifier
            .size(25.dp)
            .padding(0.dp),
        enabled = enabled) {
        Image(painter = if (selected) {
            painterResource(R.drawable.ic_checkbox_active)
        } else {
            painterResource(R.drawable.ic_checkbox_default)
        }, contentDescription = "checkbox")
    }
}