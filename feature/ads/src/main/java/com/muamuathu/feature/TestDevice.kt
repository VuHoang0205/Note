package com.muamuathu.feature

sealed class TestDevice(val id: String) {
    object Pixel3 : TestDevice("D887B4C580DCD073D702566E04BD2D39")
    object Pixel5 : TestDevice("")

    companion object {
        fun all() = listOf(
            Pixel3,
            Pixel5
        )
    }
}
