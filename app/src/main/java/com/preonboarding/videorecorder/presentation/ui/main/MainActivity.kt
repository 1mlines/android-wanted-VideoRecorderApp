package com.preonboarding.videorecorder.presentation.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.preonboarding.videorecorder.R
import com.preonboarding.videorecorder.databinding.ActivityMainBinding
import com.preonboarding.videorecorder.domain.model.Video
import com.preonboarding.videorecorder.presentation.base.BaseActivity
import com.preonboarding.videorecorder.presentation.ui.playvideo.VideoPlayActivity
import com.preonboarding.videorecorder.presentation.ui.record.RecordActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutResourceId: Int = R.layout.activity_main
    private val viewModel: MainViewModel by viewModels()
    private lateinit var mainAdapter: MainAdapter

    lateinit var factory: DataSource.Factory
    private lateinit var mediaSource: ProgressiveMediaSource
    private lateinit var exoPlayer: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        initView()
        setUpDataBinding()
    }

    private fun initView() {
        binding.apply {
            btRecord.setOnClickListener {
                startActivity(Intent(this@MainActivity, RecordActivity::class.java))
            }
            mainAdapter = MainAdapter(itemClick = {
                itemClick(it)
            }, deleteClick = {
                deleteClick(it)
            }, itemLongClick = { uri ->
                playPlayer(uri)
            }
            )
            rvRecordList.adapter = mainAdapter
        }
    }

    private fun initPlay() {
        factory = DefaultDataSource.Factory(this)
        exoPlayer = ExoPlayer.Builder(this).build()
    }

    private fun playPlayer(videoUri: String): ExoPlayer {
        mediaSource = ProgressiveMediaSource.Factory(factory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(videoUri)))

        exoPlayer.apply {
            setMediaSource(mediaSource)
            prepare()
            play()

            lifecycleScope.launch {
                delay(5000L)
                pausePlayer()
            }
        }
        return exoPlayer
    }

    private fun setUpDataBinding() {
        this.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.videoList.collect {
                    mainAdapter.submitList(it)
                }
            }
        }
    }

    private fun itemClick(video: Video) {
        val intent = Intent(this, VideoPlayActivity::class.java)
        intent.putExtra("videoUri", video.uri)
        startActivity(intent)
    }

    private fun pausePlayer() {
        exoPlayer.apply {
            seekTo(0)
            playWhenReady = false
        }
    }

    private fun deleteClick(video: Video) {
        viewModel.deleteVideo(video)
    }

    override fun onResume() {
        super.onResume()
        initPlay()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }
}
