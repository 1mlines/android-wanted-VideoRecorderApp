package com.example.domain.usecase

import com.example.domain.model.FirebaseResponse
import com.example.domain.model.Video

interface DeleteVideoFirestoreUseCase {
    suspend operator fun invoke(video: Video): FirebaseResponse<Nothing>
}