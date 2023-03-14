package com.muamuathu.app.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.muamuathu.app.ui.note.ScreenNote

fun NavGraphBuilder.folder() {
    composable(NavTarget.Folder.route) {
        ScreenNote()
    }

    composable(NavTarget.FolderAdd.route) {
        ScreenNote()
    }
}
