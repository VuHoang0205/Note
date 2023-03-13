package com.muamuathu.feature.purchase.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

data class ProFeature(
    val icon: ImageVector,
    val title: String,
    val description: String
) {
    companion object {
        fun all() = listOf(
            noAds(),
            remoteControl(),
            unlimitedScripts(),
            mirrorText(),
            importFeature()
        )

        private fun remoteControl() = ProFeature(
            icon = Icons.Rounded.SettingsRemote,
            title = "Remote control",
            description = "Control the scrolling and video recording using any Bluetooth keyboard or handheld presentation controller."
        )

        private fun noAds() = ProFeature(
            icon = Icons.Rounded.AdsClick,
            title = "No ads",
            description = "Ads help us with funding to maintain the app better. As a premium user, you will have the right to experience the app without ads."
        )

        private fun unlimitedScripts() = ProFeature(
            icon = Icons.Rounded.AllInclusive,
            title = "Unlimited scripts",
            description = "You can create unlimited amount of text."
        )

        private fun mirrorText() = ProFeature(
            icon = Icons.Rounded.Flip,
            title = "Mirror Text",
            description = "Flip your script horizontally or vertically to use your device in any professional teleprompter hardware."
        )

        private fun importFeature() = ProFeature(
            icon = Icons.Rounded.Download,
            title = "Import unlimited documents",
            description = "Import Word, PDF, .txt files easily from any of your favourite services including Google Drive, Dropbox, Microsoft Word and many more."
        )
    }
}