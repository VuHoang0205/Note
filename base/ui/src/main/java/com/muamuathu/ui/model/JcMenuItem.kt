package com.muamuathu.ui.model

import com.muamuathu.ui.icon.IconSource
import java.util.*

data class JcMenuItem(
    val id: String = UUID.randomUUID().toString(),
    val text: String? = null,
    val icon: IconSource? = null,
)