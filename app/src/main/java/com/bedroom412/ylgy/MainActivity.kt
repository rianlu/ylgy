package com.bedroom412.ylgy

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.bedroom412.ylgy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initStatusBar()

        binding.settingsBtn.setOnClickListener {  }
        binding.scanBtn.setOnClickListener {  }
        binding.searchBtn.setOnClickListener {  }
        // 打开播放器
        binding.vinylRecordView.setOnClickListener {  }
        binding.songCover.setOnClickListener {  }
    }

    private fun initStatusBar() {
        binding.songTitle.isSelected = true
        window.statusBarColor = Color.TRANSPARENT
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }
}