package com.preonboarding.videorecorder.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.preonboarding.videorecorder.R
import com.preonboarding.videorecorder.databinding.ActivityMainBinding
import com.preonboarding.videorecorder.domain.model.Video
import com.preonboarding.videorecorder.presentation.base.BaseActivity
import com.preonboarding.videorecorder.presentation.ui.play.PlayActivity
import com.preonboarding.videorecorder.presentation.ui.record.RecordActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutResourceId: Int = R.layout.activity_main
    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        setUpDataBinding()

        initView()

    }

    private fun initView() {
        binding.apply {
            btRecord.setOnClickListener {
                startActivity(Intent(this@MainActivity, RecordActivity::class.java))
            }
            val mainAdapter = MainAdapter(itemClick = {
                itemClick(it)
            }, deleteClick = {
                deleteClick(it)
            })
            rvRecordList.adapter = mainAdapter
            viewModel!!.videoList.observe(this@MainActivity) {
                mainAdapter.submitList(it)
            }
        }
    }

    private fun setUpDataBinding() {
        viewModel.videoList.observe(this) {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun itemClick(video: Video) {
        val intent = Intent(this, PlayActivity::class.java)
        intent.putExtra("video", video.uri)
        startActivity(intent)
    }

    private fun deleteClick(video: Video) {
        viewModel.deleteVideo(video)
    }
}