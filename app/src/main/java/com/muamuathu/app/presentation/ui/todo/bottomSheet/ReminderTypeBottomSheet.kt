package com.muamuathu.app.presentation.ui.todo.bottomSheet

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.muamuathu.app.R
import com.muamuathu.app.domain.model.ReminderTypeEnum
import com.muamuathu.app.presentation.components.topbar.TopBarBase
import com.muamuathu.app.presentation.event.BottomSheetEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.ui.todo.viewModel.ReminderViewModel

@Composable
fun ReminderTypeBottomSheet() {
    val eventHandler = initEventHandler()
    val reminderViewModel = hiltViewModel<ReminderViewModel>(LocalContext.current as ComponentActivity)
    Content(onDone = {
        reminderViewModel.updateReminderType(it)
        eventHandler.postBottomSheetEvent(BottomSheetEvent.Hide { true })
    })
}

@Composable
private fun Content(
    onDone: (repeatType: ReminderTypeEnum) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.alice_blue))
    ) {

        TopBarBase(
            modifier = Modifier.fillMaxWidth(),
            titleAlign = TextAlign.Center,
            title = stringResource(R.string.select_type),
            navigationIcon = {},
            listRightIcon = null,
            backgroundColor = Color.Transparent
        )

        LazyColumn(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(ReminderTypeEnum.values()) {
                ItemRepeatType(reminderType = it) {
                    onDone(it)
                }
            }
        }
    }
}

@Composable
private fun ItemRepeatType(reminderType: ReminderTypeEnum, onClick: () -> Unit) {
    Box(modifier = Modifier.clickable {
        onClick()
    }, contentAlignment = Alignment.Center) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = if (reminderType == ReminderTypeEnum.OneTime) R.drawable.ic_one_time else R.drawable.ic_reapting),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    Color(0xFF6F7382)
                )
            )
            Text(text = reminderType.text, fontSize = 14.sp, color = colorResource(id = R.color.gulf_blue))
        }
    }
}

@Preview
@Composable
private fun PreviewContent() {
    Content(
        {},
    )
}