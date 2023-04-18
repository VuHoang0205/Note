@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.muamuathu.app.presentation.ui.note

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.muamuathu.app.R
import com.muamuathu.app.domain.model.Note
import com.muamuathu.app.domain.model.commons.WrapList
import com.muamuathu.app.presentation.common.CalendarView
import com.muamuathu.app.presentation.common.ItemTagNote
import com.muamuathu.app.presentation.common.SearchView
import com.muamuathu.app.presentation.components.dialog.CustomDatePickerDialog
import com.muamuathu.app.presentation.components.dialog.JcDialog
import com.muamuathu.app.presentation.components.topbar.TopBarBase
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.extensions.indexOfDate
import com.muamuathu.app.presentation.extensions.toHour
import com.muamuathu.app.presentation.extensions.toZonedDateTime
import com.muamuathu.app.presentation.graph.NavTarget
import com.muamuathu.app.presentation.ui.note.viewModel.NoteViewModel
import de.charlex.compose.*
import java.time.ZonedDateTime

const val EXTRA_NOTE_ID = "noteId"

@Composable
fun ScreenNote() {

    val eventHandler = initEventHandler()
    val viewModel: NoteViewModel = hiltViewModel()

    val selectDateList by viewModel.dateListStateFlow.collectAsState(initial = emptyList())
    val noteItemList by viewModel.noteListStateFlow.collectAsState(initial = WrapList(mutableListOf()))
    var selectDate by remember { mutableStateOf(ZonedDateTime.now()) }

    Content(selectDate, selectDateList, noteItemList.list,
        onAdd = {
            eventHandler.postNavEvent(NavEvent.Action(NavTarget.NoteAdd))
        },
        onSearch = {
            viewModel.search(it)
        },
        onSearchClose = {
            viewModel.search("")
        },
        onCalendar = {
            it?.let {
                selectDate = it
                viewModel.getNoteList(it.toInstant().toEpochMilli())
            }
        },
        onSelectDate = {
            selectDate = it
        },
        onNoteItem = {
            eventHandler.postNavEvent(
                NavEvent.ActionWithValue(
                    NavTarget.NoteDetail,
                    Pair(EXTRA_NOTE_ID, it.noteId.toString())
                )
            )
        },
        onDeleteNoteItem = {
            viewModel.removeNote(it)
        },
        onEditNoteItem = {
            eventHandler.postNavEvent(
                NavEvent.ActionWithValue(
                    NavTarget.NoteDetail,
                    Pair(EXTRA_NOTE_ID, it.noteId.toString())
                )
            )
        })
}

@Composable
private fun Content(
    selectDate: ZonedDateTime,
    dateList: List<ZonedDateTime>,
    noteItemList: List<Note>,
    onAdd: () -> Unit,
    onSearch: (String) -> Unit,
    onSearchClose: () -> Unit,
    onCalendar: (selectDate: ZonedDateTime?) -> Unit,
    onSelectDate: (selectDate: ZonedDateTime) -> Unit,
    onNoteItem: (note: Note) -> Unit,
    onDeleteNoteItem: (note: Note) -> Unit,
    onEditNoteItem: (note: Note) -> Unit,
) {

    var showDeleteNoteDialog by remember { mutableStateOf(false) }
    var note: Note? by remember { mutableStateOf(null) }
    var enableSearchMode by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    var query by remember { mutableStateOf("") }
    var isShowDatePicker by remember { mutableStateOf(false) }

    val index = dateList.indexOfDate(selectDate)
    if (index != -1) {
        val offset = -LocalConfiguration.current.screenHeightDp / 2
        LaunchedEffect(selectDate) {
            listState.animateScrollToItem(index, offset)
        }
    }

    BackHandler(enableSearchMode) {
        if (enableSearchMode) enableSearchMode = false
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.alice_blue))
    ) {
        val (topView, contentView) = createRefs()

        TopBarBase(modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .constrainAs(topView) { top.linkTo(parent.top) },
            titleAlign = TextAlign.Center,
            title = stringResource(R.string.txt_pronto_journal),
            navigationIcon = {
                IconButton(onClick = {
                    onAdd()
                }) {
                    Image(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = null
                    )
                }
            }, listRightIcon = if (enableSearchMode) null else listOf(Triple({
                Image(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "search"
                )
            }, { enableSearchMode = true }, null))
        )

        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .constrainAs(contentView) {
                top.linkTo(topView.bottom)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            }) {
            val (calendarView, searchView, lazyColumnNote) = createRefs()

            if (enableSearchMode) {
                SearchView(modifier = Modifier.constrainAs(searchView) {
                    top.linkTo(parent.top, 16.dp)
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                },
                    label = R.string.txt_search_note_name,
                    query = query,
                    onSearch = {
                        query = it
                        onSearch(it)
                    },
                    onClose = {
                        enableSearchMode = false
                        onSearchClose()
                    })
            } else {
                CalendarView(
                    modifier = Modifier.constrainAs(calendarView) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        width = Dimension.fillToConstraints
                    },
                    textTotal = String.format(
                        "%d ${stringResource(R.string.txt_journals_today)}",
                        noteItemList.size
                    ),
                    dateList = dateList,
                    lazyListState = listState,
                    onCalendar = { isShowDatePicker = true },
                    onSelectDate = onSelectDate
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(lazyColumnNote) {
                        if (enableSearchMode) {
                            top.linkTo(searchView.bottom, 16.dp)
                        } else {
                            top.linkTo(calendarView.bottom, 16.dp)
                        }
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }, verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(noteItemList) { _, item ->
                    ItemNote(item,
                        { onNoteItem(item) },
                        {
                            note = item
                            showDeleteNoteDialog = true
                        },
                        { onEditNoteItem(item) })
                }
            }

            if (showDeleteNoteDialog) {
                JcDialog(title = stringResource(id = R.string.confirm_delete),
                    message = stringResource(id = R.string.message_delete_note),
                    negativeButtonText = stringResource(id = R.string.cancel),
                    positiveButtonText = stringResource(id = R.string.ok),
                    onNegativeClicked = { showDeleteNoteDialog = false },
                    onPositiveClicked = {
                        note?.let {
                            showDeleteNoteDialog = false
                            onDeleteNoteItem(it)
                        }
                    }) {
                }
            }

            if (isShowDatePicker) {
                CustomDatePickerDialog(isShow = isShowDatePicker, onDismissRequest = { isShowDatePicker = false }, onDateChange = {
                    isShowDatePicker = false
                    onCalendar(it.toZonedDateTime())
                })
            }
        }
    }
}

@Composable
private fun ItemNote(
    note: Note,
    onNote: () -> Unit,
    onDeleteNote: () -> Unit,
    onEditNote: () -> Unit,
) {

    val swipeState: RevealState = rememberRevealState()
    var isResetState by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isResetState) {
        if (isResetState) {
            swipeState.resetFast()
        }
        isResetState = false
    }

    RevealSwipe(
        backgroundCardEndColor = Color.White,
        directions = setOf(RevealDirection.EndToStart),
        maxRevealDp = 160.dp,
        state = swipeState,
        onContentClick = { onNote() },
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
                        .weight(1f), contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = {
                        isResetState = true
                        onDeleteNote()
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
                        .weight(1f), contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = {
                        isResetState = true
                        onEditNote()
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
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (contentView1, viewLine1) = createRefs()
                ConstraintLayout(modifier = Modifier
                    .padding(vertical = 8.dp)
                    .constrainAs(contentView1) {
                        start.linkTo(viewLine1.start, 16.dp)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }) {
                    val (contentView, viewLine, textImage, textFolder, textTime) = createRefs()

                    ConstraintLayout(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .constrainAs(contentView) {
                            top.linkTo(parent.top)
                        }) {
                        val (imgAvatar, textTitle, textContent, lazyRowCategoryNote) = createRefs()
                        if (note.attachments.isNotEmpty()) {
                            AsyncImage(model = note.attachments[0],
                                contentDescription = "avatar",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .constrainAs(imgAvatar) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                    })
                        }

                        Text(
                            text = note.title,
                            color = colorResource(R.color.gulf_blue),
                            fontSize = 16.sp,
                            maxLines = 1,
                            modifier = Modifier.constrainAs(textTitle) {
                                top.linkTo(parent.top)
                                start.linkTo(imgAvatar.end, 8.dp)
                            }
                        )

                        Text(
                            text = note.content,
                            color = colorResource(R.color.storm_grey),
                            fontSize = 11.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .constrainAs(textContent) {
                                    top.linkTo(textTitle.bottom, 8.dp)
                                    start.linkTo(textTitle.start)
                                    end.linkTo(parent.end, 8.dp)
                                    width = Dimension.fillToConstraints
                                }
                        )
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .constrainAs(lazyRowCategoryNote) {
                                    top.linkTo(textContent.bottom, 8.dp)
                                    start.linkTo(textTitle.start)
                                }, horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(note.tags) {
                                ItemTagNote(tag = it.name)
                            }
                        }
                    }

                    Divider(modifier = Modifier
                        .fillMaxWidth()
                        .alpha(0.2f)
                        .height(0.5.dp)
                        .background(colorResource(R.color.gulf_blue))
                        .constrainAs(viewLine) {
                            top.linkTo(contentView.bottom)
                        })

                    Row(modifier = Modifier.constrainAs(textImage) {
                        top.linkTo(viewLine.bottom, 10.dp)
                        start.linkTo(contentView.start, 8.dp)
                    }) {

                        Image(
                            painter = painterResource(R.drawable.ic_image_default),
                            contentDescription = "image",
                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
                        )

                        Text(
                            text = note.attachments.size.toString() + " Images",
                            color = colorResource(R.color.gulf_blue),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    Row(modifier = Modifier.constrainAs(textFolder) {
                        top.linkTo(textImage.top)
                        bottom.linkTo(textImage.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {

                        Image(
                            painter = painterResource(R.drawable.ic_folder),
                            contentDescription = "folder",
                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
                        )

                        Text(
                            text = note.folder.name,
                            color = colorResource(R.color.gulf_blue),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }

                    Row(modifier = Modifier.constrainAs(textTime) {
                        top.linkTo(textImage.top)
                        bottom.linkTo(textImage.bottom)
                        end.linkTo(parent.end, 8.dp)
                    }) {

                        Image(
                            painter = painterResource(R.drawable.ic_clock),
                            contentDescription = "clock",
                            modifier = Modifier.align(alignment = Alignment.CenterVertically)
                        )

                        Text(
                            text = note.dateTime.toHour(),
                            color = colorResource(R.color.gulf_blue),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
                if (note.attachments.isEmpty()) {
                    Divider(modifier = Modifier
                        .width(8.dp)
                        .clip(
                            shape = RoundedCornerShape(
                                topStartPercent = 8,
                                bottomStartPercent = 8
                            )
                        )
                        .constrainAs(viewLine1) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.fillToConstraints
                        }, color = Color(note.folder.color)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewContent() {

    Content(ZonedDateTime.now(),
        emptyList(),
        emptyList(),
        {},
        {},
        {},
        {},
        {},
        {},
        {},
        {})
}