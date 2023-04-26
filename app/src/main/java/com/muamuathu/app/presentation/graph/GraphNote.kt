package com.muamuathu.app.presentation.graph

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.muamuathu.app.presentation.extensions.getParentEntry
import com.muamuathu.app.presentation.ui.draw_sketch.ScreenDrawSketch
import com.muamuathu.app.presentation.ui.folder.KEY_CHOOSE_FOLDER
import com.muamuathu.app.presentation.ui.folder.ScreenNewFolder
import com.muamuathu.app.presentation.ui.note.EXTRA_NOTE_ID
import com.muamuathu.app.presentation.ui.note.MediaType
import com.muamuathu.app.presentation.ui.note.ScreenAddTag
import com.muamuathu.app.presentation.ui.note.ScreenCaptureImage
import com.muamuathu.app.presentation.ui.note.ScreenNewNote
import com.muamuathu.app.presentation.ui.note.ScreenNoteDetail
import com.muamuathu.app.presentation.ui.note.ScreenPickFile
import com.muamuathu.app.presentation.ui.note.ScreenSelectAttachment

fun NavGraphBuilder.note(navController: NavHostController, route: String) {
    composable(NavTarget.NoteAdd.route) { backStackEntry ->
        backStackEntry.getParentEntry(navController = navController, parentEntry = route)?.let {
            ScreenNewNote(hiltViewModel(it))
        }
    }

    composable(NavTarget.NoteDetail.route) { backstackEntry ->
        backstackEntry.arguments?.getString(EXTRA_NOTE_ID)?.let {
            ScreenNoteDetail(it, hiltViewModel())
        }
    }

    composable(NavTarget.NoteAddImage.route) { backstackEntry ->
        backstackEntry.getParentEntry(navController = navController, parentEntry = route)?.let {
            ScreenSelectAttachment(true, hiltViewModel(), hiltViewModel(it))
        }
    }

    composable(NavTarget.NoteAddVideo.route) { backstackEntry ->
        backstackEntry.getParentEntry(navController = navController, parentEntry = route)?.let {
            ScreenSelectAttachment(false, hiltViewModel(), hiltViewModel(it))
        }
    }

    composable(NavTarget.NotePickImage.route) { backstackEntry ->
        backstackEntry.getParentEntry(navController = navController, parentEntry = route)?.let {
            ScreenPickFile(MediaType.IMAGE, hiltViewModel(it), hiltViewModel())
        }
    }

    composable(NavTarget.NotePickVideo.route) { backstackEntry ->
        backstackEntry.getParentEntry(navController = navController, parentEntry = route)?.let {
            ScreenPickFile(MediaType.VIDEO, hiltViewModel(it), hiltViewModel())
        }
    }

    composable(NavTarget.NoteDrawSketch.route) { backstackEntry ->
        backstackEntry.getParentEntry(navController = navController, parentEntry = route)?.let {
            ScreenDrawSketch(hiltViewModel(it), hiltViewModel(it))
        }
    }

    composable(NavTarget.NoteCaptureImage.route) { backstackEntry ->
        backstackEntry.getParentEntry(navController = navController, parentEntry = route)?.let {
            ScreenCaptureImage(hiltViewModel(), hiltViewModel(it))
        }
    }

    composable(NavTarget.NoteAddTags.route) { backstackEntry ->
        backstackEntry.getParentEntry(navController = navController, parentEntry = route)?.let {
            ScreenAddTag(hiltViewModel(it))
        }
    }

    composable(NavTarget.NoteAddFolder.route, arguments = listOf(navArgument(KEY_CHOOSE_FOLDER) { defaultValue = true })) { backStackEntry ->
        backStackEntry.getParentEntry(navController = navController, parentEntry = route)?.let {
            val isChoose = backStackEntry.arguments?.getBoolean(KEY_CHOOSE_FOLDER) ?: false
            ScreenNewFolder(isChoose, hiltViewModel(), hiltViewModel(it))
        }
    }
}

