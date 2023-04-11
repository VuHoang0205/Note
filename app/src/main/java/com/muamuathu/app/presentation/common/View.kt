package com.muamuathu.app.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.muamuathu.app.R
import com.muamuathu.app.domain.model.Tag
import com.muamuathu.app.presentation.extensions.isSameDay
import com.muamuathu.app.presentation.extensions.toDayOfMonth
import com.muamuathu.app.presentation.extensions.toDayOfWeek
import org.threeten.bp.ZonedDateTime


@Composable
fun CalendarView(
    modifier: Modifier,
    textTotal: String,
    dateList: List<ZonedDateTime>,
    lazyListState: LazyListState,
    onCalendar: (selectDate: ZonedDateTime?) -> Unit,
    onSelectDate: (selectDate: ZonedDateTime) -> Unit,
) {

    val selectDate by remember { mutableStateOf(ZonedDateTime.now()) }
    val dateString = selectDate.toDayOfMonth()

    ConstraintLayout(modifier = modifier) {
        val (textDate, textTotalJournal, icCalendar, textYear, lazyRowCalendar) = createRefs()
        TextButton(onClick = {
            onCalendar(selectDate)
        }, modifier = Modifier.constrainAs(textDate) {
            start.linkTo(parent.start)
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
            text = textTotal,
            color = colorResource(R.color.storm_grey),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(textTotalJournal) {
                start.linkTo(textDate.start,8.dp)
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
                bottom.linkTo(textTotalJournal.bottom, 8.dp)
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

        LazyRow(
            state = lazyListState,
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

//        Image(painterResource(R.drawable.ic_bg_line_note_tab),
//            contentDescription = "divider",
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 16.dp)
//                .constrainAs(viewLine) {
//                    top.linkTo(lazyRowCalendar.bottom)
//                })
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
            .height(56.dp)
            .width(42.dp)
            .clickable {
                onClickDate()
            },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
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


@ExperimentalMaterial3Api
@Composable
fun SearchView(
    modifier: Modifier,
    label: Int,
    query: String,
    onSearch: (String) -> Unit,
    onClose: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(2.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = query,
                    onValueChange = { value ->
                        onSearch(value)
                    },
                    label = {
                        Text(stringResource(label))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 14.sp),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(15.dp)
                                .size(24.dp)
                        )
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    onClose()
                                }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .padding(15.dp)
                                        .size(24.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(4.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        textColor = colorResource(R.color.storm_grey),
                        cursorColor = colorResource(R.color.storm_grey),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@Composable
fun ItemTag(
    entityTag: Tag,
    entityTagSelectedList: MutableList<Tag>,
    itemClick: (Tag) -> Unit,
) {

    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            itemClick(entityTag)
        }
        .height(50.dp)) {
        val (imgTag, textName, checkBox) = createRefs()

        Image(painter = painterResource(id = R.drawable.ic_tag),
            contentDescription = null, contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(20.dp)
                .constrainAs(imgTag) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                })

        Text(text = entityTag.name,
            fontSize = 14.sp,
            color = colorResource(id = R.color.gulf_blue),
            modifier = Modifier.constrainAs(textName) {
                top.linkTo(imgTag.top)
                bottom.linkTo(imgTag.bottom)
                start.linkTo(imgTag.end, 16.dp)
                end.linkTo(checkBox.start)
                width = Dimension.fillToConstraints
            })
        Checkbox(
            checked = entityTagSelectedList.contains(entityTag),
            colors = CheckboxDefaults.colors(
                checkedColor = colorResource(R.color.royal_blue),
                uncheckedColor = colorResource(R.color.storm_grey)),
            modifier = Modifier.constrainAs(checkBox) {
                top.linkTo(imgTag.top)
                end.linkTo(parent.end)
                bottom.linkTo(imgTag.bottom)
            }, onCheckedChange = {
                itemClick(entityTag)
            })
    }
}

@Composable
fun ItemTagNote(tag: String) {
    Box(modifier = Modifier
        .background(
            color = colorResource(id = R.color.royal_blue_2),
            shape = RoundedCornerShape(16.dp)
        )
        .padding(horizontal = 20.dp, vertical = 2.dp)
    ) {
        Text(tag, color = Color.White, textAlign = TextAlign.Center)
    }
}
