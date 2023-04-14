@file:OptIn(ExperimentalMaterial3Api::class)

package com.muamuathu.app.presentation.ui.todo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.marosseleng.compose.material3.datetimepickers.time.ui.dialog.TimePickerDialog
import com.muamuathu.app.R
import com.muamuathu.app.domain.model.SubTask
import com.muamuathu.app.presentation.components.topbar.TopBarBase
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.ui.todo.viewModel.SubTasksViewModel
import java.time.ZonedDateTime

@Composable
fun ScreenAddSubTask() {

    val context = LocalContext.current
    val eventHandler = initEventHandler()
    val subTasksViewModel = hiltViewModel<SubTasksViewModel>()
    val tagSelectedList = remember { mutableStateListOf<SubTask>() }
    val taskList by subTasksViewModel.subTaskFlow.collectAsState()
    var subTaskName by remember { mutableStateOf("") }
    var selectDate by remember { mutableStateOf(ZonedDateTime.now()) }
    var timeString by remember { mutableStateOf("") }

    Content(taskSelectedList = tagSelectedList, taskList = taskList, timeString = timeString, subTaskName = subTaskName, onValueChange = {
        subTaskName = it
    }, onTimePicker = {
        timeString = it
    }, onItemClick = {
        if (tagSelectedList.contains(it)) {
            tagSelectedList.remove(it)
        } else {
            tagSelectedList.add(it)
        }
    }, onClose = {
        eventHandler.postNavEvent(NavEvent.PopBackStack(false))
    }, onAdd = {
        val subTask = SubTask(name = subTaskName, reminderTime = selectDate.toEpochSecond())
        subTasksViewModel.saveSubTask(subTask)
    }, onDone = {
        eventHandler.postNavEvent(NavEvent.PopBackStack(false))
    })
}

@Composable
private fun Content(
    taskSelectedList: MutableList<SubTask>,
    taskList: List<SubTask>,
    timeString: String,
    subTaskName: String,
    onValueChange: (String) -> Unit,
    onTimePicker: (String) -> Unit,
    onItemClick: (SubTask) -> Unit,
    onClose: () -> Unit,
    onAdd: () -> Unit,
    onDone: (MutableList<SubTask>) -> Unit,
) {

    val stateVisibility by remember { derivedStateOf { subTaskName.isNotEmpty() } }
    var isShowTimePicker by remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.alice_blue))
    ) {
        val (topView, contentView) = createRefs()

        TopBarBase(title = stringResource(id = R.string.sub_tasks), titleAlign = TextAlign.Center, navigationIcon = {
            IconButton(onClick = {
                onClose()
            }) {
                Image(
                    painter = painterResource(R.drawable.ic_close), contentDescription = "close"
                )
            }
        }, listRightIcon = null, modifier = Modifier.constrainAs(topView) { top.linkTo(parent.top) })

        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(contentView) {
                top.linkTo(topView.bottom, 40.dp)
                bottom.linkTo(parent.bottom, 30.dp)
                height = Dimension.fillToConstraints
            }) {
            val (imgPlus, textFieldInput, btnAdd, viewLine, rowTime, lazyColumnFolder, btnDone) = createRefs()

            Image(painterResource(R.drawable.ic_plus), null, modifier = Modifier.constrainAs(imgPlus) {
                top.linkTo(parent.top)
                start.linkTo(lazyColumnFolder.start)
            })

            if (subTaskName.isNotEmpty()) {
                AnimatedVisibility(visible = stateVisibility, modifier = Modifier
                    .height(40.dp)
                    .width(100.dp)
                    .constrainAs(btnAdd) {
                        top.linkTo(imgPlus.top)
                        bottom.linkTo(imgPlus.bottom)
                        end.linkTo(lazyColumnFolder.end)
                    }) {
                    Button(
                        onClick = {
                            onAdd()
                        }, shape = RoundedCornerShape(4.dp), colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.royal_blue))
                    ) {
                        Text(
                            text = stringResource(R.string.add), fontSize = 14.sp, modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally), color = Color.White, textAlign = TextAlign.Center
                        )
                    }
                }
            }

            TextField(
                value = subTaskName,
                placeholder = {
                    Text(stringResource(R.string.add_sub_task), color = colorResource(R.color.storm_grey))
                },
                onValueChange = {
                    onValueChange(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .constrainAs(textFieldInput) {
                        top.linkTo(imgPlus.top)
                        start.linkTo(imgPlus.end)
                        bottom.linkTo(imgPlus.bottom)
                        end.linkTo(btnAdd.start)
                        width = Dimension.fillToConstraints
                    },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onSurface,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledTextColor = Color.Transparent,
                ),
                textStyle = TextStyle(fontSize = 14.sp, color = colorResource(R.color.gulf_blue)),
            )

            Row(modifier = Modifier
                .constrainAs(rowTime) {
                    start.linkTo(imgPlus.start)
                    top.linkTo(textFieldInput.bottom, 16.dp)
                }
                .clickable {
                    isShowTimePicker = true
                }, verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painterResource(R.drawable.ic_clock), null, colorFilter = ColorFilter.tint(colorResource(id = R.color.royal_blue)))
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = timeString.ifEmpty { stringResource(id = R.string.select_a_due_time) },
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.storm_grey)
                )
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(R.color.link_water))
                .constrainAs(viewLine) {
                    top.linkTo(rowTime.bottom, 8.dp)
                })

            LazyColumn(
                modifier = Modifier.constrainAs(lazyColumnFolder) {
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    top.linkTo(viewLine.bottom, 16.dp)
                    bottom.linkTo(btnDone.top)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                itemsIndexed(taskList) { _, item ->
                    ItemSubTask(item) {
                        onItemClick(item)
                    }
                }
            }

            TextButton(enabled = taskSelectedList.isNotEmpty(), onClick = {
                onDone(taskSelectedList)
            }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .alpha(if (taskSelectedList.isNotEmpty()) 1f else 0.5f)
                .background(
                    colorResource(R.color.royal_blue), RoundedCornerShape(4.dp)
                )
                .constrainAs(btnDone) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(lazyColumnFolder.start)
                    end.linkTo(lazyColumnFolder.end)
                    width = Dimension.fillToConstraints
                }) {
                Text(
                    text = stringResource(R.string.done).toUpperCase(Locale.current),
                    color = Color.White,
                    fontSize = 14.sp,
                )
            }
        }
        if (isShowTimePicker) {
            TimePickerDialog(onDismissRequest = { isShowTimePicker = false }, onTimeChange = {
                isShowTimePicker = false
                onTimePicker(it.toString())
            })
        }
    }
}

@Preview
@Composable
private fun PreviewContent() {
    Content(mutableListOf(), emptyList(), "", "", {}, {}, {}, {}, {}, {})
}