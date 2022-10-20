package com.preonboarding.videorecorder.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.domain.model.Video
import com.preonboarding.videorecorder.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>


    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.vm = mainViewModel
        binding.lifecycleOwner = this
        mainViewModel.getMyVideoList()
        binding.videoRecyclerView.setOnClickListener {
            val cur = System.currentTimeMillis()
            mainViewModel.uploadVideoList(
                Video(
                    "$cur.mp4",
                    cur,
                    "content://media/external/video/media/29"
                )
            )
        }

        getRecordUrlData()
    }

    private fun getRecordUrlData() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val address = it.data?.getStringExtra("url") ?: ""
                binding.urlTextView.text = address
            }
        }

        binding.recordButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            activityResultLauncher.launch(intent)
        }
    }
}