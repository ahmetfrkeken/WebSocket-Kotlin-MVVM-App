package com.ahmetfarukeken.websocketkotlinchatapp.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.ahmetfarukeken.websocketkotlinchatapp.databinding.ActivityMainBinding
import com.ahmetfarukeken.websocketkotlinchatapp.ui.BaseActivity
import com.ahmetfarukeken.websocketkotlinchatapp.ui.fragment.MainViewModel

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }
}
