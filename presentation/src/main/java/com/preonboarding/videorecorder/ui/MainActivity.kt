package com.preonboarding.videorecorder.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Video
import com.preonboarding.videorecorder.databinding.ActivityMainBinding
import com.preonboarding.videorecorder.ui.adapter.VideoListPagingAdapter
import com.preonboarding.videorecorder.util.DateUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

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
        mainViewModel.liveData.observe(this) {
            if (it) {
                adapter.refresh()
            }
        }
    }

    private fun initData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.videoList.collectLatest {
                    adapter.submitData(it)
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
                binding.urlTextView.text = uri
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
}