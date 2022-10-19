package com.preonboarding.videorecorder.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.preonboarding.videorecorder.domain.model.Video
import com.preonboarding.videorecorder.domain.usecase.DeleteVideoUseCase
import com.preonboarding.videorecorder.domain.usecase.GetVideoListUseCase
import com.preonboarding.videorecorder.domain.usecase.SaveVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getVideoListUseCase: GetVideoListUseCase,
    private val saveVideoUseCase: SaveVideoUseCase,
    private val deleteVideoUseCase: DeleteVideoUseCase
): ViewModel(){
    private val _videoList = MutableLiveData<List<Video>>()
    val videoList: LiveData<List<Video>> get() = _videoList

    fun getVideoList() {
        viewModelScope.launch {
            _videoList.postValue(getVideoListUseCase.invoke())
        }
    }

    fun saveVideo(video: Video) {
        viewModelScope.launch {
            saveVideoUseCase.invoke(video)
        }
    }

    fun deleteVideo(video: Video) {
        viewModelScope.launch {
            deleteVideoUseCase.invoke(video)
        }
    }
}