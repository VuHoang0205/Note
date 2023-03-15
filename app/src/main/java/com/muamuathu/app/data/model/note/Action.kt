package com.muamuathu.app.data.model.note

import com.muamuathu.app.R

enum class Action(val resource: Int) {
    OpenCamera(R.drawable.ic_camera),
    OpenGallery(R.drawable.ic_gallery),
    AddTag(R.drawable.ic_tag),
    AddAudio(R.drawable.ic_audio),
    OpenVideo(R.drawable.ic_record),
    FileManager(R.drawable.ic_file_manger),
    DrawSketch(R.drawable.ic_draw_sketch)
}