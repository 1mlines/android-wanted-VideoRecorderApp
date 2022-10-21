package com.example.data.model

import com.example.data.source.local.model.VideoEntity
import com.example.domain.model.Video

/**
 * @Created by 김현국 2022/10/18
 * @Time 2:35 PM
 */
fun Video.toEntity(): VideoEntity {
    return VideoEntity(
        name = this.name,
        uri = this.uri,
        publishedAt = this.publishedAt
    )
}

fun VideoEntity.toModel(): Video {
    return Video(
        name = this.name,
        uri = this.uri,
        publishedAt = this.publishedAt
    )
}
