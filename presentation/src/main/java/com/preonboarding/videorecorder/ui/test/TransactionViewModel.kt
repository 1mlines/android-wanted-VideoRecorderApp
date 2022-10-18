package com.preonboarding.videorecorder.ui.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Video
import com.example.domain.usecase.DeleteVideoDataUseCase
import com.example.domain.usecase.InsertVideoDataUseCase
import com.preonboarding.videorecorder.util.DateUtil.getCurrentTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Created by 김현국 2022/10/18
 * @Time 2:55 PM
 */

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val insertVideoDataUseCase: InsertVideoDataUseCase,
    private val deleteVideoDataUseCase: DeleteVideoDataUseCase
) : ViewModel() {

    fun insertVideoData(uri: String) {
        viewModelScope.launch {
            insertVideoDataUseCase(
                video = Video.EMPTY.copy(
                    id = 0,
                    uri = "test",
                    date = getCurrentTime()
                )
            )
        }
    }

    fun deleteVideoData(id: Long) {
        viewModelScope.launch {
            deleteVideoDataUseCase(id = id)
        }
    }
}
