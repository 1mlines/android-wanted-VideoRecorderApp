package com.preonboarding.videorecorder.data.repository

import com.preonboarding.videorecorder.data.source.FirebaseDataSource
import com.preonboarding.videorecorder.domain.model.Video
import com.preonboarding.videorecorder.domain.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
): VideoRepository {
    override suspend fun saveVideo(video: Video) {
        firebaseDataSource.saveVideo(video)
    }

    override suspend fun deleteVideo(video: Video) {
        firebaseDataSource.deleteVideo(video)
    }

    override suspend fun getVideoList(): Flow<List<Video>> {
        return firebaseDataSource.getVideoList()
    }
}
