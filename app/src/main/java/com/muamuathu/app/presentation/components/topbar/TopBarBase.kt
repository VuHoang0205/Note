package com.muamuathu.app.presentation.components.topbar

import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarBase(
    modifier: Modifier = Modifier,
    title: String,
    titleAlign: TextAlign,
    navigationIcon: @Composable (() -> Unit)?,
    listRightIcon: (List<Pair<@Composable () -> Unit, () -> Unit>>)?,
    backgroundColor: Color = Color.White,
) {
    TopAppBar(
        modifier = modifier
            .background(backgroundColor)
            .background(Color.Transparent),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}