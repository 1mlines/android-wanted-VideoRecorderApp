# 원티드 프리온보딩 안드로이드
# 2팀 VideoRecorderApp

## 팀원

<div align="center">
  <table style="font-weight : bold">
      <tr>
          <td align="center">
              <a href="https://github.com/tjrkdgnl">                 
                  <img alt="tjrkdgnl" src="https://avatars.githubusercontent.com/tjrkdgnl" width="80" />            
              </a>
          </td>
          <td align="center">
              <a href="https://github.com/dudwls901">                 
                  <img alt="gyurim" src="https://avatars.githubusercontent.com/u/66052467?v=4" width="80" />            
              </a>
          </td>
          <td align="center">
              <a href="https://github.com/014967 ">                 
                  <img alt="lsy524" src="https://avatars.githubusercontent.com/014967 " width="80" />            
              </a>
          </td>
          <td align="center">
              <a href="https://github.com/DavidKwon7">                 
                  <img alt="davidKwon7" src="https://avatars.githubusercontent.com/u/70066242?v=4" width="80" />            
              </a>
          </td>
      </tr>
      <tr>
          <td align="center">서강휘</td>
          <td align="center">김영진</td>
          <td align="center">김현국</td>
          <td align="center">권혁준</td>
      </tr>
  </table>
</div>

## 김현국
### 맡은 역할
- 아키텍처 설계

### 아키텍처 구조
<img src= "https://user-images.githubusercontent.com/62296097/196941657-8179f6f5-5b1f-4002-9f55-5693bff08655.png">

- Domain Layer는 Repository interface와 UseCase interface을 포함하고 있으며 인터페이스의 구현체는 Data Layer에서 구현하였습니다.

### 프로젝트 구조 - 멀티모듈
<img src="https://user-images.githubusercontent.com/62296097/196951134-165fec0b-6a07-42d5-9431-9f71a2f11aa9.png">


- Presentation Layer
    - UI과 관련된 작업으로 구성되어있습니다.
    - 대표적으로 Activity, Fragment, ViewModel이 있습니다.

- Domain Layer
    - 비지니스 로직에서 수행되어져야할 행동들을 Interface로 정의하고 제공됩니다.
    - Presentation Layer에 제공되는 비지니스 모델이 포함됩니다. 
    

- Data Layer
    - Domain Layer에서 정의된 Interface(Repository, Usecase)의 구현체와 DataSource가 존재합니다.
    - Hilt에 의해 인터페이스의 구현체가 제공됩니다. 
    - DataSource는 Local과 Remote로 나뉘어 각각 Room Database와 Firebase에 접근합니다.



- Gradle
    - KTS를 통해 gradle를 Kotlin Script로 구성했습니다. 
- Version Catalog
    - Library와 Plugin의 버전을 관리합니다.



___

## 김영진
### 맡은 역할
- 영상 업로드 (Firebase Storage, Firebase Firestore)
- 영상 삭제 (Firebase Storage, Firebase Firestore)
- 영상 리스트 가져오기 (Firebase Firestore, Paging3)

### 영상 업로드
- 결과값을 FirebaseResponse로 매핑 (data-domain)
- FirebaseResponse를 UiState로 매핑 (presentation)
- UiState에 따라 리스트 업데이트 or 프로그레스바 노출 or 에러 메시지 노출

```kotlin
//Video

data class Video(
    val name: String = "",
    val publishedAt: Long = 0L,
    val uri: String = ""
) 

```

```kotlin
//FirebaseResponse

data class FirebaseResponse<T>(
    val state : FirebaseState,
    val result : T? = null
)

enum class FirebaseState{
    SUCCESS,
    FAILURE,
}

```

```kotlin
//safeFirebaseCall

suspend fun <T> safeSetCall(callFunction: () -> Task<T>): FirebaseResponse<Nothing> {
    return try {
        callFunction.invoke().await()
        FirebaseResponse(FirebaseState.SUCCESS)
    } catch (e: Exception) {
        Timber.e("safeSetCall: 실패 $e")
        FirebaseResponse(FirebaseState.FAILURE)
    }
}

```

```kotlin
//VideoDataSourceImpl

    override suspend fun uploadVideoStorage(video: Video): FirebaseResponse<Nothing> {
        return safeSetCall {
            firebaseStorage.reference.child(video.name).putFile(Uri.parse(video.uri))
        }
    }

```

```kotlin
//VideoListState

sealed class VideoListState {

    object Failure : VideoListState()

    object Loading : VideoListState()

    object Update : VideoListState()
}

```



```kotlin
//MainViewModel

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

```



```kotlin
//MainActivity

lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.videoState.collectLatest {
                    when (it) {
                        VideoListState.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        VideoListState.Update -> {
                            binding.progressBar.isVisible = false
                            adapter.refresh()
                        }
                        VideoListState.Failure -> {
                            binding.progressBar.isVisible = false
                            Snackbar.make(binding.root, "오류가 발생했습니다.", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

```


https://user-images.githubusercontent.com/66052467/197009920-0bedb55f-5225-4880-80f1-664e014876ef.mov



___


## 서강휘
### 맡은 역할
- 비디오 플레이 화면 구현
- 5초간 영상 재생 

### CustomExoPlayerRecyclerView
https://user-images.githubusercontent.com/45396949/197003564-c1544155-0e61-42a5-8893-52cbf66d2e71.mp4

- scroll에 따라 exoPlayer가 동작하도록 만들기 위해서 Custom View를 구현했습니다. 

### 앱 백그라운드 진입 시, 리소스 할당 해제

```kotlin
   //CustomExoPlayerRecyclerView
   
    fun releasePlayer() {
        removePlayerView()

        player?.run {
            removeListener(exoPlayerListener)
            release()
        }

        player = null
    }

```

```kotlin
   //Activity
   
    override fun onStop() {
        super.onStop()
        binding.videoRecyclerView.releasePlayer()
    }

```

- CustomExoPlayerRecyclerView로부터 구현한 release 메소드를 액티비티 라이프싸이클에 따라 동작하도록 만들었습니다. 

### 포백그라운드 진입 시, 리소스 할당

```kotlin
   //CustomExoPlayerRecyclerView
   
    private fun initializePlayer(video: Video) {
        if (player == null) {
            player = ExoPlayer.Builder(context)
                .build()
                .also { exoPlayer ->
                    exoPlayerView?.player = exoPlayer

                    exoPlayer.setMediaItem(MediaItem.fromUri(video.uri))

                    exoPlayerListener = playerStateListener()
                    exoPlayer.addListener(exoPlayerListener)

                    exoPlayer.playWhenReady = true
                    exoPlayer.seekTo(0)
                    exoPlayer.prepare()
                }

        } else {
            player?.also { exoPlayer ->
                exoPlayerView?.player = exoPlayer

                exoPlayer.setMediaItem(MediaItem.fromUri(video.uri))

                exoPlayer.playWhenReady = true
                exoPlayer.seekTo(0)
                exoPlayer.prepare()
            }
        }
    }

```

```kotlin
   //Activity
   
    override fun onResume() {
        super.onResume()

        lifecycleScope.launch{
            delay(3000)
            binding.videoRecyclerView.playCurrentPosition(false)
        }
    }

```

- CustomExoPlayerRecyclerView로부터 구현한 initialize 메소드를 액티비티 라이프싸이클에 따라 동작하도록 만들었습니다. 


___

## 권혁준
### 맡은 역할
- 비디오 녹화 화면 구현 


[change.webm](https://user-images.githubusercontent.com/70066242/197023267-70f10ffb-92ce-4e9b-951f-4f8dcd5eb1d8.webm)


### 전면 / 후면  화면 전환
```kotlin
private fun changeFacing() {
        binding.changeLensButton.setOnClickListener {
            defaultCameraFacing = if (defaultCameraFacing == CameraSelector.DEFAULT_FRONT_CAMERA) {
                CameraSelector.DEFAULT_BACK_CAMERA
            } else {
                CameraSelector.DEFAULT_FRONT_CAMERA
            }
            try {
                startCamera(defaultCameraFacing)
            } catch (exc: Exception) {
                // Do nothing
            }
        }
    }
```

### 녹화 화면 
```kotlin
    private fun startCamera(defaultCameraFacing: CameraSelector) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            val recorder = Recorder.Builder()
                // 화질 설정 (비디오)
                .setQualitySelector(QualitySelector.from(Quality.HD))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            val cameraSelector = defaultCameraFacing

            try {
                cameraProvider.unbindAll()
                // video 추가
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, videoCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "startCamera fail: ", exc )
            }
        }, ContextCompat.getMainExecutor(this)
        )
    }
```

### Uri 생성 
```kotlin
val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH,
                    "Movies/CameraX-Video")
            }
        }
        
// uri 생성
val currentUri = contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
```

<img width="806" alt="스크린샷 2022-10-21 오전 2 54 20" src="https://user-images.githubusercontent.com/70066242/197022671-6cae4adb-7afb-45a7-8f82-15817123b27e.png">
https://kimdabang.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%A0%80%EC%9E%A5%EC%86%8C-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0-2-Scoped-Storage 참조


## Convention

### Branch Convention

```feat/<branch name>  ```

- e.g) ```feat/Base Architecture ```


### Commit convention

``` [prefix]: <commit content> ```

- e.g) ``` feat: DAO 개발완료 ```

- e.g) ``` fix: room crash 수정 ```

- e.g) ``` refactor: MVVM 아키텍처 구조 리팩토링 ```

### Issue Convention
``` [prefix] 작업할 내용 ```

- e.g) ``` [feat] base architecture 생성 ```
- e.g) ``` [fix] room crash 수정 ```
- e.g) ``` [refactor] Sensor구조 일부 수정 ```

- 브랜치를 생성하기 전, github issue를 생성해주세요.
- branch 명의 issue number는 해당 issue Number로 지정합니다.

### PR Convention
``` [Issue-#number] PR 내용 ```

- e.g) ``` [Issue-#7] Timer 추가 ``` 

-----
