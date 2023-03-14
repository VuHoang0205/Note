package com.muamuathu.app.presentation.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.muamuathu.app.presentation.ui.note.ScreenNote


fun NavGraphBuilder.todo() {
    composable(NavTarget.Todo.route) {
        ScreenNote()
    }
}