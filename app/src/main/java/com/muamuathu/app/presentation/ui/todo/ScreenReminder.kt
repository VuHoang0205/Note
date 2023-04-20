package com.muamuathu.app.presentation.ui.todo

import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.muamuathu.app.R
import com.muamuathu.app.domain.model.Task
import com.muamuathu.app.presentation.components.topbar.TopBarBase
import com.muamuathu.app.presentation.event.BottomSheetEvent
import com.muamuathu.app.presentation.event.NavEvent
import com.muamuathu.app.presentation.event.initEventHandler
import com.muamuathu.app.presentation.ui.todo.bottomSheet.ReminderTypeBottomSheet
import com.muamuathu.app.presentation.ui.todo.viewModel.AddTodoViewModel

@Composable
fun ScreenReminder() {
    val eventHandler = initEventHandler()
    val context = LocalContext.current as ComponentActivity
    val viewModel = hiltViewModel<AddTodoViewModel>(context)
    val task = viewModel.task.collectAsState()
    Content(task = task.value, onClose = { eventHandler.postNavEvent(NavEvent.PopBackStack()) }, onReminderType = {}, onSelectDate = {}, onSelectTime = {}, onRepeatType = {
        eventHandler.postBottomSheetEvent(BottomSheetEvent.Custom { ReminderTypeBottomSheet() })
    }, onDone = {})
}

@Composable
private fun Content(
    task: Task,
    onClose: () -> Unit,
    onReminderType: () -> Unit,
    onSelectDate: () -> Unit,
    onSelectTime: () -> Unit,
    onRepeatType: () -> Unit,
    onDone: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.alice_blue))
    ) {

        TopBarBase(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp), titleAlign = TextAlign.Center, title = stringResource(R.string.reminder), navigationIcon = {
                IconButton(onClick = {
                    onClose()
                }) {
                    Image(painter = painterResource(R.drawable.ic_close), contentDescription = null)
                }
            }, listRightIcon = null
        )

        Column(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .weight(1f)
        ) {
            ItemReminder(icon = R.drawable.ic_reminder, title = R.string.reminder_type, data = stringResource(id = R.string.repeating)) {
                onReminderType()
            }
            ItemReminder(icon = R.drawable.ic_select_date, title = R.string.date, data = stringResource(id = R.string.select_a_date)) {
                onSelectDate()
            }
            ItemReminder(icon = R.drawable.ic_select_time, title = R.string.time, data = stringResource(id = R.string.select_a_time)) {
                onSelectTime()
            }
            ItemReminder(icon = R.drawable.ic_reapting, title = R.string.repeat, data = stringResource(id = R.string.select_a_type)) {
                onRepeatType()
            }
        }

        TextButton(
            enabled = true, onClick = {
                onDone()
            }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .alpha(0.5f)
                .padding(horizontal = 16.dp, vertical = 30.dp)
                .background(
                    colorResource(R.color.royal_blue), RoundedCornerShape(4.dp)
                )
        ) {
            Text(
                text = stringResource(R.string.done).toUpperCase(Locale.current),
                color = Color.White,
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
private fun ItemReminder(@DrawableRes icon: Int, @StringRes title: Int, data: String, onClick: () -> Unit) {
    ConstraintLayout(modifier = Modifier
        .clickable { onClick() }
        .fillMaxWidth()) {
        val (ivIcon, contentView, divider) = createRefs()
        Image(modifier = Modifier.constrainAs(ivIcon) {
            start.linkTo(parent.start, 16.dp)
            centerVerticallyTo(contentView)
        }, painter = painterResource(id = icon), contentDescription = null)
        Column(modifier = Modifier
            .padding(vertical = 8.dp)
            .constrainAs(contentView) {
                start.linkTo(ivIcon.end, 4.dp)
                end.linkTo(parent.end, 16.dp)
                width = Dimension.fillToConstraints
                centerVerticallyTo(parent)
            }) {
            Text(text = stringResource(id = title), fontSize = 12.sp, color = colorResource(id = R.color.storm_grey))
            Text(text = data, fontSize = 14.sp, color = colorResource(id = R.color.gulf_blue))
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(divider) {
                    bottom.linkTo(parent.bottom)
                }, color = Color.Gray, thickness = 0.5.dp
        )
    }
}

@Preview
@Composable
private fun PreviewContent() {
    Content(
        Task(),
        {},
        {},
        {},
        {},
        {},
        {},
    )
}