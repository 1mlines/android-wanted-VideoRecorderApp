package com.preonboarding.videorecorder.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.Video
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    //todo flow, real data
    private val _videoList = MutableLiveData<List<Video>>().apply { value =
        listOf(
            Video(0L,"abc","abc"),
            Video(1L,"abc","abc"),
            Video(2L,"abc","abc"),
            Video(3L,"abc","abc"),
        )
    }
    val videoList: LiveData<List<Video>>
        get() = _videoList
}