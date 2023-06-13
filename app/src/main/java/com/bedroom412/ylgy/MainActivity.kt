package com.bedroom412.ylgy

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.bedroom412.ylgy.activity.DownloadActivity
import com.bedroom412.ylgy.dao.AppDatabase
import com.bedroom412.ylgy.databinding.ActivityMainBinding
import com.core.download.HttpDownloadManagerImpl

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var downloadBinder: DownloadService.DownloadBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initStatusBar()

        binding.settingsBtn.setOnClickListener { }
        binding.scanBtn.setOnClickListener {
            startActivity(Intent(this, DownloadActivity::class.java))
        }
        binding.searchBtn.setOnClickListener {
            downloadBinder.syncSource(
                1, listOf(
                    "http://music.163.com/song/media/outer/url?id=441491828",
                    "http://music.163.com/song/media/outer/url?id=436346833",
                    "http://music.163.com/song/media/outer/url?id=1867217766"
                )
            )
        }
        // 打开播放器
        binding.vinylRecordView.setOnClickListener { }
        binding.songCover.setOnClickListener {

        }

        var intent = Intent(this, DownloadService::class.java)
        bindService(intent, object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                downloadBinder = service as DownloadService.DownloadBinder
                Log.d("MainActivity", "Service bind ${name} ${downloadBinder}")
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Log.d("MainActivity", "Service unbind ${name}")
            }

        }, Context.BIND_AUTO_CREATE)
    }

    private fun initStatusBar() {
        binding.songTitle.isSelected = true
        window.statusBarColor = Color.TRANSPARENT
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }
}