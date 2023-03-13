package com.muamuathu.ui.timepicker

import android.widget.TimePicker
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import java.time.Duration

@Composable
fun JcTimePicker(duration: Duration, onDateChanged: (Duration) -> Unit) {
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = {
            TimePicker(it).apply {
                hour = duration.toHours().toInt()
                minute = duration.toMinutes().toInt()

                setOnTimeChangedListener { _, hour, minute ->
                    onDateChanged(Duration.ofSeconds((hour * 3600 + minute * 60).toLong()))
                }
            }
        }
    )
}