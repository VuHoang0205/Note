package com.muamuathu.app.presentation.components.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.muamuathu.app.R


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
        modifier = modifier.background(backgroundColor),
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        contentColor = Color.White
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()

        ) {
            val (leftItem, text, rightIconList) = createRefs()
            if (navigationIcon != null) {
                Box(modifier = Modifier.constrainAs(leftItem) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }) {
                    navigationIcon.invoke()
                }
            } else {
                val AppBarHorizontalPadding = 4.dp
                val TitleInsetWithoutIcon =
                    Modifier.width(16.dp - AppBarHorizontalPadding)
                Spacer(TitleInsetWithoutIcon.constrainAs(leftItem) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                })
            }
            Text(
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(text) {
                        when (titleAlign) {
                            TextAlign.Center -> {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            else -> {
                                start.linkTo(leftItem.end)
                            }
                        }
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                text = title,
                maxLines = 1,
                fontSize = 20.sp,
                overflow = TextOverflow.Ellipsis,
                color = colorResource(R.color.gulf_blue),
                textAlign = titleAlign
            )
            if (listRightIcon != null) {
                LazyRow(modifier = Modifier.constrainAs(rightIconList) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }) {
                    items(listRightIcon) {
                        IconButton(onClick = it.second) {
                            it.first.invoke()
                        }
                    }
                }
            }
        }
    }
}