package com.preonboarding.videorecorder.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.domain.model.FirebaseState
import com.example.domain.model.Video
import com.example.domain.usecase.DeleteVideoFirestoreUseCase
import com.example.domain.usecase.DeleteVideoStorageUseCase
import com.example.domain.usecase.GetVideoListUseCase
import com.example.domain.usecase.UploadVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getVideoListUseCase: GetVideoListUseCase,
    private val uploadVideoUseCase: UploadVideoUseCase,
    private val deleteVideoStorageUseCase: DeleteVideoStorageUseCase,
    private val deleteVideoFirestoreUseCase: DeleteVideoFirestoreUseCase,
) : ViewModel() {
    val videoList = getVideoListUseCase().cachedIn(viewModelScope)
    private var _videoState = MutableSharedFlow<VideoListState>()
    val videoState = _videoState.asSharedFlow()

    fun uploadVideoList(video: Video) {
        viewModelScope.launch {
            _videoState.emit(VideoListState.Loading)
            when (uploadVideoUseCase(video).state) {
                FirebaseState.SUCCESS -> {
                    _videoState.emit(VideoListState.Update)
                }
                FirebaseState.FAILURE -> {
                    _videoState.emit(VideoListState.Failure)
                }
            }
        }
    }

    fun deleteVideo(video: Video) {
        viewModelScope.launch {
            _videoState.emit(VideoListState.Loading)
            if (deleteVideoStorageUseCase(video).state == FirebaseState.SUCCESS &&
                deleteVideoFirestoreUseCase(video).state == FirebaseState.SUCCESS
            ) {
                _videoState.emit(VideoListState.Update)
            } else {
                _videoState.emit(VideoListState.Failure)
            }
        }
    }
}