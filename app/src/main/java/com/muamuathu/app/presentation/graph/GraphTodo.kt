package com.muamuathu.app.presentation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.muamuathu.app.presentation.ui.todo.ScreenAddSubTask
import com.muamuathu.app.presentation.ui.todo.ScreenNewTask
import com.muamuathu.app.presentation.ui.todo.ScreenReminder
import com.muamuathu.app.presentation.ui.todo.ScreenTodo


fun NavGraphBuilder.todo(navController: NavHostController) {
    composable(NavTarget.Todo.route) {
        ScreenTodo()
    }
    composable(NavTarget.TodoAdd.route) {
        ScreenNewTask()
    }
    composable(NavTarget.TodoAddSubTask.route) {
        ScreenAddSubTask()
    }

    composable(NavTarget.TodoReminder.route) {
        ScreenReminder()
    }
}