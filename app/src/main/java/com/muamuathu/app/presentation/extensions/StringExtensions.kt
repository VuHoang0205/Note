package com.muamuathu.app.presentation.extensions

import java.text.Normalizer

fun String.removeAccent(): String {
    var output = this
    try {
        output = Normalizer.normalize(output, Normalizer.Form.NFD)
        output = output.replace("đ".toRegex(), "d")
        output = output.replace("Đ".toRegex(), "D")
        output = output.replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
        output = output.replace("[^\\p{ASCII}]".toRegex(), "")
        output = output.replace("\\p{M}".toRegex(), "")
    } catch (e: Exception) {
        return this
    }
    return if (output.isEmpty()) {
        this
    } else output
}