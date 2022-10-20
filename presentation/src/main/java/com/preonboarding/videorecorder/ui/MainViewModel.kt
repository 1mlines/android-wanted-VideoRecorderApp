package com.preonboarding.videorecorder.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.di.DispatcherModule
import com.example.domain.model.FirebaseState
import com.example.domain.model.Video
import com.example.domain.usecase.DeleteVideoFirestoreUseCase
import com.example.domain.usecase.DeleteVideoStorageUseCase
import com.example.domain.usecase.GetVideoListUseCase
import com.example.domain.usecase.UploadVideoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getVideoListUseCase: GetVideoListUseCase,
    private val uploadVideoUseCase: UploadVideoUseCase,
    private val deleteVideoStorageUseCase: DeleteVideoStorageUseCase,
    private val deleteVideoFirestoreUseCase: DeleteVideoFirestoreUseCase,
    @DispatcherModule.DispatcherIO private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    var videoList = getVideoListUseCase().cachedIn(viewModelScope).flowOn(dispatcher)
    var liveData = MutableLiveData<Boolean>().apply { value = false }

    fun uploadVideoList(video: Video){
        viewModelScope.launch {
            val result = uploadVideoUseCase(video)
            if(result.state == FirebaseState.SUCCESS){
                liveData.value = true
            }
        }
    }

    fun deleteVideo(video: Video){
        viewModelScope.launch {
            val deleteStorageResult = deleteVideoStorageUseCase(video)
            val deleteFirestoreResult = deleteVideoFirestoreUseCase(video)
            if(deleteFirestoreResult.state == FirebaseState.SUCCESS &&
                deleteStorageResult.state == FirebaseState.FAILURE){
                liveData.value = true
            }
        }
    }
}