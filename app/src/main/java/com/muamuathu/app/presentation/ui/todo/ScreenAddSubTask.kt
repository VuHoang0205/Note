@file:OptIn(ExperimentalMaterial3Api::class)

package com.muamuathu.app.presentation.ui.todo

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.muamuathu.app.R
import com.muamuathu.app.domain.model.SubTask
import com.muamuathu.app.presentation.components.topbar.TopBarBase
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.ui.todo.viewModel.AddTodoViewModel
import com.muamuathu.app.presentation.ui.todo.viewModel.SubTasksViewModel

@Composable
fun ScreenAddSubTask() {
    val eventHandler = initEventHandler()

    val addNoteViewModel = hiltViewModel<AddTodoViewModel>(LocalContext.current as ComponentActivity)
    val subTasksViewModel = hiltViewModel<SubTasksViewModel>()
    val coroutineScope = rememberCoroutineScope()
    val tagSelectedList = remember { mutableStateListOf<SubTask>() }
    val taskList by subTasksViewModel.subTaskFlow.collectAsState()

    Content(
        taskSelectedList = tagSelectedList,
        taskList = taskList,
        onItemClick = {
            if (tagSelectedList.contains(it)) {
                tagSelectedList.remove(it)
            } else {
                tagSelectedList.add(it)
            }
        },
        onClose = {
            eventHandler.postNavEvent(NavEvent.PopBackStack(false))
        },
        onAdd = {
            subTasksViewModel.saveSubTask(SubTask())
        }, onDone = {
            eventHandler.postNavEvent(NavEvent.PopBackStack(false))
        })
}

@Composable
private fun Content(
    taskSelectedList: MutableList<SubTask>,
    taskList: List<SubTask>,
    onItemClick: (SubTask) -> Unit,
    onClose: () -> Unit,
    onAdd: (String) -> Unit,
    onDone: (MutableList<SubTask>) -> Unit,
) {

    var subTaskName by remember { mutableStateOf("") }
    val stateVisibility by remember {
        derivedStateOf { subTaskName.isNotEmpty() }
    }

    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .background(colorResource(R.color.alice_blue))) {
        val (topView, contentView) = createRefs()

        TopBarBase(title = stringResource(id = R.string.sub_tasks),
            titleAlign = TextAlign.Center,
            navigationIcon = {
                IconButton(onClick = {
                    onClose()
                }) {
                    Image(painter = painterResource(R.drawable.ic_close),
                        contentDescription = "close")
                }
            },
            listRightIcon = null,
            modifier = Modifier.constrainAs(topView) { top.linkTo(parent.top) })

        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(contentView) {
                top.linkTo(topView.bottom, 40.dp)
                bottom.linkTo(parent.bottom, 30.dp)
                height = Dimension.fillToConstraints
            }) {
            val (imgPlus, textFieldInput, btnAdd, viewLine, lazyColumnFolder, btnDone) = createRefs()
            Image(painterResource(R.drawable.ic_plus),
                null,
                modifier = Modifier.constrainAs(imgPlus) {
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
                    Button(onClick = {
                        onAdd(subTaskName)
                        subTaskName = ""
                    }, shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.royal_blue))
                    ) {
                        Text(
                            text = stringResource(R.string.add),
                            fontSize = 14.sp,
                            modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            TextField(
                value = subTaskName,
                placeholder = {
                    Text(stringResource(R.string.add_sub_task), color = colorResource(
                        R.color.storm_grey))
                },
                onValueChange = {
                    subTaskName = it
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
                textStyle = TextStyle(fontSize = 14.sp,
                    color = colorResource(R.color.gulf_blue)),
            )

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(R.color.link_water))
                .constrainAs(viewLine) {
                    top.linkTo(textFieldInput.bottom, 4.dp)
                })

            LazyColumn(modifier = Modifier.constrainAs(lazyColumnFolder) {
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)
                top.linkTo(viewLine.bottom, 16.dp)
                bottom.linkTo(btnDone.top)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            },
                horizontalAlignment = Alignment.CenterHorizontally) {
                itemsIndexed(taskList) { _, item ->
                    ItemSubTask(item) {
                        onItemClick(item)
                    }
                }
            }

            TextButton(
                enabled = taskSelectedList.isNotEmpty(),
                onClick = {
                    onDone(taskSelectedList)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .alpha(if (taskSelectedList.isNotEmpty()) 1f else 0.5f)
                    .background(
                        colorResource(R.color.royal_blue),
                        RoundedCornerShape(4.dp)
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
    }
}

@Preview
@Composable
private fun PreviewContent() {
    Content(mutableListOf(), emptyList(), {}, {}, {}, {})
}