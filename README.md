# 👨‍👨‍👦‍👦 Members

<div align="center">
  <table style="font-weight : bold">
      <tr>
          <td align="center">
              <a href="https://github.com/hoyahozz">                 
                  <img alt="김정호" src="https://avatars.githubusercontent.com/hoyahozz" width="80" />            
              </a>
          </td>
          <td align="center">
              <a href="https://github.com/gyurim">                 
                  <img alt="박규림" src="https://avatars.githubusercontent.com/gyurim" width="80" />            
              </a>
          </td>
          <td align="center">
              <a href="https://github.com/INAH-PAK">                 
                  <img alt="박인아" src="https://avatars.githubusercontent.com/INAH-PAK" width="80" />            
              </a>
          </td>
          <td align="center">
              <a href="https://github.com/leehandsub">                 
                  <img alt="이현섭" src="https://avatars.githubusercontent.com/leehandsub" width="80" />            
              </a>
          </td>
      </tr>
      <tr>
          <td align="center">김정호</td>
          <td align="center">박규림</td>
          <td align="center">박인아</td>
          <td align="center">이현섭</td>
      </tr>
  </table>
</div>

# 🤝 Convention

## 📚 Branch

- `{behavior}/issue-{number}-{something}` 
- e.g. : `feature/issue-007-data-module`

## 🤔 Issue

- `[{behavior}] {something}`
- e.g. : `[FEATURE] 프로젝트 세팅`

## 🤲 Pull Request

- `[ISSUE-{number}] {something}`
- e.g. : `[ISSUE-007] 데이터 모듈 추가`
- 문장 단위가 아닌, **단어 단위**로 제목을 작성합니다.

## 😊 Commit

- `feat - {something}` : 새로운 기능을 추가했을 때
- `fix - {something}` : 기능 중 버그를 수정했을 때
- `design - {something}` : 디자인 일부를 변경했을 때
- `refactor - {something}` : 코드를 재정비 하였을 때
- `chore - {something}` : 빌드 관련 업무를 수정하거나 패키지 매니저를 수정했을 때
- `docs - {something}` : README와 같은 문서를 변경했을 때
- 문장 단위가 아닌, **단어 단위**로 제목을 작성합니다.

## ⌨️ Coding
- [android-style-guide](https://github.com/PRNDcompany/android-style-guide)의 코딩 컨벤션과 동일하게 진행합니다.

## ⚙️ Tech Stack
`Clean Architecture`, `MVVM`, `Kotlin`, `Hilt`, `Coroutine`, `Flow`, `Firebase Realtime DB`, `Firebase Storage`, `Lifecycle`, `Databinding`, `Livedata`, `ExoPlayer`, `CameraX`, `Timber`

## 박규림
- 담당한 일
  - Base Architecture 설계
  - 영상 long click 시 처음 5초간 플레이
- 더 해야할 점
  - 현재 영상 끝까지 재생되도록 구현되었는데, 5초간 재생으로 변경하기

### Base Architecture
- Clean Architecture 적용
<img width="700" alt="스크린샷 2022-10-06 오후 10 59 44" src="https://user-images.githubusercontent.com/31344894/194359965-720b5e68-81ef-4479-95ad-321c3e445e2c.png">

- UseCase
<img width="811" alt="스크린샷 2022-10-21 오전 3 44 31" src="https://user-images.githubusercontent.com/31344894/197031940-ad8484b8-85bb-45d6-b476-354f4dbbdfce.png">

- Video Model 
```kotlin
data class Video(
    val id: Int,
    val uri: String,
    val date: String
) {
    companion object {
        val EMPTY = Video(
            id = 0,
            uri = "",
            date = ""
        )
    }
}
```

### 영상 Long Click 시 처음 5초간 플레이
- 시연 영상  

https://user-images.githubusercontent.com/31344894/197033577-5c258bf7-0155-457b-a75d-8a05db13679f.mp4

- ExoPlayer 주요 컴포넌트 소개 
  - PlayerView: 비디오를 불러와 실제 UI에 뿌려줄 때 사용되는 UI 요소
  - ExoPlayer: 미디어 플레이어 기능 제공 
  - MediaSource: ExoPlayer에서 재생될 미디어를 정의하고 제공하는 역할
    - ProgressiveMediaSource: 일반 미디어 파일 형식
  - DataSource: 영상 스트림을 읽을 수 있는 형태로 표현

- Long Click Listener 적용 
```kotlin
binding.itemNewsPlayerView.setOnClickListener {
    itemClick(video)
}

binding.itemNewsPlayerView.setOnLongClickListener {
    mediaSource = ProgressiveMediaSource.Factory(factory)
        .createMediaSource(MediaItem.fromUri(Uri.parse(video.uri)))

    exoPlayer.apply {
        setMediaSource(mediaSource)
        prepare()
        play()
    }

    binding.itemNewsPlayerView.apply {
        player = exoPlayer
        useController = false // 자동재생이므로 controlview는 사용되지 않도록 적용 
    }
    true 
}
```
- background로 이동 시, player 재시작 준비
```kotlin
override fun onPause() {
    super.onPause()
    mainAdapter.exoPlayer.apply {
        seekTo(0)
        playWhenReady = false
    }
}
```

## 이현섭

화면전환

```kotlin
private fun itemClick(video: Video) {
        val intent = Intent(this, PlayActivity::class.java)
        intent.putExtra("video", video.uri)
        startActivity(intent)
    }

    private fun deleteClick(video: Video) {
        viewModel.deleteVideo(video)
    }
```

녹화 목록 어답터 구현

```kotlin
class MainAdapter(
    private val itemClick: (Video) -> Unit,
    private val deleteClick: (Video) -> Unit,
) : ListAdapter<Video, MainAdapter.MainViewHolder>(diffUtil) {

    inner class MainViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(video: Video) {
            binding.video = video
            binding.cvVideoItem.setOnClickListener {
                itemClick(video)
            }
            binding.btnDelete.setOnClickListener {
                deleteClick(video)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }
}
```

아쉬운점이나 더해야할점

- 페이징을 사용해서 리사이클러뷰 구현
- ui가 보기 좋지 않음

## 김정호

![스크린샷 2022-10-21 오전 8 46 19 중간](https://user-images.githubusercontent.com/85336456/197079846-661e6c17-cc34-4ca0-a88c-8b6864ee71db.jpeg)

- `CameraX` 라이브러리를 통해 전-후면 동영상 촬영 기능 구현
- 애플리케이션 실행에 필요한 권한 요청 코드 구현
- 녹화 영상 갤러리 저장 및 `Firebase Storage` 에 저장
- `Firebase Realtime Database` 의 `CRUD` 구현

```kotlin

  private fun captureVideo() {
      val videoCapture = this.videoCapture ?: return

      binding.videoCaptureButton.isEnabled = false

      // 진행 중인 녹음이 있으면 중지, 현재 녹음을 해제한다.
      // 캡처한 비디오 파일이 애플리케이션에서 사용할 준비가 됐을 때 알림을 받게 된다.
      val curRecording = recording
      if (curRecording != null) {
          curRecording.stop()
          recording = null
          return
      }

      val name = SimpleDateFormat(FILENAME_FORMAT, Locale.KOREA)
          .format(System.currentTimeMillis())
      val contentValues = ContentValues().apply {
          put(MediaStore.MediaColumns.DISPLAY_NAME, name)
          put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
          if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
              put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
          }
      }

      val mediaStoreOutputOptions = MediaStoreOutputOptions
          .Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
          .setContentValues(contentValues)
          .build()

      // videoCapture 의 레코더에 대한 출력 옵션을 구성하고 오디오 녹음 활성화
      recording = videoCapture.output
          .prepareRecording(this, mediaStoreOutputOptions)
          .apply {
              if (PermissionChecker.checkSelfPermission(
                      this@RecordActivity,
                      Manifest.permission.RECORD_AUDIO
                  ) ==
                  PermissionChecker.PERMISSION_GRANTED
              ) {
                  withAudioEnabled()
              }
          }
          .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
              when (recordEvent) {
                  is VideoRecordEvent.Start -> {
                      binding.videoCaptureButton.apply {
                          text = "stop capture"
                          isEnabled = true
                      }
                  }
                  is VideoRecordEvent.Finalize -> {
                      if (!recordEvent.hasError()) {
                          val msg = "Video capture succeeded: ${recordEvent.outputResults.outputUri}"
                          Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                      } else {
                          recording?.close()
                          recording = null
                      }
                      binding.videoCaptureButton.apply {
                          text = "start capture"
                          isEnabled = true
                      }
                  }
              }
          }
  }

  private fun startCamera() {
      // 카메라 생명 주기를 현재 라이프사이클 오너에게 연결하는데 사용된다.
      // CameraX 는 생명 주기를 인식하므로 카메라를 열고 닫는 작업이 필요하지 않다.
      cameraProviderFuture = ProcessCameraProvider.getInstance(this)

      cameraProviderFuture.addListener({

          // 애플리케이션 프로세스 내에서 카메라의 생명주기를 라이프사이클 오너에 바인딩하는데 사용된다.
          val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

          // Preview 클래스 생성
          val preview: Preview = Preview.Builder()
              .build()
              .also {
                  // 유즈케이스(Preview)와 previewView 를 연결
                  it.setSurfaceProvider(binding.previewView.surfaceProvider)
              }

          // Recorder 클래스 생성
          val recorder = Recorder.Builder()
              .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
              .build()
          videoCapture = VideoCapture.withOutput(recorder)

          val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

          try {
              cameraProvider.unbindAll() // 이전에 바인딩된 유즈케이스 해제
              cameraProvider
                  .bindToLifecycle(
                      this as LifecycleOwner,
                      cameraSelector,
                      preview,
                      videoCapture
                  ) // 라이프 사이클 연결
          } catch (e: Exception) {
              Toast.makeText(this, "Use case binding failed $e", Toast.LENGTH_SHORT).show()
          }
      }, ContextCompat.getMainExecutor(this))
  }
```


## 박인아

역할 : 비디오 플레이 화면 구현



#### 활용 library : ExoPlayer ( Android용 어플리케이션 레벨 미디어 플레이어)

- 다른 미디어 플레이어 API와의 비교

  - MediaPlayer 
    - MediaPlayer 보다 커스터마이징이 매우 용이.
    - 배터리 소모측면에서, 비디오 재생시 소비되는 전력이 거의 비슷함.
  - Jetpack Media3
    - Jetpack Media3에서의 비디오 재생은 ExoPlayer를 사용.
    - MediaSession 다루기가 쉽지만, 현재 앱에선 동영상 재생 기능만 필요하므로 MediaSession 불필요.


#### API 수준에 따른 동영상 player와 앱 수명 주기의 연결


- 동영상 플레이어 객체 생성
  - ExoPlayer 인터페이스를 다목적으로 편리하게 구현한 SimpleExoPlayer가 Deprecated 되어, ExoPlayer를 빌더 패턴을 이용하여 객체 생성.
  
- 동영상 플레이어 객체 생성
  - Android API 수준 24 이상
    - 멀티 윈도우를 지원하므로, 분활 윈도우 모드로 활성화되지 않으므로 onStart에서 초기화.
  - Android API 수준 24 이하
    - 리소스 포착시까지 기다려야 하므로, onResume에서초기화 
    
- 재생될 미디어 항목
    - 미디어 파일의 URI 를 사용함.
    
```kotlin

    fun initPlay(){
        player = ExoPlayer.Builder(this).build()
        playerView.player = player
        binding.playControlView.player = player

        player!!.also {
            it.setMediaItem(mediaItem)
            it.playWhenReady = playWhenReady
            it.seekTo(currentWindow, playbackPosition)
            it.prepare()
        }

    }

    override fun onStart() {
        if (Util.SDK_INT >= 24) {
            initPlay()
        }
        super.onStart()
    }

    override fun onResume() {
        if ((Util.SDK_INT < 24 || player == null)) {
            initPlay()
        }
        super.onResume()
    }
    
```
    
- 동영상 플레이어 객체의 해제
  - Android API 수준 24 이상
    - 멀티 윈도우 지원으로 인해 onStop 호출이 보장된다. 멀티 윈도우 모드에서 한 앱이 포커스를 가질 경우, 그 외의 모든 앱은 일시중지 됨. 이때 일시중지 상태의 동영상 플레이 Activity도 다른 윈도우에서 완전하게 보일 수 있어서 onStop에서 객체 해제.
  - Android API 수준 24 이하
    - onStop의 호출이 보장되지 않음. 그러므로 onPause 에서 플레이어 해제
- 동영상 플레이어 객체 해체시 권장사항
  - 앱이 활동 수명 주기의 다양한 상태에서의 기존 재생 정보 유지
    ex) 앱의 회전, 포그라운드 상태 -> 백그라운드 상태 -> 포그라운드 상태 , 다른 앱 시작 후 앱 실행 등의 경우의 재생 위치 

    
 ```kotlin

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentMediaItemIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }


    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

     override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

```

- 컨트롤러 
  - ExoPlayer의 기본 controller 레이아웃 커스터마이징.

#### 시연 스크린샷

![Screenshot_20221021_054628 크게 작게](https://user-images.githubusercontent.com/95750706/197056463-d9fe4c68-576f-4f3a-a496-771c5b240002.jpeg)

- 아쉬웠던 점 
  - Media3와 ExoPlayer 를 스스로 비교해보지 못한 점.
- 남은 일
  - 동영상 목록 리스트에서 보여질 동영상 썸네일 생성 기능 구현

