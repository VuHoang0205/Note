package com.muamuathu.app.presentation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.muamuathu.app.presentation.ui.folder.ScreenFolder
import com.muamuathu.app.presentation.ui.folder.ScreenNewFolder

fun NavGraphBuilder.folder() {
    composable(NavTarget.Folder.route) {
        ScreenFolder()
    }

    composable(NavTarget.FolderAdd.route) {
        ScreenNewFolder(false)
    }

    composable(NavTarget.FolderChoose.route,
        arguments = listOf(navArgument("isChoose") { defaultValue = true })) { backStackEntry ->
        val isChoose = backStackEntry.arguments?.getBoolean("isChoose") ?: false
        ScreenNewFolder(isChoose)
    }
}
