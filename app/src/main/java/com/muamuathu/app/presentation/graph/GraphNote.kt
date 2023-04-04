package com.muamuathu.app.presentation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.muamuathu.app.presentation.ui.draw_sketch.ScreenDrawSketch
import com.muamuathu.app.presentation.ui.note.*

fun NavGraphBuilder.note() {
    composable(NavTarget.Note.route) {
        ScreenNote()
    }

    composable(NavTarget.NoteDetail.route) { backstackEntry ->
        backstackEntry.arguments?.getString(EXTRA_NOTE_ID)?.let {
            ScreenNoteDetail(it)
        }
    }

    composable(NavTarget.NoteAdd.route) {
        ScreenNewNote()
    }

    composable(NavTarget.NoteAddImage.route) {
        ScreenSelectAttachment(true)
    }

    composable(NavTarget.NoteAddVideo.route) {
        ScreenSelectAttachment(false)
    }

    composable(NavTarget.NotePickImage.route) {
        ScreenPickFile(MediaType.IMAGE)
    }

    composable(NavTarget.NotePickVideo.route) {
        ScreenPickFile(MediaType.VIDEO)
    }

    composable(NavTarget.NoteDrawSketch.route) {
        ScreenDrawSketch()
    }

    composable(NavTarget.NoteCaptureImage.route) {
        ScreenCaptureImage()
    }

    composable(NavTarget.NoteAddTags.route) {
        ScreenAddTag()
    }
}


