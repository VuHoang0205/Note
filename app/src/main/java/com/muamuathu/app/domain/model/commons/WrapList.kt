package com.muamuathu.app.domain.model.commons

data class WrapList<T>(
    val list: List<T>,
    val tag: Long = System.currentTimeMillis()
)