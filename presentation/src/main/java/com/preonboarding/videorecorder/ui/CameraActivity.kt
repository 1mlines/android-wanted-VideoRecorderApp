package com.preonboarding.videorecorder.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.preonboarding.videorecorder.databinding.ActivityCameraBinding
import com.preonboarding.videorecorder.databinding.ActivityMainBinding

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}