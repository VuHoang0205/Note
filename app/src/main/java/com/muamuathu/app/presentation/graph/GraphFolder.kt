package com.muamuathu.app.presentation.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.muamuathu.app.presentation.ui.folder.ScreenNewFolder

fun NavGraphBuilder.folder(navController: NavHostController, route: String) {

    composable(NavTarget.FolderAdd.route) { backStackEntry ->
        ScreenNewFolder(false, hiltViewModel())
    }
}
