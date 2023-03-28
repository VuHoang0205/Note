package com.muamuathu.app.domain.mapper

import com.muamuathu.app.data.entity.EntityTag
import com.muamuathu.app.domain.model.Tag

fun EntityTag.toDomainModel() = Tag(
    tagId = tagId,
    name = name,
)

fun Tag.toEntityModel() = EntityTag(
    tagId = tagId,
    name = name,
)