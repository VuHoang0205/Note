package com.muamuathu.app.presentation.ui.todo

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.muamuathu.app.R
import com.muamuathu.app.data.entity.EntityTask
import com.muamuathu.app.presentation.common.CalendarView
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.extensions.indexOfDate
import com.muamuathu.app.presentation.extensions.toDayOfWeek
import com.muamuathu.app.presentation.extensions.toHour
import com.muamuathu.app.presentation.graph.NavTarget
import com.muamuathu.app.presentation.ui.todo.viewModel.TodoViewModel
import de.charlex.compose.*
import java.time.ZoneId

import java.time.ZonedDateTime
import java.util.*

const val TAB_1 = 0
const val TAB_2 = 1

sealed class Tabs(val tabName: Int) {
    object Tab1 : Tabs(R.string.txt_incomplete_task)
    object Tab2 : Tabs(R.string.txt_completed_task)
}

@Composable
fun ScreenTodo() {

    val context = LocalContext.current
    val viewModel: TodoViewModel = hiltViewModel()
    val eventHandler = initEventHandler()
    val dateList by viewModel.bindDateListState().collectAsState(initial = emptyList())
    val taskList by viewModel.bindTaskListState().collectAsState(initial = mutableListOf())

    var selectDate by remember { mutableStateOf(ZonedDateTime.now()) }

    val selectEntityTaskList: List<EntityTask> by remember { mutableStateOf(emptyList()) }

    Content(selectDate, dateList, taskList, selectEntityTaskList,
        onAdd = {
            eventHandler.postNavEvent(NavEvent.Action(NavTarget.TodoAdd))
        }, onSort = {

        }, onCalendar = {
            val calendar: Calendar = Calendar.getInstance(TimeZone.getDefault()).clone() as Calendar
            if (it != null) {
                calendar.timeInMillis = it.toInstant().toEpochMilli()
            }
            val dialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    selectDate = ZonedDateTime.of(
                        year, month, dayOfMonth, 1, 1, 1, 1, ZoneId.systemDefault()
                    )
                    viewModel.getCalenderList(selectDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }, onSelectDate = {
            selectDate = it
            viewModel.getCalenderList(selectDate)
        }, onTaskItem = {

        }, onDeleteTaskItem = {
            viewModel.removeTask(it)
        }, onEditTaskItem = {

        })
}

@Composable
private fun Content(
    selectDate: ZonedDateTime,
    dateList: List<ZonedDateTime>,
    entityTaskList: List<EntityTask>,
    selectEntityTaskList: List<EntityTask>,
    onAdd: () -> Unit,
    onSort: () -> Unit,
    onCalendar: (selectDate: ZonedDateTime?) -> Unit,
    onSelectDate: (selectDate: ZonedDateTime) -> Unit,
    onTaskItem: (selectEntityTask: EntityTask) -> Unit,
    onDeleteTaskItem: (selectEntityTask: EntityTask) -> Unit,
    onEditTaskItem: (selectEntityTask: EntityTask) -> Unit,
) {
    val tabItems = listOf(Tabs.Tab1, Tabs.Tab2)
    var selectedTab by remember { mutableStateOf(0) }

    val listState = rememberLazyListState()
    val tasks = if (selectedTab == TAB_1) {
        entityTaskList.filter {
            it.reminderType == 0
        }
    } else {
        entityTaskList.filter {
            it.reminderType == 1
        }
    }

    val index = dateList.indexOfDate(selectDate)
    if (index != -1) {
        val offset = -LocalConfiguration.current.screenHeightDp / 2
        LaunchedEffect(selectDate) {
            listState.animateScrollToItem(index, offset)
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.alice_blue))
    ) {
        val (topView, contentView) = createRefs()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color.White)
                .padding(horizontal = 12.dp)
                .constrainAs(topView) { top.linkTo(parent.top) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                onAdd()
            }) {
                Image(
                    painter = painterResource(R.drawable.ic_add), contentDescription = "menu"
                )
            }

            Text(
                text = stringResource(R.string.txt_todo_list),
                color = colorResource(R.color.gulf_blue),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            IconButton(onClick = {
                onSort()
            }) {
                Image(
                    painter = painterResource(R.drawable.ic_sort), contentDescription = "search"
                )
            }
        }

        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .constrainAs(contentView) {
                top.linkTo(topView.bottom)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }) {
            val (calendarView, tabViewTask, lazyColumnTask) = createRefs()

            CalendarView(
                modifier = Modifier.constrainAs(calendarView) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                },
                textTotal = String.format(
                    "%d ${stringResource(R.string.txt_task_today)}", tasks.size
                ),
                dateList = dateList,
                lazyListState = listState,
                onCalendar = onCalendar,
                onSelectDate = onSelectDate
            )

            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(tabViewTask) {
                        top.linkTo(calendarView.bottom, 16.dp)
                    },
                contentColor = colorResource(R.color.royal_blue),
                containerColor = colorResource(R.color.alice_blue)
            ) {
                tabItems.forEachIndexed { index, it ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = String.format(
                                    stringResource(it.tabName) + " (%d)",
                                    if (selectedTab == index) {
                                        tasks.size
                                    } else {
                                        tasks.size - tasks.size
                                    }
                                ), fontSize = 13.sp, color = if (selectedTab == index) {
                                    colorResource(R.color.royal_blue)
                                } else {
                                    colorResource(R.color.storm_grey)
                                }
                            )
                        },
                        selectedContentColor = colorResource(R.color.royal_blue),
                        unselectedContentColor = colorResource(R.color.storm_grey)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(lazyColumnTask) {
                        top.linkTo(tabViewTask.bottom, 16.dp)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }, verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(tasks) { _, item ->
                    ItemTask(item, onTaskItem = {
                        onTaskItem(item)
                    }, onEditTaskItem = {
                        onEditTaskItem(item)
                    }, onDeleteTaskItem = {
                        onDeleteTaskItem(item)
                    }, checked = selectEntityTaskList.contains(item))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemTask(
    entityTask: EntityTask,
    checked: Boolean,
    onTaskItem: () -> Unit,
    onEditTaskItem: () -> Unit,
    onDeleteTaskItem: () -> Unit,
) {
    var isChecked by remember { mutableStateOf(checked) }

    val swipeState: RevealState = rememberRevealState()
    var isResetState by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isResetState) {
        if (isResetState) {
            swipeState.reset()
        }
        isResetState = false
    }

    RevealSwipe(backgroundCardEndColor = Color.White,
        directions = setOf(RevealDirection.EndToStart),
        maxRevealDp = 160.dp,
        state = swipeState,
        onContentClick = { onTaskItem() },
        hiddenContentEnd = {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(160.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(colorResource(R.color.catalina_blue))
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = {
                        isResetState = true
                        onDeleteTaskItem()
                    }) {
                        Image(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = "delete"
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(colorResource(R.color.royal_blue))
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = {
                        isResetState = true
                        onEditTaskItem()
                    }) {
                        Image(
                            painter = painterResource(R.drawable.ic_edit),
                            contentDescription = "edit"
                        )
                    }
                }
            }
        }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(2.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                CircleCheckbox(selected = isChecked, modifier = Modifier) {
                    isChecked = !isChecked
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = entityTask.name,
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        color = colorResource(R.color.gulf_blue),
                        modifier = Modifier.align(alignment = Alignment.Start)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = entityTask.startDate.toHour(),
                            textAlign = TextAlign.Start,
                            color = colorResource(R.color.storm_grey),
                            fontSize = 12.sp,
                        )
                        val color = entityTask.getPriorityTask().color
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painterResource(R.drawable.ic_priority),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(
                                    colorResource(color)
                                )
                            )
                            Text(
                                text = entityTask.getPriorityTask().name,
                                textAlign = TextAlign.Start,
                                fontSize = 12.sp,
                                color = colorResource(color),
                            )
                        }

                        Text(
                            text = "8 Tasks",
                            textAlign = TextAlign.Start,
                            fontSize = 12.sp,
                            color = colorResource(R.color.royal_blue),
                            modifier = Modifier.padding(end = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CircleCheckbox(
    selected: Boolean,
    enabled: Boolean = true,
    modifier: Modifier,
    onChecked: () -> Unit,
) {

    IconButton(
        onClick = { onChecked() }, modifier = modifier, enabled = enabled
    ) {
        Image(
            painter = if (selected) {
                painterResource(R.drawable.ic_checkbox_active)
            } else {
                painterResource(R.drawable.ic_checkbox_default)
            }, contentDescription = "checkbox"
        )
    }
}

@Composable
private fun ItemCalendar(
    date: ZonedDateTime,
    select: Boolean,
    onClickDate: () -> Unit,
) {

    Card(
        colors = CardDefaults.cardColors(if (select) colorResource(R.color.royal_blue_2) else Color.White),
        modifier = Modifier
            .height(64.dp)
            .width(58.dp)
            .clickable {
                onClickDate()
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = date.dayOfMonth.toString(),
                color = if (select) Color.White else colorResource(R.color.gulf_blue),
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
            Text(
                text = date.toDayOfWeek(),
                color = if (select) Color.White else colorResource(R.color.storm_grey),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewContent() {
    Content(ZonedDateTime.now(), emptyList(), emptyList(), emptyList(), {}, {}, {}, {}, {}, {}, {})
}