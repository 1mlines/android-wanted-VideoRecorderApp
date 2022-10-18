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
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "uri") val uri: String,
    @ColumnInfo(name = "date") val date: String
) {
    companion object {
        val EMPTY = VideoEntity(
            id = 0,
            uri = "",
            date = ""
        )
    }
}
