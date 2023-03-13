package com.muamuathu.ui.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.muamuathu.ui.icon.IconSource
import com.muamuathu.ui.icon.JcIcon

@Composable
internal fun JcBaseIconButton(
    modifier: Modifier,
    iconSource: IconSource,
    colors: IconButtonColors,
    enable: Boolean,
    contentPadding: PaddingValues,
    onClick: () -> Unit
) {

    IconButton(
        modifier = modifier.clip(CircleShape),
        colors = colors,
        onClick = onClick,
        enabled = enable
    ) {
        JcIcon(
            modifier = Modifier
                .padding(contentPadding)
                .clip(CircleShape),
            iconSource = iconSource,
            tint = LocalContentColor.current
        )
    }
}

@Composable
fun JcIconButton(
    modifier: Modifier,
    iconSource: IconSource,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    enable: Boolean = true,
    contentPadding: PaddingValues,
    onClick: () -> Unit
) {
    JcBaseIconButton(
        modifier = modifier,
        iconSource = iconSource,
        colors = colors,
        enable = enable,
        onClick = onClick,
        contentPadding = contentPadding
    )
}