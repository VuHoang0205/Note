package com.muamuathu.ui.timer

import android.os.CountDownTimer
import androidx.compose.runtime.*

@Composable
fun JcTimer(
    durationMs: Long,
    intervalMs: Long,
    onTick: (Long) -> Unit,
    onCompleted: () -> Unit,
) {
    DisposableEffect(Unit) {
        val timer = object : CountDownTimer(durationMs, intervalMs) {
            override fun onTick(remainMs: Long) {
                onTick(remainMs)
            }

            override fun onFinish() {
                onCompleted()
            }
        }
        timer.start()

        onDispose {
            timer.cancel()
        }
    }
}