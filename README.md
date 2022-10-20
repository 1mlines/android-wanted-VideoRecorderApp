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
    -멀티 윈도우 지원으로 인해 onStop 호출이 보장되고, 만일 동영상이 일시정지 상태에서도 활동은 여전히 표시되므로, onStop에서 해제
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

- 동영상 플레이어 컨트롤러
  

