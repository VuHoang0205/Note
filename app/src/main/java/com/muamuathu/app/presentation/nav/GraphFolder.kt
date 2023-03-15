package com.muamuathu.app.presentation.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.muamuathu.app.presentation.ui.folder.ScreenFolder
import com.solid.folder.ScreenNewFolder

fun NavGraphBuilder.folder() {
    composable(NavTarget.Folder.route) {
        ScreenFolder()
    }

    composable(NavTarget.FolderAdd.route) {
        ScreenNewFolder()
    }
}
