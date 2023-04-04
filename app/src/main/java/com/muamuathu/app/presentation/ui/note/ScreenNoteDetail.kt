package com.muamuathu.app.presentation.ui.note

import android.os.SystemClock
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jakewharton.threetenabp.AndroidThreeTen
import com.muamuathu.app.R
import com.muamuathu.app.domain.model.Note
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.extensions.*
import com.muamuathu.app.presentation.helper.observeResultFlow
import com.muamuathu.app.presentation.ui.note.viewModel.NoteViewModel
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

@Composable
fun ScreenNoteDetail(idNote: String) {

    val viewModel: NoteViewModel = hiltViewModel()
    val eventHandler = initEventHandler()
    val coroutineScope = rememberCoroutineScope()
    var note: Note by remember {
        mutableStateOf(Note())
    }
    LaunchedEffect(key1 = Unit, block = {
        coroutineScope.observeResultFlow(
            viewModel.getNoteById(idNote.toLong()),
            successHandler = { note = it })
    })

    Content(note, onBack = {
        eventHandler.postNavEvent(NavEvent.PopBackStack(false))
    }, onMoreSetting = {

    }, onPlayAudio = {

    }, onAttachments = {

    })
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun Content(
    note: Note?,
    onBack: () -> Unit,
    onMoreSetting: () -> Unit,
    onPlayAudio: () -> Unit,
    onAttachments: () -> Unit,
) {
    note?.apply {
        val pagerState = rememberPagerState(0)
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(colorResource(R.color.alice_blue))
        ) {

            val (topView, pagerAvatar, pagerIndicator, contentView) = createRefs()

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
                    onBack()
                }) {
                    Image(
                        painter = painterResource(R.drawable.ic_back), contentDescription = "back"
                    )
                }

                Text(
                    text = stringResource(R.string.txt_details),
                    color = colorResource(R.color.gulf_blue),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )

                IconButton(onClick = {
                    onMoreSetting()
                }) {
                    Image(
                        painter = painterResource(R.drawable.ic_more),
                        contentDescription = "more setting"
                    )
                }
            }

            HorizontalPager(state = pagerState,
                count = attachments.size,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(pagerAvatar) {
                        top.linkTo(topView.bottom)
                    }) { page ->
                Image(
                    painter = rememberAsyncImagePainter(attachments[page]),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                )
            }
            PagerIndicator(attachments.size,
                pagerState.currentPage,
                modifier = Modifier.constrainAs(pagerIndicator) {
                    bottom.linkTo(pagerAvatar.bottom, 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            ConstraintLayout(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .constrainAs(contentView) {
                    top.linkTo(pagerAvatar.bottom, 10.dp)
                }) {
                val (ctlTime, rowTag, textTitle, textContent, audioView, tagView, textAttachment, lazyRowAttachMent) = createRefs()

                ConstraintLayout(modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(ctlTime) {
                        top.linkTo(parent.top)
                    }) {
                    val (textDay, columnDate, columnTime) = createRefs()
                    Text(text = ZonedDateTime.ofInstant(
                        Instant.ofEpochMilli(note.dateTime), ZoneId.systemDefault()
                    ).toDayOfMonth(),
                        color = colorResource(R.color.royal_blue_2),
                        fontSize = 34.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.constrainAs(textDay) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        })

                    Column(
                        modifier = Modifier.constrainAs(columnDate) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(textDay.end, 4.dp)
                        },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dateTime.toMonth(),
                            color = colorResource(R.color.catalina_blue),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                        )

                        Text(
                            text = dateTime.toYearValue().toString(),
                            color = colorResource(R.color.catalina_blue),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                        )
                    }

                    Column(
                        modifier = Modifier.constrainAs(columnTime) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                        },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dateTime.toDayOfWeekDetail(),
                            color = colorResource(R.color.storm_grey),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                        )

                        Text(
                            text = dateTime.toHour(),
                            color = colorResource(R.color.storm_grey),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                Row(modifier = Modifier.constrainAs(rowTag) {
                    top.linkTo(ctlTime.bottom)
                    start.linkTo(parent.start)
                }, verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.ic_folder),
                        contentDescription = "icon",
                    )

                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(Color.Blue)
                    )
                    Text(
                        text = "tag",
                        color = colorResource(R.color.gulf_blue),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Text(text = title,
                    color = colorResource(R.color.gulf_blue),
                    fontSize = 20.sp,
                    modifier = Modifier.constrainAs(textTitle) {
                        top.linkTo(rowTag.bottom, 12.dp)
                        start.linkTo(parent.start)
                    })

                ExpandedText(text = content.substring(0, note.content.length / 2),
                    expandedText = content,
                    expandedTextButton = stringResource(R.string.txt_read_more),
                    shrinkTextButton = stringResource(R.string.txt_read_less),
                    expandedTextButtonStyle = TextStyle(colorResource(R.color.royal_blue)),
                    shrinkTextButtonStyle = TextStyle(colorResource(R.color.royal_blue)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(textContent) {
                            top.linkTo(textTitle.bottom)
                            start.linkTo(parent.start)
                        })

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(audioView) {
                            top.linkTo(textContent.bottom, 8.dp)
                            start.linkTo(parent.start)
                        },
                    elevation = 4.dp,
                    backgroundColor = Color.White,
                    shape = MaterialTheme.shapes.medium.copy(CornerSize(8.dp))
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                                .align(alignment = Alignment.CenterStart)
                        ) {
                            Text(
                                text = "Audio",
                                color = colorResource(R.color.gulf_blue),
                                fontSize = 14.sp,
                            )
                            Row {
                                Image(
                                    painter = painterResource(R.drawable.ic_music),
                                    contentDescription = "icon",
                                )

                                Divider(modifier = Modifier.size(3.dp))

                                Text(
                                    text = "Ed Sherran",
                                    color = colorResource(R.color.gulf_blue),
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                        }

                        TextButton(
                            onClick = {
                                onPlayAudio()
                            },
                            modifier = Modifier
                                .align(alignment = Alignment.CenterEnd)
                                .padding(8.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_play),
                                contentDescription = "down",
                                modifier = Modifier.align(alignment = Alignment.CenterVertically)
                            )
                            Text(
                                text = stringResource(R.string.txt_play),
                                color = colorResource(R.color.gulf_blue),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(tagView) {
                            top.linkTo(audioView.bottom, 16.dp)
                            start.linkTo(parent.start)
                        },
                    elevation = 4.dp,
                    backgroundColor = Color.White,
                    shape = MaterialTheme.shapes.medium.copy(CornerSize(8.dp))
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {

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
                                    top = 4.dp, start = 8.dp, end = 8.dp, bottom = 10.dp
                                )
                                .fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                    fontSize = 16.sp,
                    modifier = Modifier.constrainAs(textAttachment) {
                        top.linkTo(tagView.bottom, 12.dp)
                        start.linkTo(parent.start)
                    })
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(lazyRowAttachMent) {
                            top.linkTo(textAttachment.bottom, 12.dp)
                            start.linkTo(topView.start)
                        }, horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    itemsIndexed(attachments.take(limitItem)) { index, path ->
                        AttachmentsItem(
                            path, index == limitItem - 1, redundantItem
                        ) { onAttachments() }
                    }
                }
            }
        }
    }
}

@Composable
fun AttachmentsItem(
    url: String,
    isLastItem: Boolean,
    redundantItem: Int,
    onAttachments: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(70.dp)
            .clickable {
                onAttachments()
            }, contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(url),
            contentDescription = "attachments",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .alpha(if (isLastItem) 0.5f else 1f),
            contentScale = ContentScale.Crop
        )
        if (isLastItem) {
            Text(
                text = String.format("+%s", redundantItem),
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun PagerIndicator(totalDots: Int, currentPage: Int, modifier: Modifier) {
    LazyRow(
        modifier = modifier
    ) {
        items(totalDots) { index ->
            if (index == currentPage) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .alpha(0.5f)
                )
            }
        }
    }
}

@Composable
private fun ExpandedText(
    text: String,
    expandedText: String,
    expandedTextButton: String,
    shrinkTextButton: String = "",
    modifier: Modifier,
    softWrap: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    expandedTextStyle: TextStyle = LocalTextStyle.current,
    expandedTextButtonStyle: TextStyle = LocalTextStyle.current,
    shrinkTextButtonStyle: TextStyle = LocalTextStyle.current,
) {
    val isEnableExpanded by remember { mutableStateOf(expandedText.length > 50) }

    if (isEnableExpanded) {
        var isExpanded by remember { mutableStateOf(false) }

        val textHandler =
            "${if (isExpanded) expandedText else text} ${if (isExpanded) shrinkTextButton else expandedTextButton}"

        val annotatedString = buildAnnotatedString {
            withStyle(
                if (isExpanded) expandedTextStyle.toSpanStyle() else textStyle.toSpanStyle()
            ) {
                append(if (isExpanded) expandedText else text)
            }

            append("")

            withStyle(
                if (isExpanded) shrinkTextButtonStyle.toSpanStyle() else expandedTextButtonStyle.toSpanStyle()
            ) {
                append(if (isExpanded) shrinkTextButton else expandedTextButton)
            }

            addStringAnnotation(
                tag = "expand_shrink_text_button",
                annotation = if (isExpanded) shrinkTextButton else expandedTextButton,
                start = textHandler.indexOf(if (isExpanded) shrinkTextButton else expandedTextButton),
                end = textHandler.indexOf(if (isExpanded) shrinkTextButton else expandedTextButton) + if (isExpanded) expandedTextButton.length else shrinkTextButton.length
            )
        }

        ClickableText(text = annotatedString, softWrap = softWrap, modifier = modifier, onClick = {
            annotatedString.getStringAnnotations(
                "expand_shrink_text_button", it, it
            ).firstOrNull()?.let { stringAnnotation ->
                isExpanded = stringAnnotation.item == expandedTextButton
            }
        })
    } else {
        Text(
            text = expandedText,
            color = colorResource(R.color.storm_grey),
            fontSize = 12.sp,
            modifier = modifier
        )
    }
}

@Preview
@Composable
private fun PreviewContent() {
    AndroidThreeTen.init(LocalContext.current)
    Content(Note(
        1,
        "Fun day with Friends $1",
        "Come on, people now Smile on your bro everybody get together to try new Come on, people now Smile on your bro everybody get together to try new Come on, people now Smile on your bro everybody get together to try new...",
        SystemClock.currentThreadTimeMillis(),
        attachments = listOf(
            "https://kenh14cdn.com/thumb_w/660/2020/7/17/brvn-15950048783381206275371.jpg",
            "https://kenh14cdn.com/thumb_w/660/2020/7/17/brvn-15950048783381206275371.jpg",
            "https://kenh14cdn.com/thumb_w/660/2020/7/17/brvn-15950048783381206275371.jpg"
        )
    ), {}, {}, {}, {})
}