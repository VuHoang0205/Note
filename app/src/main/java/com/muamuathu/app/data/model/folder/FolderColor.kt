package com.muamuathu.app.data.model.folder

import androidx.annotation.ColorRes
import com.muamuathu.app.R

enum class FolderColor(@ColorRes val color: Int) {
    ORANGE(R.color.coral),
    GREEN(R.color.emerald),
    BLUE(R.color.summer_sky),
    BROWN(R.color.buccaneer),
    DARK_BLUE(R.color.boston_blue),
    DARK_GREEN(R.color.willow_grove),
    PURPLE(R.color.victoria),
}