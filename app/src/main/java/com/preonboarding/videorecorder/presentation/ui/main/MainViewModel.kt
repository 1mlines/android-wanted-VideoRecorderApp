package com.preonboarding.videorecorder.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.videorecorder.domain.model.Video
import com.preonboarding.videorecorder.domain.usecase.DeleteVideoUseCase
import com.preonboarding.videorecorder.domain.usecase.GetVideoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getVideoListUseCase: GetVideoListUseCase,
    private val deleteVideoUseCase: DeleteVideoUseCase,
) : ViewModel() {

    private val _videoList = MutableStateFlow<List<Video>>(emptyList())
    val videoList: StateFlow<List<Video>> get() = _videoList

    init {
        getVideoList()
    }

    private fun getVideoList() {
        viewModelScope.launch {
            getVideoListUseCase.invoke().stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                emptyList()
            ).collectLatest {
                _videoList.emit(it)
            }
        }
    }

    fun deleteVideo(video: Video) {
        viewModelScope.launch {
            deleteVideoUseCase.invoke(video)
        }
    }
}
