package com.muamuathu.app.presentation.ui.draw_sketch

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.muamuathu.app.R
import com.muamuathu.app.domain.model.SketchColor
import com.muamuathu.app.presentation.components.topbar.Toolbar
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.helper.observeResultFlow
import com.muamuathu.app.presentation.ui.draw_sketch.drawbox.DrawBox
import com.muamuathu.app.presentation.ui.draw_sketch.drawbox.rememberDrawController
import com.muamuathu.app.presentation.ui.note.viewModel.AddNoteViewModel
import com.muamuathu.app.presentation.ui.note.viewModel.SelectFileViewModel

@Composable
fun ScreenDrawSketch(noteViewModel: AddNoteViewModel, selectViewModel: SelectFileViewModel) {
    val eventHandler = initEventHandler()
    val coroutineScope = rememberCoroutineScope()
    val pathSavedDrawSketch by remember { selectViewModel.pathDrawSketchStateFlow }

    Content(onBack = {
        eventHandler.postNavEvent(NavEvent.PopBackStack())
    }, onSave = {
        coroutineScope.observeResultFlow(selectViewModel.saveImageDrawSketch(it), {
            noteViewModel.updateAttachments(listOf(it))
            eventHandler.postNavEvent(NavEvent.PopBackStack(false))
        })
    }, pathSavedDrawSketch)
}

@Composable
private fun Content(
    onBack: () -> Unit,
    onSave: (Bitmap) -> Unit,
    pathSavedDrawSketch: String,
) {

    val drawController = rememberDrawController()
    var currentColor by remember { mutableStateOf(SketchColor.BLACK) }
    var undoVisibility by remember { mutableStateOf(false) }
    var redoVisibility by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableStateOf(50f) }
    val enableSave by remember { derivedStateOf { undoVisibility || redoVisibility } }

    if (pathSavedDrawSketch.isNotEmpty()) {
        drawController.reset()
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (topView, rowAction, contentView, bottomView) = createRefs()

        Toolbar(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color.White)
                .padding(horizontal = 12.dp)
                .constrainAs(topView) { top.linkTo(parent.top) },
            title = R.string.draw_sketch,
            iconLeft = R.drawable.ic_close,
            onLeftClick = { onBack() },
            onRightClick = {
                drawController.saveBitmap()
            },
            enableIconRight = enableSave
        )

        Row(modifier = Modifier.constrainAs(rowAction) {
            top.linkTo(topView.bottom, 8.dp)
            end.linkTo(parent.end, 16.dp)
        }) {
            IconButton(modifier = Modifier
                .alpha(if (redoVisibility) 1f else 0.5f), enabled = redoVisibility,
                onClick = {
                    drawController.reDo()
                }) {
                Image(painter = painterResource(R.drawable.ic_redo), contentDescription = null)
            }

            IconButton(modifier = Modifier
                .alpha(if (undoVisibility) 1f else 0.5f), enabled = undoVisibility, onClick = {
                drawController.unDo()
            }) {
                Image(painter = painterResource(R.drawable.ic_undo), contentDescription = null)
            }
        }
        Card(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(contentView) {
                top.linkTo(rowAction.bottom, 2.dp)
                bottom.linkTo(bottomView.top, 2.dp)
                height = Dimension.fillToConstraints
            }
            .padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
            elevation = CardDefaults.cardElevation(2.dp)) {
            DrawBox(
                modifier = Modifier.fillMaxSize(),
                drawController = drawController,
                backgroundColor = Color.White,
                bitmapCallback = { imageBitmap, error ->
                    val bitmap = imageBitmap?.asAndroidBitmap()
                    bitmap?.let {
                        onSave(it)
                    }
                }
            ) { undoCount, redoCount ->
                undoVisibility = undoCount != 0
                redoVisibility = redoCount != 0
            }
        }

        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .constrainAs(bottomView) {
                bottom.linkTo(parent.bottom)
            }
            .background(Color.White)) {
            val (ivCircle, colorList, ivPencil, textBrush, slider, sizeSlider) = createRefs()
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(colorList) {
                        top.linkTo(parent.top, 16.dp)
                        start.linkTo(parent.start, 16.dp)
                        end.linkTo(parent.end, 16.dp)
                        width = Dimension.fillToConstraints
                    }, horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(SketchColor.values()) { index, item ->
                    ItemColor(item, item == currentColor) {
                        currentColor = it
                        drawController.changeColor(Color(currentColor.color))
                    }
                }
            }

            Image(painterResource(R.drawable.ic_circle),
                contentDescription = null,
                modifier = Modifier.constrainAs(ivCircle) {
                    top.linkTo(ivPencil.top, (-8).dp)
                    start.linkTo(ivPencil.start)
                    end.linkTo(ivPencil.end)
                })

            Image(painterResource(R.drawable.ic_pen),
                contentDescription = null,
                modifier = Modifier.constrainAs(ivPencil) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(colorList.start)
                })

            Text(
                text = stringResource(R.string.brush_size),
                color = colorResource(R.color.storm_grey),
                modifier = Modifier.constrainAs(textBrush) {
                    top.linkTo(ivCircle.top)
                    start.linkTo(ivPencil.end, 8.dp)
                }, fontSize = 14.sp
            )

            val activeColor = colorResource(
                R.color.royal_blue
            )
            Slider(
                modifier = Modifier.constrainAs(slider) {
                    top.linkTo(textBrush.bottom)
                    bottom.linkTo(ivPencil.bottom)
                    start.linkTo(textBrush.start)
                    end.linkTo(sizeSlider.start, 8.dp)
                    width = Dimension.fillToConstraints
                },
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                    drawController.changeStrokeWidth(it)
                },
                valueRange = 1F..100F,
                colors = SliderDefaults.colors(
                    thumbColor = activeColor,
                    activeTrackColor = activeColor,
                    inactiveTickColor = colorResource(R.color.alice_blue)
                )
            )

            Text(
                text = (sliderPosition).toInt().toString(),
                color = colorResource(R.color.storm_grey),
                modifier = Modifier.constrainAs(sizeSlider) {
                    top.linkTo(ivPencil.top)
                    bottom.linkTo(ivPencil.bottom)
                    end.linkTo(colorList.end)
                }, fontSize = 14.sp
            )
        }
    }
}

@Preview
@Composable
private fun ContentPreview() {
    Content({}, {}, "")
}

@Composable
private fun ItemColor(
    itemColor: SketchColor,
    selected: Boolean,
    itemClick: (selectColor: SketchColor) -> Unit,
) {
    Box(
        modifier = Modifier
            .clickable {
                itemClick(itemColor)
            }, contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Color(itemColor.color))
        )
        if (selected) {
            Image(
                painter = painterResource(id = R.drawable.ic_tick),
                contentDescription = null
            )
        }
    }
}