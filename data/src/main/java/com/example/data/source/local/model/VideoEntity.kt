package com.example.data.source.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Created by 김현국 2022/10/18
 * @Time 2:05 PM
 */

@Entity
data class VideoEntity(
    @PrimaryKey val name: String,
    @ColumnInfo(name = "publishedAt") val publishedAt: Long,
    @ColumnInfo(name = "uri") val uri: String
) {
    companion object {
        val EMPTY = VideoEntity(
            name = "",
            publishedAt = 0L,
            uri = ""
        )
    }
}
