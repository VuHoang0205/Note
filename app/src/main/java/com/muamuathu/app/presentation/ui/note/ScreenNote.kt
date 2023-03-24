package com.muamuathu.app.presentation.ui.note

import android.app.DatePickerDialog
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
import com.jakewharton.threetenabp.AndroidThreeTen
import com.muamuathu.app.R
import com.muamuathu.app.data.entity.EntityNote
import com.muamuathu.app.presentation.event.BottomSheetEvent
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.extensions.*
import com.muamuathu.app.presentation.graph.NavTarget

import com.muamuathu.app.presentation.ui.note.viewModel.NoteViewModel
import de.charlex.compose.RevealDirection
import de.charlex.compose.RevealSwipe
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import java.util.*

const val EXTRA_NOTE_ID = "noteId"

@Composable
fun ScreenNote() {

    val eventHandler = initEventHandler()
    val context = LocalContext.current
    val viewModel: NoteViewModel = hiltViewModel(context as ComponentActivity)

    val selectDateList by viewModel.bindDateListState().collectAsState(initial = emptyList())
    val noteItemList by viewModel.bindNoteListState().collectAsState(initial = mutableListOf())

    var selectDate by remember { mutableStateOf(ZonedDateTime.now()) }

    Content(selectDate, selectDateList, noteItemList,
        onAdd = {
            eventHandler.postBottomSheetEvent(BottomSheetEvent.Custom { ScreenNewNote() })
        }, onSearch = {

        }, onCalendar = {
            val calendar: Calendar = Calendar.getInstance(TimeZone.getDefault()).clone() as Calendar
            if (it != null) {
                calendar.timeInMillis = it.toInstant().toEpochMilli()
            }
            val dialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    selectDate = ZonedDateTime.of(
                        year,
                        month,
                        dayOfMonth,
                        1,
                        1,
                        1,
                        1,
                        ZoneId.systemDefault()
                    )
                    viewModel.getCalenderList(selectDate)
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }, onSelectDate = {
            selectDate = it
        }, onNoteItem = {
            eventHandler.postNavEvent(
                NavEvent.ActionWithValue(
                    NavTarget.NoteDetail,
                    Pair(EXTRA_NOTE_ID, it.noteId.toString())
                )
            )
        }, onDeleteNoteItem = {
            viewModel.removeNote(it)
        }, onEditNoteItem = {

        })
}

@Composable
private fun Content(
    selectDate: ZonedDateTime,
    dateList: List<ZonedDateTime>,
    entityNoteItemList: List<EntityNote>,
    onAdd: () -> Unit,
    onSearch: () -> Unit,
    onCalendar: (selectDate: ZonedDateTime?) -> Unit,
    onSelectDate: (selectDate: ZonedDateTime) -> Unit,
    onNoteItem: (entityNote: EntityNote) -> Unit,
    onDeleteNoteItem: (entityNote: EntityNote) -> Unit,
    onEditNoteItem: (entityNote: EntityNote) -> Unit,
) {

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
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "menu"
                )
            }

            Text(
                text = stringResource(R.string.txt_pronto_journal),
                color = colorResource(R.color.gulf_blue),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            IconButton(onClick = {
                onSearch()
            }) {
                Image(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = "search"
                )
            }
        }

        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp, top = 8.dp)
            .constrainAs(contentView) {
                top.linkTo(topView.bottom)
            }) {
            val (textDate, textTotalJournal, icCalendar, textYear, lazyRowCalendar, viewLine, lazyColumnNote) = createRefs()

            val dateString = selectDate.toDayOfMonth()
            TextButton(onClick = {
                onCalendar(selectDate)
            }, modifier = Modifier.constrainAs(textDate) {
                start.linkTo(parent.start, 4.dp)
                top.linkTo(parent.top)
            }) {
                Text(
                    text = dateString,
                    color = colorResource(R.color.gulf_blue),
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center,
                )
            }

            Text(
                text = String.format(
                    "%d ${stringResource(R.string.txt_journals_today)}",
                    entityNoteItemList.size
                ),
                color = colorResource(R.color.storm_grey),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.constrainAs(textTotalJournal) {
                    start.linkTo(textDate.start)
                    top.linkTo(textDate.bottom, 8.dp)
                }
            )

            IconButton(onClick = {
                onCalendar(selectDate)
            }, modifier = Modifier
                .size(33.dp)
                .background(
                    shape = CircleShape,
                    color = colorResource(R.color.catalina_blue)
                )
                .constrainAs(icCalendar) {
                    top.linkTo(textDate.top)
                    end.linkTo(parent.end, 6.dp)
                    bottom.linkTo(textTotalJournal.bottom)
                }) {
                Image(
                    painter = painterResource(R.drawable.ic_calendar),
                    contentDescription = "search"
                )
            }

            TextButton(
                onClick = {
                    onCalendar(selectDate)
                },
                modifier = Modifier.constrainAs(textYear) {
                    top.linkTo(icCalendar.top)
                    bottom.linkTo(icCalendar.bottom)
                    end.linkTo(icCalendar.start, 8.dp)
                },
            ) {
                Text(
                    text = selectDate.year.toString(),
                    color = colorResource(id = R.color.gulf_blue),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                )

                Image(
                    painter = painterResource(R.drawable.ic_down),
                    contentDescription = "down",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
            }

            val listState = rememberLazyListState()
            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(lazyRowCalendar) {
                        top.linkTo(textTotalJournal.bottom, 4.dp)
                    },
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(dateList) { _, item ->
                    ItemCalendar(date = item, select = item.isSameDay(selectDate)) {
                        onSelectDate(item)
                    }
                }
            }

            val index = dateList.indexOfDate(selectDate)
            if (index != -1) {
                val offset = -LocalConfiguration.current.screenHeightDp / 2
                LaunchedEffect(selectDate) {
                    listState.animateScrollToItem(index, offset)
                }
            }

            Image(painterResource(R.drawable.ic_bg_line_note_tab),
                contentDescription = "divider",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
                    .constrainAs(viewLine) {
                        top.linkTo(lazyRowCalendar.bottom)
                    })

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(lazyColumnNote) {
                        top.linkTo(viewLine.bottom)
                    }, verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                itemsIndexed(entityNoteItemList) { _, item ->
                    ItemNote(item,
                        { onNoteItem(item) },
                        { onDeleteNoteItem(item) },
                        { onEditNoteItem(item) })
                }
            }
        }
    }
}


@Composable
private fun ItemCalendar(
    date: ZonedDateTime,
    select: Boolean,
    onClickDate: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = if (select) colorResource(R.color.royal_blue_2) else Color.White),
        modifier = Modifier
            .height(64.dp)
            .width(58.dp)
            .clickable {
                onClickDate()
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ItemNote(
    entityNote: EntityNote,
    onNote: () -> Unit,
    onDeleteNote: () -> Unit,
    onEditNote: () -> Unit,
) {
    RevealSwipe(
        backgroundCardEndColor = Color.White,
        directions = setOf(RevealDirection.EndToStart),
        maxRevealDp = 160.dp,
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
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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

                        if (entityNote.avatar.isNotEmpty()) {
                            AsyncImage(model = entityNote.avatar,
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
                            text = entityNote.title,
                            color = colorResource(R.color.gulf_blue),
                            fontSize = 16.sp,
                            maxLines = 1,
                            modifier = Modifier.constrainAs(textTitle) {
                                top.linkTo(parent.top)
                                start.linkTo(imgAvatar.end, 8.dp)
                            }
                        )

                        Text(
                            text = entityNote.content,
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
                            val strs = entityNote.tag.split(",").toTypedArray()
                            items(strs) {
                                TextButton(
                                    onClick = {},
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(
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
                            text = "6 Images",
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
                            text = "Guys Circle",
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
                            text = entityNote.dateTime.toHour(),
                            color = colorResource(R.color.gulf_blue),
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
                if (entityNote.avatar.isEmpty()) {
                    Divider(modifier = Modifier
                        .width(8.dp)
                        .clip(
                            shape = RoundedCornerShape(
                                topStartPercent = 8,
                                bottomStartPercent = 8
                            )
                        )
                        .background(colorResource(R.color.royal_blue))
                        .constrainAs(viewLine1) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.fillToConstraints
                        })
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewContent() {
    AndroidThreeTen.init(LocalContext.current)
    Content(ZonedDateTime.now(),
        emptyList(),
        emptyList(),
        {},
        {},
        {},
        {},
        {},
        {},
        {})
}