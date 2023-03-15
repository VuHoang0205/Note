package com.muamuathu.app.presentation.components.topbar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.muamuathu.app.R

@Composable
fun Toolbar(
    modifier: Modifier,
    @StringRes title: Int,
    textColor: Color = colorResource(R.color.gulf_blue),
    @DrawableRes iconLeft: Int = R.drawable.ic_close,
    onLeftClick: () -> Unit,
    @DrawableRes iconRight: Int = R.drawable.ic_save,
    onRightClick: () -> Unit,
    enableIconRight: Boolean = true,
) {
    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {

        IconButton(onClick = {
            onLeftClick()
        }) {
            Image(painter = painterResource(iconLeft),
                contentDescription = "close")
        }

        Text(
            text = stringResource(title),
            color = textColor,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )

        IconButton(onClick = {
            onRightClick()
        },
            enabled = enableIconRight,
            modifier = Modifier.alpha(if (enableIconRight) 1f else 0.5f)) {
            Image(painter = painterResource(iconRight),
                contentDescription = "save")
        }
    }

}