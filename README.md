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
