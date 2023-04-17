package com.muamuathu.app.presentation.ui.note

import android.app.DatePickerDialog
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.muamuathu.app.R
import com.muamuathu.app.domain.model.Folder
import com.muamuathu.app.domain.model.NoteAction
import com.muamuathu.app.domain.model.Tag
import com.muamuathu.app.presentation.common.DateTimeView
import com.muamuathu.app.presentation.common.FolderSelectView
import com.muamuathu.app.presentation.common.ItemTagNote
import com.muamuathu.app.presentation.components.topbar.TopBarBase
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.extensions.formatFromPattern
import com.muamuathu.app.presentation.graph.NavTarget
import com.muamuathu.app.presentation.helper.observeResultFlow
import com.muamuathu.app.presentation.ui.note.viewModel.AddNoteViewModel
import java.util.*

@Composable
fun ScreenNewNote() {

    val eventHandler = initEventHandler()
    val context = LocalContext.current as ComponentActivity

    val viewModel = hiltViewModel<AddNoteViewModel>(context)
    val coroutineScope = rememberCoroutineScope()

    val calendar: Calendar by remember {
        mutableStateOf(Calendar.getInstance(TimeZone.getDefault()).clone() as Calendar)
    }
    val dateTime by viewModel.dateTime.collectAsState()
    val title by viewModel.title.collectAsState()
    val content by viewModel.content.collectAsState()
    val attachments by viewModel.attachments.collectAsState()
    val tag by viewModel.tags.collectAsState()
    val folder by viewModel.folder.collectAsState()
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

    Content(folder = folder,
        title = title,
        content = content,
        attachments = attachments.list,
        tags = tag.list,
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
        onActionClick = {
            when (it) {
                NoteAction.OpenCamera -> {
                    eventHandler.postNavEvent(NavEvent.Action(NavTarget.NoteCaptureImage))
                }
                NoteAction.OpenGallery -> {
                    eventHandler.postNavEvent(NavEvent.Action(NavTarget.NoteAddImage))
                }
//                Action.AddTag -> {
//                    eventHandler.postNavEvent(NavEvent.Action(NavTarget.NoteAddTags))
//                }
//                Action.AddAudio -> {}
//                Action.OpenVideo -> {
//                    eventHandler.postNavEvent(NavEvent.Action(NavTarget.NoteAddVideo))
//                }
                NoteAction.FileManager -> {
                    eventHandler.postNavEvent(NavEvent.Action(NavTarget.NotePickImage))
                }
                NoteAction.DrawSketch -> {
                    eventHandler.postNavEvent(NavEvent.Action(NavTarget.NoteDrawSketch))
                }
            }
        }
    ) {
        eventHandler.postNavEvent(NavEvent.Action(NavTarget.NoteAddTags))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    folder: Folder,
    title: String,
    content: String,
    attachments: List<String>,
    tags: List<Tag>,
    enableSave: Boolean,
    onInputTitle: (String) -> Unit,
    onInputContent: (String) -> Unit,
    onClose: () -> Unit,
    onSave: () -> Unit,
    onCalendar: () -> Unit,
    onChooseFolder: () -> Unit,
    onActionClick: (noteAction: NoteAction) -> Unit,
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
            val (dateTimeView, folderView, textTitle, textContent, textAttachment, lazyRowAttach, columnTag) = createRefs()

            DateTimeView(modifier = Modifier
                .padding(horizontal = 16.dp)
                .constrainAs(dateTimeView) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    width = Dimension.fillToConstraints
                }, textTotal = stringResource(id = R.string.txt_add_new_journal), onCalendar = { onCalendar() })

            FolderSelectView(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(folderView) {
                    top.linkTo(dateTimeView.bottom, 16.dp)
                }, folder = folder) {
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

            if (attachments.isNotEmpty()) {
                val limitItem: Int =
                    (((LocalConfiguration.current.screenWidthDp) / 70) - 0.5f).toInt()
                val redundantItem = attachments.size - limitItem
                val redundantItemString = if (attachments.size > limitItem) {
                    String.format(
                        "%s (%d)", stringResource(R.string.txt_attachments), redundantItem
                    )
                } else {
                    stringResource(R.string.txt_attachments)
                }
                Text(text = redundantItemString,
                    color = colorResource(R.color.gulf_blue),
                    fontSize = 17.sp,
                    modifier = Modifier.constrainAs(textAttachment) {
                        top.linkTo(textContent.bottom, 12.dp)
                        start.linkTo(parent.start, 20.dp)
                    })
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(lazyRowAttach) {
                            top.linkTo(textAttachment.bottom, 12.dp)
                            start.linkTo(topView.start)
                        }, horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    itemsIndexed(attachments.take(limitItem)) { index, path ->
                        AttachmentsItem(
                            path, index == limitItem - 1, redundantItem
                        ) { }
                    }
                }
            }
            if (tags.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .constrainAs(columnTag) {
                            top.linkTo(
                                if (attachments.isEmpty()) textContent.bottom else lazyRowAttach.bottom,
                                12.dp
                            )
                            start.linkTo(parent.start, 16.dp)
                            end.linkTo(parent.end, 16.dp)
                            width = Dimension.fillToConstraints
                        },
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = MaterialTheme.shapes.medium.copy(CornerSize(8.dp))
                ) {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 70.dp)
                    ) {

                        val (textTag, lazyRow, btnAdd) = createRefs()

                        Text(
                            modifier = Modifier
                                .constrainAs(textTag) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    if (tags.isEmpty()) centerVerticallyTo(parent)
                                }
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            text = stringResource(R.string.txt_tags),
                            color = colorResource(R.color.gulf_blue),
                            fontSize = 14.sp,
                        )

                        if (tags.isNotEmpty()) {
                            LazyRow(
                                modifier = Modifier
                                    .constrainAs(lazyRow) {
                                        top.linkTo(textTag.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(btnAdd.start)
                                        width = Dimension.fillToConstraints
                                    }
                                    .padding(vertical = 8.dp, horizontal = 8.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                itemsIndexed(tags) { _, tag ->
                                    ItemTagNote(tag = tag.name)
                                }
                            }
                        }

                        IconButton(
                            modifier = Modifier
                                .constrainAs(btnAdd) {
                                    centerVerticallyTo(parent)
                                    end.linkTo(parent.end)
                                },
                            onClick = { onTageClick() }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_plus),
                                contentDescription = ""
                            )
                        }
                    }
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
            items(NoteAction.values()) {
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
    Content(
        Folder(),
        "",
        "",
        emptyList(),
        emptyList(),
        true,
        {},
        {},
        {},
        {},
        {},
        {},
        {},
    ) {}
}