package com.muamuathu.app.presentation.components.dialog

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog
import com.marosseleng.compose.material3.datetimepickers.time.ui.dialog.TimePickerDialog
import com.muamuathu.app.R
import java.time.LocalDate
import java.time.LocalTime

@ExperimentalComposeUiApi
@Composable
fun CustomDatePickerDialog(isShow: Boolean, onDismissRequest: () -> Unit, onDateChange: (LocalDate) -> Unit) {
    if (isShow) {
        var isShowToast by remember { mutableStateOf(false) }
        DatePickerDialog(onDismissRequest = onDismissRequest, onDateChange = {
            if (it.dayOfYear >= LocalDate.now().dayOfYear) {
                onDismissRequest()
                onDateChange(it)
            } else {
                isShowToast = true
            }
        })
        if (isShowToast) {
            Toast.makeText(LocalContext.current, stringResource(R.string.msg_choose_time_error), Toast.LENGTH_SHORT).show()
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun CustomTimePickerDialog(isShow: Boolean, onDismissRequest: () -> Unit, onTimeChange: (LocalTime) -> Unit) {
    if (isShow) {
        var isShowToast by remember { mutableStateOf(false) }
        TimePickerDialog(onDismissRequest = onDismissRequest, onTimeChange = {
            if (it.isAfter(LocalTime.now())) {
                onDismissRequest()
                onTimeChange(it)
            } else {
                isShowToast = true
            }
        })
        if (isShowToast) {
            Toast.makeText(LocalContext.current, stringResource(R.string.msg_choose_time_error), Toast.LENGTH_SHORT).show()
        }
    }
}


