package com.example.domain.usecase

import com.example.domain.model.Video

/**
 * @Created by 김현국 2022/10/18
 * @Time 2:42 PM
 */
interface InsertVideoDataUseCase {
    suspend operator fun invoke(video: Video)
}