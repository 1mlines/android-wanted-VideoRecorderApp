package com.example.domain.usecase

import com.example.domain.model.FirebaseResponse
import com.example.domain.model.Video

interface GetVideoListUseCase {
    suspend operator fun invoke(): FirebaseResponse<List<Video>>
}