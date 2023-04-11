package com.muamuathu.app.presentation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.muamuathu.app.presentation.ui.todo.ScreenNewTask
import com.muamuathu.app.presentation.ui.todo.ScreenTodo


fun NavGraphBuilder.todo() {
    composable(NavTarget.Todo.route) {
        ScreenTodo()
    }
    composable(NavTarget.TodoAdd.route) {
        ScreenNewTask()
    }
}