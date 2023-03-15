package com.muamuathu.app.presentation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.muamuathu.app.presentation.ui.folder.ScreenFolder
import com.muamuathu.app.presentation.ui.folder.ScreenNewFolder

fun NavGraphBuilder.folder() {
    composable(NavTarget.Folder.route) {
        ScreenFolder()
    }

    composable(NavTarget.FolderAdd.route) {
        ScreenNewFolder()
    }
}
