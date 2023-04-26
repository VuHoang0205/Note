package com.muamuathu.app.presentation.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.muamuathu.app.presentation.ui.authentication.ScreenLogin
import com.muamuathu.app.presentation.ui.folder.ScreenFolder
import com.muamuathu.app.presentation.ui.note.ScreenNote
import com.muamuathu.app.presentation.ui.todo.ScreenTodo

fun NavGraphBuilder.home() {
    composable(NavTarget.Note.route) {
        ScreenNote(hiltViewModel())
    }

    composable(NavTarget.Folder.route) {
        ScreenFolder(hiltViewModel())
    }

    composable(NavTarget.Todo.route) {
        ScreenTodo()
    }

    composable(NavTarget.Account.route) {
        ScreenLogin()
    }
}

