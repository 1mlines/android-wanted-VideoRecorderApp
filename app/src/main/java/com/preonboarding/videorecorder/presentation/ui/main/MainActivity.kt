package com.preonboarding.videorecorder.presentation.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.preonboarding.videorecorder.R
import com.preonboarding.videorecorder.databinding.ActivityMainBinding
import com.preonboarding.videorecorder.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutResourceId: Int = R.layout.activity_main
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        setUpDataBinding()
    }

    private fun setUpDataBinding() {
        viewModel.videoList.observe(this) {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}