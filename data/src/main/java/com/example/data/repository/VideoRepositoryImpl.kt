package com.example.data.repository

import com.example.data.model.toEntity
import com.example.data.source.local.LocalDataSource
import com.example.data.source.remote.VideoRemoteDataSource
import com.example.domain.model.FirebaseResponse
import com.example.domain.model.FirebaseState
import com.example.domain.model.Video
import com.example.domain.repository.VideoRepository
import timber.log.Timber
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

    override suspend fun getVideoList(): FirebaseResponse<List<Video>> =
        videoRemoteDataSource.getVideoList()

    override suspend fun uploadVideo(video: Video): FirebaseResponse<Nothing> {
        val result = videoRemoteDataSource.uploadVideoStorage(video)
        return if(result.state == FirebaseState.SUCCESS){
            videoRemoteDataSource.uploadVideoFirestore(video)
        }else result
    }

    override suspend fun deleteVideoFirestore(video: Video): FirebaseResponse<Nothing> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteVideoStorage(video: Video): FirebaseResponse<Nothing> {
        TODO("Not yet implemented")
    }

}
