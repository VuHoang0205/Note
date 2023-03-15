package com.muamuathu.app.presentation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.muamuathu.app.presentation.ui.authentication.ScreenLogin
import com.muamuathu.app.presentation.ui.authentication.ScreenLoginWithEmail
import com.muamuathu.app.presentation.ui.authentication.ScreenSignUp
import com.muamuathu.app.presentation.ui.authentication.ScreenVerifyEmail

const val EXTRA_EMAIL = "email"

fun NavGraphBuilder.authentication() {
    composable(NavTarget.Login.route) {
        ScreenLogin()
    }
    composable(NavTarget.LoginWithEmail.route) {
        ScreenLoginWithEmail()
    }
    composable(NavTarget.SignUpWithEmail.route) {
        ScreenSignUp()
    }
    composable(NavTarget.VerifyEmailAddress.route) { backstackEntry ->
        backstackEntry.arguments?.getString(EXTRA_EMAIL)?.let {
            ScreenVerifyEmail(it)
        }
    }
    composable(NavTarget.FacebookSignIn.route) {
    }
    composable(NavTarget.GoogleSignIn.route) {
    }
}