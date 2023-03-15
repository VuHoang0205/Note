package com.muamuathu.app.presentation.common.text_input_validator

import com.muamuathu.app.presentation.common.text_input_validator.BaseTextInputValidator

class NotEmptyTextInputValidator : BaseTextInputValidator() {
    override val validate: (String) -> Boolean
        get() = { text ->
            text.isNotEmpty()
        }
}