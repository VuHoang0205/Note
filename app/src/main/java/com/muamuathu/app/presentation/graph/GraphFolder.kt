package com.muamuathu.app.presentation.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.muamuathu.app.data.base.AppLog
import com.muamuathu.app.presentation.extensions.getParentEntry
import com.muamuathu.app.presentation.ui.folder.ScreenFolder
import com.muamuathu.app.presentation.ui.folder.ScreenNewFolder

fun NavGraphBuilder.folder(navController: NavHostController, route: String) {
    composable(NavTarget.Folder.route) {
        ScreenFolder(hiltViewModel())
    }

    composable(NavTarget.FolderAdd.route) { backStackEntry ->
        backStackEntry.getParentEntry(navController = navController, parentEntry = route)?.let {
            ScreenNewFolder(false, hiltViewModel(), hiltViewModel(it))
        }
    }
}
