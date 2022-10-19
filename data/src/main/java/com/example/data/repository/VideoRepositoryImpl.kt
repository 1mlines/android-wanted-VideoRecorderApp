package com.example.data.repository

import com.example.data.model.toEntity
import com.example.data.source.local.LocalDataSource
import com.example.data.source.remote.VideoRemoteDataSource
import com.example.domain.model.FirebaseResponse
import com.example.domain.model.Video
import com.example.domain.repository.VideoRepository
import javax.inject.Inject

/**
 * @Created by 김현국 2022/10/18
 * @Time 2:31 PM
 */
class VideoRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val videoRemoteDataSource: VideoRemoteDataSource,
) : VideoRepository {
    override suspend fun insertVideo(video: Video) {
        return localDataSource.insertVideoData(videoEntity = video.toEntity())
    }

    override suspend fun deleteVideo(name: String) {
        return localDataSource.deleteVideoData(name = name)
    }

    override suspend fun getVideoList(): FirebaseResponse<List<Video>> = videoRemoteDataSource.getVideoList()


}
