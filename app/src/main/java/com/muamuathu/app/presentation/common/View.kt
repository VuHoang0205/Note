package com.muamuathu.app.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.muamuathu.app.domain.model.Folder
import com.muamuathu.app.domain.model.Tag
import com.muamuathu.app.presentation.extensions.isSameDay
import com.muamuathu.app.presentation.extensions.toDayOfMonth
import com.muamuathu.app.presentation.extensions.toDayOfWeek
import java.time.ZonedDateTime


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
    ConstraintLayout(modifier = modifier) {
        val (columnDate, lazyRowCalendar) = createRefs()
        DateTimeView(modifier = modifier.constrainAs(columnDate) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }, textTotal = textTotal, onCalendar = onCalendar)

        CalendarView(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(lazyRowCalendar) {
                top.linkTo(columnDate.bottom, 16.dp)
            }, dateList = dateList, lazyListState = lazyListState, selectDate = selectDate, onSelectDate = onSelectDate)
    }
}

@Composable
fun DateTimeView(
    modifier: Modifier,
    textTotal: String,
    onCalendar: (selectDate: ZonedDateTime?) -> Unit,
) {
    val selectDate by remember { mutableStateOf(ZonedDateTime.now()) }
    val dateString = selectDate.toDayOfMonth()
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = dateString, color = colorResource(R.color.gulf_blue), fontSize = 26.sp)
            Text(text = textTotal, color = colorResource(R.color.storm_grey), fontSize = 13.sp)
        }
        Row(modifier = Modifier
            .clickable {
                onCalendar(selectDate)
            }, verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectDate.year.toString(),
                color = colorResource(id = R.color.gulf_blue),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )
            Image(painter = painterResource(R.drawable.ic_down), contentDescription = "down", modifier = Modifier.align(alignment = Alignment.CenterVertically))
            Image(painter = painterResource(R.drawable.ic_calendar), contentScale = ContentScale.Crop, modifier = Modifier.size(32.dp), contentDescription = "search")
        }
    }
}

@Composable
private fun CalendarView(
    modifier: Modifier,
    dateList: List<ZonedDateTime>,
    lazyListState: LazyListState,
    selectDate: ZonedDateTime,
    onSelectDate: (selectDate: ZonedDateTime) -> Unit,
) {
    LazyRow(
        state = lazyListState, modifier = modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(dateList) { _, item ->
            ItemCalendar(date = item, select = item.isSameDay(selectDate)) {
                onSelectDate(item)
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
        colors = CardDefaults.cardColors(containerColor = if (select) colorResource(R.color.royal_blue_2) else Color.White), modifier = Modifier
            .height(56.dp)
            .width(42.dp)
            .clickable {
                onClickDate()
            }, shape = RoundedCornerShape(8.dp), elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()
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
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(2.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(value = query, onValueChange = { value ->
                    onSearch(value)
                }, label = {
                    Text(stringResource(label))
                }, modifier = Modifier.fillMaxWidth(), textStyle = TextStyle(fontSize = 14.sp), leadingIcon = {
                    Icon(
                        Icons.Default.Search, contentDescription = "", modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }, trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = {
                            onClose()
                        }) {
                            Icon(
                                Icons.Default.Close, contentDescription = "", modifier = Modifier
                                    .padding(15.dp)
                                    .size(24.dp)
                            )
                        }
                    }
                }, singleLine = true,
                    shape = RoundedCornerShape(4.dp), colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        cursorColor = colorResource(R.color.storm_grey),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedLabelColor = colorResource(R.color.storm_grey)
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

        Image(painter = painterResource(id = R.drawable.ic_tag), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier
            .size(20.dp)
            .constrainAs(imgTag) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            })

        Text(text = entityTag.name, fontSize = 14.sp, color = colorResource(id = R.color.gulf_blue), modifier = Modifier.constrainAs(textName) {
            top.linkTo(imgTag.top)
            bottom.linkTo(imgTag.bottom)
            start.linkTo(imgTag.end, 16.dp)
            end.linkTo(checkBox.start)
            width = Dimension.fillToConstraints
        })
        Checkbox(checked = entityTagSelectedList.contains(entityTag), colors = CheckboxDefaults.colors(
            checkedColor = colorResource(R.color.royal_blue), uncheckedColor = colorResource(R.color.storm_grey)
        ), modifier = Modifier.constrainAs(checkBox) {
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
    Box(
        modifier = Modifier
            .background(
                color = colorResource(id = R.color.royal_blue_2), shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 20.dp, vertical = 2.dp)
    ) {
        Text(tag, color = Color.White, textAlign = TextAlign.Center)
    }
}

@Composable
fun FolderSelectView(modifier: Modifier, folder: Folder, onChooseFolder: () -> Unit) {
    ConstraintLayout(modifier = modifier) {
        val (divider1, rowFolder, divider2) = createRefs()
        Divider(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colorResource(R.color.gainsboro))
            .constrainAs(divider1) {
                top.linkTo(parent.top)
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
                    .padding(top = 16.dp, start = 20.dp, bottom = 16.dp)
                    .constrainAs(icFolder) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    })

            Text(text = folder.name.ifEmpty { stringResource(R.string.txt_choose_folder) },
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
    }
}
