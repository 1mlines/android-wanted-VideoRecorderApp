package com.example.data.source.local

import androidx.room.withTransaction
import com.example.data.source.local.model.VideoEntity
import com.example.data.source.local.room.VideoDatabase
import javax.inject.Inject

/**
* @Created by 김현국 2022/10/18
* @Time 2:28 PM
*/
class LocalDataSource @Inject constructor(
    private val videoDatabase: VideoDatabase
) {
    private val videoDao = videoDatabase.getVideoDao()

    suspend fun insertVideoData(videoEntity: VideoEntity) {
        videoDatabase.withTransaction {
            videoDao.insertVideoData(videoEntity = videoEntity)
        }
    }

    suspend fun deleteVideoData(id: Long) {
        videoDatabase.withTransaction {
            videoDao.deleteVideoData(id = id)
        }
    }
}
