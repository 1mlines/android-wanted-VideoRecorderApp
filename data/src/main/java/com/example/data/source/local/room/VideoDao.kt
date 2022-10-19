package com.example.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.source.local.model.VideoEntity

/**
 * @Created by 김현국 2022/10/18
 * @Time 2:14 PM
 */
@Dao
interface VideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVideoData(videoEntity: VideoEntity)

    @Query("DELETE FROM VideoEntity WHERE id = :id")
    fun deleteVideoData(id: Long)
}
