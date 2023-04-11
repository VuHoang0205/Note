package com.muamuathu.app.presentation.event

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign


sealed class TopBarEvent {
    object None : TopBarEvent()
    class Title(
        val title: String,
        val titleAlign: TextAlign = TextAlign.Center,
        val rightList: List<Triple<@Composable () -> Unit, () -> Unit, Boolean?>> = emptyList(),
    ) : TopBarEvent()

    class Back(
        val title: String,
        val titleAlign: TextAlign = TextAlign.Center,
        val rightList: List<Triple<@Composable () -> Unit, () -> Unit, Boolean?>> = emptyList(),
        val onBack: () -> Unit,
    ) : TopBarEvent()

    class Close(
        val title: String,
        val titleAlign: TextAlign = TextAlign.Center,
        val rightList: List<Triple<@Composable () -> Unit, () -> Unit, Boolean?>> = emptyList(),
        val onClose: () -> Unit,
    ) : TopBarEvent()

    class Home(
        val title: String,
        val titleAlign: TextAlign = TextAlign.Center,
        val rightList: List<Triple<@Composable () -> Unit, () -> Unit, Boolean?>> = emptyList(),
        val onHome: () -> Unit,
    ) : TopBarEvent()
}


