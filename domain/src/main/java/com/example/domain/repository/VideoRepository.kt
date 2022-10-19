package com.example.domain.repository

import com.example.domain.model.FirebaseResponse
import com.example.domain.model.Video

/**
 * @Created by 김현국 2022/10/18
 * @Time 2:32 PM
 */
interface VideoRepository {

    suspend fun insertVideo(video: Video)

    suspend fun deleteVideo(name: String)

    suspend fun getVideoList(): FirebaseResponse<List<Video>>
}
