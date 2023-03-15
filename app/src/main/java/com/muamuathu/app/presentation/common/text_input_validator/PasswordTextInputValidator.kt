package com.muamuathu.app.presentation.common.text_input_validator

import com.muamuathu.app.presentation.common.text_input_validator.BaseTextInputValidator
import java.util.regex.Pattern

class PasswordTextInputValidator : BaseTextInputValidator() {
    companion object {
        const val REGEX_PASSWORD =
            "(?=^.{6,16}\$)(?!.*\\s)[0-9a-zA-Z!@#\$%^&*()]*\$"
    }

    override val validate: (String) -> Boolean
        get() = { text ->
            Pattern.compile(REGEX_PASSWORD).matcher(text).matches()
        }
}