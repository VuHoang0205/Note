package com.muamuathu.app.presentation.common.text_input_validator

import androidx.core.util.PatternsCompat

class EmailTextInputValidator : BaseTextInputValidator() {
    override val validate: (String) -> Boolean
        get() = { text ->
            PatternsCompat.EMAIL_ADDRESS.matcher(text).matches()
        }
}