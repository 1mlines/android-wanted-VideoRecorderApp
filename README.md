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
