@file:OptIn(ExperimentalComposeUiApi::class)

package com.muamuathu.app.presentation.ui.todo

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.muamuathu.app.domain.model.Folder
import com.muamuathu.app.domain.model.SubTask
import com.muamuathu.app.domain.model.Task
import com.muamuathu.app.domain.model.TaskAction
import com.muamuathu.app.presentation.common.FolderSelectView
import com.muamuathu.app.presentation.components.dialog.CustomDatePickerDialog
import com.muamuathu.app.presentation.components.topbar.TopBarBase
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.extensions.formatFromPattern
import com.muamuathu.app.presentation.graph.NavTarget
import com.muamuathu.app.presentation.helper.observeResultFlow
import com.muamuathu.app.presentation.ui.todo.viewModel.AddTodoViewModel
import java.time.ZoneOffset
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

    fun onBackPress() {
        viewModel.clearReference()
        eventHandler.postNavEvent(NavEvent.PopBackStack(false))
    }

    BackHandler(true) {
        onBackPress()
    }

    Content(monthString, yearString, task = task, title = title, content = content, enableSave = enableSave, onInputTitle = {
        viewModel.updateTitle(it)
    }, onInputContent = {
        viewModel.updateContent(it)
    }, onClose = {
        onBackPress()
    }, onSave = {
        coroutineScope.observeResultFlow(viewModel.saveNote(), successHandler = {
            onBackPress()
        })
    }, onCalendar = {
        viewModel.updateDateTime(calendar.timeInMillis)
    }, onChooseFolder = {
        eventHandler.postNavEvent(NavEvent.Action(NavTarget.FolderChoose))
    }, onActionClick = {
        when (it) {
            TaskAction.AddSubTask -> eventHandler.postNavEvent(NavEvent.Action(NavTarget.TodoAddSubTask))
            else -> {}
        }
    })
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    monthString: String,
    yearString: String,
    task: Task,
    title: String,
    content: String,
    enableSave: Boolean,
    onInputTitle: (String) -> Unit,
    onInputContent: (String) -> Unit,
    onClose: () -> Unit,
    onSave: () -> Unit,
    onCalendar: (Long) -> Unit,
    onChooseFolder: () -> Unit,
    onActionClick: (action: TaskAction) -> Unit,
) {
    var isShowDatePicker by remember { mutableStateOf(false) }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.alice_blue))
    ) {
        val (topView, contentView, lazyRowBottom) = createRefs()

        TopBarBase(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .constrainAs(topView) { top.linkTo(parent.top) },
            titleAlign = TextAlign.Center,
            title = stringResource(R.string.txt_add_new_journal),
            navigationIcon = {
                IconButton(onClick = {
                    onClose()
                }) {
                    Image(painter = painterResource(R.drawable.ic_close), contentDescription = null)
                }
            },
            listRightIcon = listOf(Triple({
                Image(
                    painter = painterResource(R.drawable.ic_save), contentDescription = "save"
                )
            }, { onSave() }, enableSave))
        )

        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(contentView) {
                bottom.linkTo(lazyRowBottom.top)
                top.linkTo(topView.bottom)
                height = Dimension.fillToConstraints
            }) {
            val (columnDate, imgCalendar, textTime, folderView, textTitle, textContent) = createRefs()

            Column(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(columnDate) {
                    top.linkTo(parent.top)
                    end.linkTo(imgCalendar.start, 2.dp)
                }
                .padding(vertical = 8.dp), horizontalAlignment = Alignment.End) {
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

            IconButton(modifier = Modifier.constrainAs(imgCalendar) {
                top.linkTo(columnDate.top)
                bottom.linkTo(columnDate.bottom)
                end.linkTo(parent.end)
            }, onClick = {
                isShowDatePicker = true
            }) {
                Image(
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(R.drawable.ic_calendar),
                    contentDescription = "calendar",
                )
            }

            Text(text = stringResource(id = R.string.due_date),
                color = colorResource(R.color.gulf_blue),
                fontSize = 14.sp,
                textAlign = TextAlign.Center, modifier = Modifier.constrainAs(textTime) {
                    top.linkTo(columnDate.top)
                    bottom.linkTo(columnDate.bottom)
                    start.linkTo(parent.start, 16.dp)
                })

            FolderSelectView(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(folderView) {
                    top.linkTo(columnDate.bottom)
                }, folder = Folder()) {
                onChooseFolder()
            }

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
                        top.linkTo(folderView.bottom, 20.dp)
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
                    text = stringResource(id = R.string.sub_tasks), color = colorResource(R.color.gulf_blue), fontSize = 14.sp
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
                        painter = painterResource(id = R.drawable.ic_plus), contentDescription = null
                    )
                    Text(
                        text = stringResource(id = R.string.add_more_sub_task), color = colorResource(id = R.color.royal_blue), fontSize = 14.sp
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = R.string.sub_tasks), color = colorResource(R.color.gulf_blue), fontSize = 14.sp
                    )

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit), contentDescription = null
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
                    shape = RoundedCornerShape(topStartPercent = 8, bottomStartPercent = 8), color = Color.White
                )
                .constrainAs(lazyRowBottom) {
                    bottom.linkTo(parent.bottom)
                }, horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically
        ) {
            items(TaskAction.values()) {
                IconButton(onClick = {
                    onActionClick(it)
                }, modifier = Modifier.padding(6.dp)) {
                    Image(painterResource(it.resource), contentDescription = null)
                }
            }
        }
        if (isShowDatePicker) {
            CustomDatePickerDialog(isShow = isShowDatePicker, onDismissRequest = { isShowDatePicker = false }, onDateChange = {
                isShowDatePicker = false
                onCalendar(it.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())
            })
        }
    }
}

@Composable
fun ItemSubTask(subTask: SubTask, onClose: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            modifier = Modifier.align(Alignment.CenterVertically), painter = painterResource(id = R.drawable.ic_sub_task_small), contentDescription = null
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
                text = subTask.reminderTime.toString(), color = colorResource(R.color.storm_grey), fontSize = 12.sp
            )
        }
        IconButton(onClick = { onClose() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_remove_subtask), contentDescription = null
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
    )
}