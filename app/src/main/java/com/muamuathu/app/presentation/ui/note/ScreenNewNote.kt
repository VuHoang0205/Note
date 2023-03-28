package com.muamuathu.app.presentation.ui.note

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.activity.ComponentActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
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
import androidx.lifecycle.Lifecycle
import com.muamuathu.app.R
import com.muamuathu.app.domain.model.Action
import com.muamuathu.app.domain.model.Folder
import com.muamuathu.app.domain.model.Note
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.extensions.formatFromPattern
import com.muamuathu.app.presentation.graph.NavTarget
import com.muamuathu.app.presentation.helper.OnLifecycleEvent
import com.muamuathu.app.presentation.ui.note.viewModel.NoteViewModel
import java.util.*

@Composable
fun ScreenNewNote() {

    val eventHandler = initEventHandler()
    val context = LocalContext.current as ComponentActivity

    val viewModel = hiltViewModel<NoteViewModel>(context)

    val calendar: Calendar by remember {
        mutableStateOf(Calendar.getInstance(TimeZone.getDefault()).clone() as Calendar)
    }

    var dateTime by remember { mutableStateOf(calendar.timeInMillis) }

    val monthString by remember {
        derivedStateOf {
            dateTime.formatFromPattern("dd MMMM")
        }
    }
    val yearString by remember {
        derivedStateOf {
            dateTime.formatFromPattern("yyyy, EEEE")
        }
    }
    val timeString by remember {
        derivedStateOf {
            dateTime.formatFromPattern("hh:mm a")
        }
    }

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                viewModel.clearReference()
            }
            else -> {}
        }
    }

    Content(
        monthString, yearString, timeString,
        onInputTitle = {},
        onInputContent = {},
        folder = viewModel.folder,
        onClose = {
            eventHandler.postNavEvent(NavEvent.PopBackStack(false))
        },
        onSave = {
            // TODO: Save
        },
        onCalendar = {
            val dialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    dateTime = calendar.timeInMillis
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }, onChooseFolder = {
            eventHandler.postNavEvent(NavEvent.Action(NavTarget.FolderChoose))
        }, onTimePicker = {
            val dialog = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    dateTime = calendar.timeInMillis
                }, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            )
            dialog.show()
        },
        onActionClick = {
            when (it) {
                Action.OpenCamera -> {
                    eventHandler.postNavEvent(NavEvent.Action(NavTarget.NoteCaptureImage))
                }
                Action.OpenGallery -> {
                    eventHandler.postNavEvent(NavEvent.Action(NavTarget.NoteAddImage))
                }
                Action.AddTag -> {
                    eventHandler.postNavEvent(NavEvent.Action(NavTarget.NoteAddTags))
                }
//                Action.AddAudio -> {}
//                Action.OpenVideo -> {
//                    eventHandler.postNavEvent(NavEvent.Action(NavTarget.NoteAddVideo))
//                }
                Action.FileManager -> {
                    eventHandler.postNavEvent(NavEvent.Action(NavTarget.NotePickImage))
                }
                Action.DrawSketch -> {
                    eventHandler.postNavEvent(NavEvent.Action(NavTarget.NoteDrawSketch))
                }
            }
        }
    )
}

@Composable
private fun Content(
    monthString: String,
    yearString: String,
    timeString: String,
    onInputTitle: (title: String) -> Unit,
    onInputContent: (content: String) -> Unit,
    folder: Folder,
    onClose: () -> Unit,
    onSave: () -> Unit,
    onCalendar: () -> Unit,
    onChooseFolder: () -> Unit,
    onTimePicker: () -> Unit,
    onActionClick: (action: Action) -> Unit,
) {

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var note by remember { mutableStateOf(Note()) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.alice_blue))
    ) {
        val (topView, contentView, lazyRowBottom) = createRefs()

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
                onClose()
            }) {
                Image(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "close"
                )
            }

            Text(
                text = stringResource(R.string.txt_add_new_journal),
                color = colorResource(R.color.gulf_blue),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            IconButton(onClick = {
                onSave()
            }) {
                Image(
                    painter = painterResource(R.drawable.ic_save),
                    contentDescription = "save"
                )
            }
        }

        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .constrainAs(contentView) {
                top.linkTo(topView.bottom)
                bottom.linkTo(lazyRowBottom.top)
                height = Dimension.fillToConstraints
            }) {
            val (columnDate, imgCalendar, imgClock, textTime, divider1, rowFolder, divider2, textTitle, textContent, textAttachment, lazyRowAttachMent, columnTag) = createRefs()

            Column(modifier = Modifier
                .constrainAs(columnDate) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 16.dp)
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
                    shape = CircleShape,
                    color = colorResource(R.color.royal_blue)
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

            Text(
                text = timeString,
                color = colorResource(R.color.gulf_blue),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.constrainAs(textTime) {
                    top.linkTo(columnDate.top)
                    bottom.linkTo(columnDate.bottom)
                    end.linkTo(imgClock.start)
                }
            )

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
                        .padding(top = 16.dp, start = 16.dp, bottom = 16.dp)
                        .constrainAs(icFolder) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        })

                Text(
                    text = folder.name.ifEmpty { stringResource(R.string.txt_choose_folder) },
                    color = colorResource(if (folder.name.isEmpty()) R.color.storm_grey else R.color.gulf_blue),
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
                        stringResource(R.string.txt_title),
                        color = colorResource(R.color.gulf_blue)
                    )
                },
                onValueChange = {
                    title = it
                    onInputTitle(title)
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = colorResource(R.color.gulf_blue)
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
                    backgroundColor = Color.Transparent,
                    cursorColor = MaterialTheme.colors.onSurface,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledTextColor = Color.Transparent,
                ),
                singleLine = true,
            )

            val maxChar = 200
            TextField(
                value = "Chỉ số trung bình công nghiệp Dow Jones hay Chỉ số bình quân công nghiệp Dow Jones (tiếng Anh: Dow Jones Industrial Average, viết tắt DJIA, còn gọi Dow 30, Dow Jones công nghiệp, hoặc Dow Jones; NYSE: DJI Lưu trữ 2008-10-26 tại Wayback Machine) là một trong vài chỉ số thị trường chứng khoán được tạo ra bởi Charles Dow, chủ báo The Wall Street Journal và đồng sáng lập viên của công ty Dow Jones & Company vào thế kỷ 19. Dow tập hợp chỉ số này để đánh giá khu vực công nghiệp của thị trường chứng khoán tại Hoa Kỳ. Nó là chỉ số Mỹ lâu đời thứ hai, chỉ sau Chỉ số Trung bình Vận tải Dow Jones, cũng do Dow tạo ra.\n",
                placeholder = { Text(stringResource(R.string.txt_write_more_here)) },
                onValueChange = {
                    if (it.length < maxChar) {
                        content = it
                        onInputContent(content)
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
                    backgroundColor = Color.Transparent,
                    cursorColor = MaterialTheme.colors.onSurface,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledTextColor = Color.Transparent,
                ),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = colorResource(R.color.storm_grey)
                ),
            )

            val limitItem: Int =
                (((LocalConfiguration.current.screenWidthDp) / 70) - 0.5f).toInt()
            val redundantItem = note.attachments.size - limitItem
            val redundantItemString = if (note.attachments.size > limitItem) {
                String.format(
                    "%s (%d)",
                    stringResource(R.string.txt_attachments),
                    redundantItem
                )
            } else {
                stringResource(R.string.txt_attachments)
            }
            Text(text = redundantItemString,
                color = colorResource(R.color.gulf_blue),
                fontSize = 17.sp,
                modifier = Modifier.constrainAs(textAttachment) {
                    top.linkTo(textContent.bottom, 12.dp)
                    start.linkTo(parent.start, 16.dp)
                }
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(lazyRowAttachMent) {
                        top.linkTo(textAttachment.bottom, 12.dp)
                        start.linkTo(topView.start)
                    }, horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                itemsIndexed(note.attachments.take(limitItem)) { index, path ->
                    AttachmentsItem(
                        path,
                        index == limitItem - 1,
                        redundantItem
                    ) { }
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(columnTag) {
                        top.linkTo(lazyRowAttachMent.bottom, 12.dp)
                        start.linkTo(topView.start)
                    }, elevation = 4.dp,
                backgroundColor = Color.White,
                shape = MaterialTheme.shapes.medium.copy(CornerSize(8.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.txt_tags),
                        color = colorResource(R.color.gulf_blue),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )

                    val strs = "tag,TAG".split(",").toTypedArray()
                    LazyRow(
                        modifier = Modifier
                            .padding(
                                top = 4.dp,
                                start = 8.dp,
                                end = 8.dp,
                                bottom = 10.dp
                            )
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(strs) {
                            TextButton(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = colorResource(
                                        R.color.royal_blue_2
                                    )
                                ),
                                shape = RoundedCornerShape(100.dp),
                                modifier = Modifier.height(25.dp),
                                contentPadding = PaddingValues(3.dp)
                            ) {
                                Text(it, color = Color.White, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .background(
                    shape = RoundedCornerShape(topStartPercent = 8, bottomStartPercent = 8),
                    color = Color.White
                )
                .constrainAs(lazyRowBottom) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start, 60.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(Action.values()) {
                IconButton(onClick = {
                    onActionClick(it)
                }, modifier = Modifier.padding(6.dp)) {
                    Image(painterResource(it.resource), contentDescription = null)
                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewContent() {
    Content("", "", "", {}, {}, Folder(), {}, {}, {}, {}, {}, {})
}