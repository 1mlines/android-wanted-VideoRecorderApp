package com.preonboarding.videorecorder.ui

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
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

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val adapter: VideoListPagingAdapter by lazy { VideoListPagingAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val uri = it.data?.getStringExtra("url") ?: ""

                val cur = DateUtil.getTime()
                mainViewModel.uploadVideoList(
                    Video(
                        "$cur.mp4",
                        cur,
//                    "content://media/external/video/media/29",
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

    private fun createThumbNail(uri: String) {
        val cursor: Cursor? =
            contentResolver.query(
                uri.toUri(), null, null, null, null
            )
        cursor?.moveToNext()

        val path: String? = cursor?.getString(cursor.getColumnIndex("_data"))

        cursor?.close()

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