package com.muamuathu.app.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.muamuathu.app.ui.note.ScreenNote


fun NavGraphBuilder.todo() {
    composable(NavTarget.Todo.route) {
        ScreenNote()
    }
}