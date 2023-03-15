package com.muamuathu.app.presentation.common.text_input_validator

abstract class BaseTextInputValidator {
    abstract val validate: (String) -> Boolean
}