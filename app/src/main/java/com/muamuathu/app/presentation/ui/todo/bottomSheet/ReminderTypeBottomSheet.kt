package com.muamuathu.app.presentation.ui.todo.bottomSheet

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.muamuathu.app.domain.model.RepeatTypeEnum
import com.muamuathu.app.presentation.components.topbar.TopBarBase
import com.muamuathu.app.presentation.event.BottomSheetEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.ui.todo.viewModel.AddTodoViewModel

@Composable
fun ReminderTypeBottomSheet() {
    val eventHandler = initEventHandler()
    val todoViewModel = hiltViewModel<AddTodoViewModel>(LocalContext.current as ComponentActivity)
    Content(onDone = {
        todoViewModel.updateRepeatType()
        eventHandler.postBottomSheetEvent(BottomSheetEvent.Hide { true })
    })
}

@Composable
private fun Content(
    onDone: (repeatType: RepeatTypeEnum) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.alice_blue))
    ) {

        TopBarBase(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp), titleAlign = TextAlign.Start, title = stringResource(R.string.reminder), navigationIcon = {}, listRightIcon = null
        )

        LazyColumn(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(RepeatTypeEnum.values()) {
                ItemRepeatType(reminderType = it) {
                    onDone(it)
                }
            }
        }
    }
}

@Composable
private fun ItemRepeatType(reminderType: RepeatTypeEnum, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

            }, horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = R.drawable.ic_repeat_type), contentDescription = null)
        Text(text = reminderType.name, fontSize = 14.sp, color = colorResource(id = R.color.gulf_blue))
    }
}

@Preview
@Composable
private fun PreviewContent() {
    Content(
        {},
    )
}