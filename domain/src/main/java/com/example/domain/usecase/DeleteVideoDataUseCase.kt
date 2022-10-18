package com.example.domain.usecase

/**
 * @Created by 김현국 2022/10/18
 * @Time 2:42 PM
 */
interface DeleteVideoDataUseCase {
    suspend operator fun invoke(id: Long)
}
