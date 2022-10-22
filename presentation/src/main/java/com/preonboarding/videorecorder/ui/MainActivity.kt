package com.preonboarding.videorecorder.ui

import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Video
import com.google.android.material.snackbar.Snackbar
import com.preonboarding.videorecorder.databinding.ActivityMainBinding
import com.preonboarding.videorecorder.ui.adapter.VideoListPagingAdapter
import com.preonboarding.videorecorder.util.DateUtil

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val mainViewModel: MainViewModel by viewModels()
    private val adapter: VideoListPagingAdapter by lazy { VideoListPagingAdapter(createThumbNail()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.vm = mainViewModel
        binding.lifecycleOwner = this

        initRecyclerView()
        getRecordUrlData()
        initData()
    }

    private fun initData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.videoList.collectLatest {
                    adapter.submitData(it)
                    if(adapter.itemCount == 0){
                        binding.emptyTextView.visibility = View.VISIBLE
                    }else{
                        binding.emptyTextView.visibility = View.GONE
                    }
                }
            }
        }

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
    }

    private fun initRecyclerView() = with(binding) {
        videoRecyclerView.adapter = adapter

        ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.START or ItemTouchHelper.END
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ): Boolean =
                    false


                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    lifecycleScope.launch {
                        val position = viewHolder.absoluteAdapterPosition
                        mainViewModel.deleteVideo(adapter.snapshot().items[position])
                        adapter.refresh()
                    }
                }
            }
        ).attachToRecyclerView(videoRecyclerView)
    }

    private fun getRecordUrlData() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val uri = it.data?.getStringExtra("url") ?: ""

                    val cur = DateUtil.getTime()
                    mainViewModel.uploadVideoList(
                        Video(
                            "$cur.mp4",
                            cur,
                            uri
                        )
                    )
                }
            }

        binding.recordButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            activityResultLauncher.launch(intent)
        }
    }

    private fun createThumbNail(): (String) -> Bitmap? = { uri ->
        val cursor: Cursor? =
            contentResolver.query(
                uri.toUri(), null, null, null, null
            )
        cursor?.moveToNext()

        // 정확한 저장 경로 파악
        val path: String? = cursor?.getString(cursor.getColumnIndex("_data"))

        cursor?.close()

        if (path != null) {
            val retriever = MediaMetadataRetriever()

            retriever.setDataSource(path)

            // 영상에서 1초되는 부분을 썸네일로 지정
            retriever.getFrameAtTime(1 * 1000000)
        } else {
            null
        }
    }

    override fun onResume() {
        super.onResume()
        binding.videoRecyclerView.initializePlayer()
    }

    override fun onStop() {
        super.onStop()

        binding.videoRecyclerView.releasePlayer()
    }
}