package com.muamuathu.ui.timer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import java.util.*

@Composable
fun JcIntervalTimer(
    key: Any? = Unit,
    intervalMs: Long,
    onTick: () -> Unit,
) {
    DisposableEffect(key) {
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                onTick()
            }
        }, 0, intervalMs)

        onDispose {
            timer.cancel()
            timer.purge()
        }
    }
}