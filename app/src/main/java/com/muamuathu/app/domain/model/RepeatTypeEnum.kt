package com.muamuathu.app.domain.model

enum class RepeatTypeEnum(val text: String) {
    None(""),
    Daily("Daily"),
    Weekly("Weekly"),
    Monthly("Monthly"),
    Yearly("Yearly"),
    CustomRepeat("Custom Repeat"),
}