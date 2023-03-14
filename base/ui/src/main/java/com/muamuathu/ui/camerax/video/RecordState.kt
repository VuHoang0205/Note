package com.muamuathu.ui.camerax.video


sealed interface RecordState

object Pending: RecordState
object Started : RecordState
object Paused: RecordState
object Stopped: RecordState
object Resumed: RecordState

fun RecordState.togglePauseResume() : RecordState {
    return when (this) {
        is Paused -> Resumed
        in listOf(Resumed, Started) -> Paused
        else -> throw IllegalArgumentException("Invalid record state! -> $this")
    }
}

fun RecordState.toggleStartStop() : RecordState {
    return when (this) {
        is Pending -> Started
        in listOf(Started, Paused, Resumed, Stopped) -> Stopped
        else -> throw IllegalArgumentException("Invalid record state! -> $this")
    }
}

