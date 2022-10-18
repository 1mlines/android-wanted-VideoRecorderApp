package com.example.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.source.local.model.VideoEntity

/**
 * @Created by 김현국 2022/10/18
 * @Time 2:21 PM
 */
@Database(
    entities = [VideoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class VideoDatabase : RoomDatabase() {

    abstract fun getVideoDao(): VideoDao
}
