package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.model.FirebaseResponse
import com.example.domain.model.Video
import kotlinx.coroutines.flow.Flow

/**
 * @Created by 김현국 2022/10/18
 * @Time 2:32 PM
 */
interface VideoRepository {

    fun getVideoList(): Flow<PagingData<Video>>

    suspend fun uploadVideo(video: Video): FirebaseResponse<Nothing>

    suspend fun deleteVideoFirestore(video: Video): FirebaseResponse<Nothing>

    suspend fun deleteVideoStorage(video: Video): FirebaseResponse<Nothing>

}
