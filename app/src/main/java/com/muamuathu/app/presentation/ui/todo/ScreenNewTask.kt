package com.muamuathu.app.presentation.ui.todo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.muamuathu.app.R
import com.muamuathu.app.domain.model.SubTask
import com.muamuathu.app.domain.model.Task
import com.muamuathu.app.domain.model.TaskAction
import com.muamuathu.app.presentation.components.topbar.TopBarBase
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.extensions.formatFromPattern
import com.muamuathu.app.presentation.graph.NavTarget
import com.muamuathu.app.presentation.helper.observeResultFlow
import com.muamuathu.app.presentation.ui.todo.viewModel.AddTodoViewModel
import java.util.*

@Composable
fun ScreenNewTask() {

    val eventHandler = initEventHandler()
    val context = LocalContext.current as ComponentActivity

    val viewModel = hiltViewModel<AddTodoViewModel>(context)
    val coroutineScope = rememberCoroutineScope()

    val calendar: Calendar by remember {
        mutableStateOf(Calendar.getInstance(TimeZone.getDefault()).clone() as Calendar)
    }
    val dateTime by viewModel.dateTime.collectAsState()
    val title by viewModel.title.collectAsState()
    val content by viewModel.content.collectAsState()
    val task by viewModel.task.collectAsState()
    val enableSave by viewModel.isValidData.collectAsState()

    val monthString by remember { derivedStateOf { dateTime.formatFromPattern("dd MMMM") } }
    val yearString by remember { derivedStateOf { dateTime.formatFromPattern("yyyy, EEEE") } }
    val timeString by remember { derivedStateOf { dateTime.formatFromPattern("hh:mm a") } }

    fun onBackPress() {
        viewModel.clearReference()
        eventHandler.postNavEvent(NavEvent.PopBackStack(false))
    }

    BackHandler(true) {
        onBackPress()
    }

    Content(monthString,
        yearString,
        timeString,
        task = task,
        title = title,
        content = content,
        enableSave = enableSave,
        onInputTitle = {
            viewModel.updateTitle(it)
        },
        onInputContent = {
            viewModel.updateContent(it)
        },
        onClose = {
            onBackPress()
        },
        onSave = {
            coroutineScope.observeResultFlow(viewModel.saveNote(), successHandler = {
                onBackPress()
            })
        },
        onCalendar = {
            val dialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    viewModel.updateDateTime(calendar.timeInMillis)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        },
        onChooseFolder = {
            eventHandler.postNavEvent(NavEvent.Action(NavTarget.FolderChoose))
        },
        onTimePicker = {
            val dialog = TimePickerDialog(
                context, { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    viewModel.updateDateTime(calendar.timeInMillis)
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
            )
            dialog.show()
        },
        onActionClick = {
            when (it) {
                TaskAction.AddSubTask -> eventHandler.postNavEvent(NavEvent.Action(NavTarget.TodoAddSubTask))
                else -> {}
            }
        },
        onTageClick = {
            eventHandler.postNavEvent(NavEvent.Action(NavTarget.NoteAddTags))
        })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    monthString: String,
    yearString: String,
    timeString: String,
    task: Task,
    title: String,
    content: String,
    enableSave: Boolean,
    onInputTitle: (String) -> Unit,
    onInputContent: (String) -> Unit,
    onClose: () -> Unit,
    onSave: () -> Unit,
    onCalendar: () -> Unit,
    onChooseFolder: () -> Unit,
    onTimePicker: () -> Unit,
    onActionClick: (action: TaskAction) -> Unit,
    onTageClick: () -> Unit,
) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.alice_blue))
    ) {
        val (topView, contentView, lazyRowBottom) = createRefs()

        TopBarBase(modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 12.dp)
            .constrainAs(topView) { top.linkTo(parent.top) },
            titleAlign = TextAlign.Center,
            title = stringResource(R.string.txt_add_new_journal),
            navigationIcon = {
                IconButton(onClick = {
                    onClose()
                }) {
                    Image(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = null
                    )
                }
            }, listRightIcon = listOf(Triple({
                Image(
                    painter = painterResource(R.drawable.ic_save),
                    contentDescription = "save"
                )
            }, { onSave() }, enableSave))
        )

        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(contentView) {
                top.linkTo(topView.bottom, 16.dp)
                bottom.linkTo(lazyRowBottom.top)
                height = Dimension.fillToConstraints
            }) {
            val (columnDate, imgCalendar, imgClock, textTime, divider1, rowFolder, divider2, textTitle, textContent) = createRefs()

            Column(modifier = Modifier
                .constrainAs(columnDate) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 20.dp)
                }
                .padding(vertical = 10.dp)) {
                Text(
                    text = monthString,
                    color = colorResource(R.color.gulf_blue),
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = yearString,
                    color = colorResource(R.color.gulf_blue),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                )
            }

            IconButton(modifier = Modifier
                .constrainAs(imgCalendar) {
                    top.linkTo(columnDate.top)
                    bottom.linkTo(columnDate.bottom)
                    start.linkTo(columnDate.end, 16.dp)
                }
                .size(24.dp)
                .background(
                    shape = CircleShape, color = colorResource(R.color.royal_blue)
                ), onClick = {
                onCalendar()
            }) {
                Image(
                    painter = painterResource(R.drawable.ic_calendar),
                    contentDescription = "calendar",
                )
            }

            IconButton(onClick = {
                onTimePicker()
            }, modifier = Modifier.constrainAs(imgClock) {
                top.linkTo(columnDate.top)
                bottom.linkTo(columnDate.bottom)
                end.linkTo(parent.end, 10.dp)
            }) {
                Image(
                    painter = painterResource(R.drawable.ic_clock_time),
                    contentDescription = "clock"
                )
            }

            Text(text = timeString,
                color = colorResource(R.color.gulf_blue),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.constrainAs(textTime) {
                    top.linkTo(columnDate.top)
                    bottom.linkTo(columnDate.bottom)
                    end.linkTo(imgClock.start)
                })

            Divider(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(R.color.gainsboro))
                .constrainAs(divider1) {
                    top.linkTo(columnDate.bottom, 16.dp)
                })

            ConstraintLayout(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onChooseFolder.invoke()
                }
                .constrainAs(rowFolder) {
                    top.linkTo(divider1.bottom)
                }) {
                val (icFolder, textChooseFolder, icArrow) = createRefs()
                Image(painter = painterResource(R.drawable.ic_folder),
                    contentDescription = "folder",
                    colorFilter = ColorFilter.tint(colorResource(R.color.storm_grey)),
                    modifier = Modifier
                        .padding(top = 24.dp, start = 16.dp, bottom = 24.dp)
                        .constrainAs(icFolder) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        })

                Text(text = task.name.ifEmpty { stringResource(R.string.txt_choose_folder) },
                    color = colorResource(if (task.name.isEmpty()) R.color.storm_grey else R.color.gulf_blue),
                    fontSize = 14.sp,
                    textAlign = TextAlign.End,
                    modifier = Modifier.constrainAs(textChooseFolder) {
                        top.linkTo(icFolder.top)
                        bottom.linkTo(icFolder.bottom)
                        start.linkTo(icFolder.end, 16.dp)
                    })

                Image(
                    painter = painterResource(R.drawable.ic_down),
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(-90f)
                        .constrainAs(icArrow) {
                            top.linkTo(icFolder.top)
                            bottom.linkTo(icFolder.bottom)
                            end.linkTo(parent.end, 8.dp)
                        },
                    colorFilter = ColorFilter.tint(colorResource(R.color.storm_grey))
                )
            }

            Divider(modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.storm_grey))
                .height(1.dp)
                .background(colorResource(R.color.gainsboro))
                .constrainAs(divider2) {
                    top.linkTo(rowFolder.bottom)
                })

            TextField(
                value = title,
                placeholder = {
                    Text(
                        stringResource(R.string.txt_title), color = colorResource(R.color.gulf_blue)
                    )
                },
                onValueChange = {
                    onInputTitle(it)
                },
                textStyle = TextStyle(
                    fontSize = 16.sp, color = colorResource(R.color.gulf_blue)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .constrainAs(textTitle) {
                        top.linkTo(rowFolder.bottom, 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
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
                singleLine = true,
            )

            val maxChar = 200
            TextField(
                value = content,
                placeholder = { Text(stringResource(R.string.txt_write_more_here)) },
                onValueChange = {
                    if (it.length < maxChar) {
                        onInputContent(it)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .constrainAs(textContent) {
                        top.linkTo(textTitle.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
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
                textStyle = TextStyle(
                    fontSize = 14.sp, color = colorResource(R.color.storm_grey)
                ),
            )
        }

        if (task.subTasks.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = stringResource(id = R.string.sub_tasks),
                    color = colorResource(R.color.gulf_blue),
                    fontSize = 14.sp
                )
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(task.subTasks) {
                        ItemSubTask(subTask = it) {

                        }
                    }
                }
                Divider(color = Color.Gray, thickness = 0.5.dp)

                TextButton(onClick = { /*TODO*/ }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(id = R.string.add_more_sub_task),
                        color = colorResource(id = R.color.royal_blue),
                        fontSize = 14.sp
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.sub_tasks),
                        color = colorResource(R.color.gulf_blue),
                        fontSize = 14.sp
                    )

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = null
                        )
                    }
                }

                Row() {

                }
            }
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 20.dp)
                .background(
                    shape = RoundedCornerShape(topStartPercent = 8, bottomStartPercent = 8),
                    color = Color.White
                )
                .constrainAs(lazyRowBottom) {
                    bottom.linkTo(parent.bottom)
                },
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(TaskAction.values()) {
                IconButton(onClick = {
                    onActionClick(it)
                }, modifier = Modifier.padding(6.dp)) {
                    Image(painterResource(it.resource), contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun ItemSubTask(subTask: SubTask, onClose: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            modifier = Modifier.align(Alignment.CenterVertically),
            painter = painterResource(id = R.drawable.ic_sub_task_small),
            contentDescription = null
        )

        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = subTask.name, color = colorResource(R.color.gulf_blue), fontSize = 14.sp
            )
            Text(
                text = subTask.reminderTime.toString(),
                color = colorResource(R.color.storm_grey),
                fontSize = 12.sp
            )
        }
        IconButton(onClick = { onClose() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_remove_subtask),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun PreviewContent() {
    Content(
        "",
        "",
        "",
        Task(),
        "",
        "",
        true,
        {},
        {},
        {},
        {},
        {},
        {},
        {},
        {},
        {},
    )
}