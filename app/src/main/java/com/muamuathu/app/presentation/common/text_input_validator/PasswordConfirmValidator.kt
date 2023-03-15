package com.muamuathu.app.presentation.common.text_input_validator

import android.text.TextUtils
import com.muamuathu.app.presentation.common.text_input_validator.BaseTextInputValidator

class PasswordConfirmValidator : BaseTextInputValidator() {
    override val validate: (String) -> Boolean
        get() = { text ->
            if (TextUtils.isEmpty(confirmPassword)) true else TextUtils.equals(
                text,
                confirmPassword
            )
        }

    private var confirmPassword: String? = null
    fun updatePassword(password: String? = null) {
        this.confirmPassword = password
    }
}