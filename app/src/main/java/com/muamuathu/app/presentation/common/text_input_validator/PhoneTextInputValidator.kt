package com.muamuathu.app.presentation.common.text_input_validator

import android.util.Patterns
import com.muamuathu.app.presentation.common.text_input_validator.BaseTextInputValidator

class PhoneTextInputValidator : BaseTextInputValidator() {
    override val validate: (String) -> Boolean
        get() = { text ->
            Patterns.PHONE.matcher(text).matches()
        }
}