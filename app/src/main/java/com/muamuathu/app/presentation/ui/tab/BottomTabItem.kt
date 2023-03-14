package com.muamuathu.app.presentation.ui.tab

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.muamuathu.app.R
import com.muamuathu.app.presentation.nav.NavTarget

enum class BottomTabItem(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    val route: String,
) {
    NOTE(R.drawable.ic_note_tab, R.string.note, NavTarget.Note.route),
    FOLDER(R.drawable.ic_folder_tab, R.string.folder, NavTarget.Folder.route),
    ADD(R.drawable.ic_folder_tab, R.string.create, NavTarget.NoteAdd.route),
    TODO(R.drawable.ic_todo_tab, R.string.todo, NavTarget.Todo.route),
    ACCOUNT(R.drawable.ic_tab_profile, R.string.account, NavTarget.Account.route)
}