package com.muamuathu.app.presentation.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.muamuathu.app.presentation.ui.note.ScreenNote

fun NavGraphBuilder.folder() {
    composable(NavTarget.Folder.route) {
        ScreenNote()
    }

    composable(NavTarget.FolderAdd.route) {
        ScreenNote()
    }
}
