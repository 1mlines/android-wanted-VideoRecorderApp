package com.preonboarding.videorecorder.presentation.ui.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.videorecorder.domain.model.Video
import com.preonboarding.videorecorder.domain.usecase.SaveVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val saveVideoUseCase: SaveVideoUseCase,
) : ViewModel() {

    fun saveVideo(video: Video) {
        viewModelScope.launch {
            saveVideoUseCase.invoke(video)
        }
    }
}
