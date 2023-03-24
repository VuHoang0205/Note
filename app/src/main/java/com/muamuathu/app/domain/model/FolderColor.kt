package com.muamuathu.app.domain.model

enum class FolderColor(val color: Long) {
    ORANGE(0xFF7348),
    GREEN(0x3FCD69),
    BLUE(0x41ABFF),
    BROWN(0x7A5353),
    DARK_BLUE(0x458FA6),
    DARK_GREEN(0x6B7A53),
    PURPLE(0x54537A);

    companion object {
        fun fromValue(color: Long): FolderColor {
            return when (color) {
                ORANGE.color -> ORANGE
                GREEN.color -> GREEN
                BLUE.color -> BLUE
                BROWN.color -> BROWN
                DARK_BLUE.color -> DARK_BLUE
                DARK_GREEN.color -> DARK_GREEN
                PURPLE.color -> PURPLE
                else -> ORANGE
            }
        }
    }
}