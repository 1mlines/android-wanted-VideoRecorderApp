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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getVideoListUseCase: GetVideoListUseCase,
    private val saveVideoUseCase: SaveVideoUseCase,
    private val deleteVideoUseCase: DeleteVideoUseCase,
) : ViewModel() {
    private val _videoList = MutableLiveData<List<Video>>().apply {
        value =
            listOf(
                Video(1, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4", "2020-02-22"),
                Video(2, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4", "2020-02-22"),
                Video(3, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4", "2020-02-22"),
                Video(4, "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4", "2020-02-22")
            )
    }
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
            Timber.tag("asdf").e(video.id.toString())
            //deleteVideoUseCase.invoke(video)
        }
    }
}