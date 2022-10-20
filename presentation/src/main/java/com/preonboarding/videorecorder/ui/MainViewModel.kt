package com.preonboarding.videorecorder.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.FirebaseState
import com.example.domain.model.Video
import com.example.domain.usecase.GetVideoListUseCase
import com.example.domain.usecase.UploadVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getVideoListUseCase: GetVideoListUseCase,
    private val uploadVideoUseCase: UploadVideoUseCase
) : ViewModel() {
    //todo flow, loading
    private val _videoList = MutableLiveData<List<Video>>()
    val videoList: LiveData<List<Video>>
        get() = _videoList

    fun getMyVideoList() {
        viewModelScope.launch {
            val result = getVideoListUseCase()
            _videoList.value = result.result ?: emptyList()
            Timber.d(result.result.toString())
        }
    }

    fun uploadVideoList(video: Video){
        viewModelScope.launch {
            val result = uploadVideoUseCase(video)
            Timber.d(result.toString())
        }
    }
}