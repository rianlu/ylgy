package com.bedroom412.ylgy.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bedroom412.ylgy.DownloadViewModel
import com.bedroom412.ylgy.databinding.ActivityDownloadBinding
import com.bedroom412.ylgy.recycleview.DownloadRecycleViewAdapter

class DownloadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDownloadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val downloadViewModel =
            ViewModelProvider(this).get(DownloadViewModel::class.java)
        val adapter = DownloadRecycleViewAdapter(downloadViewModel)
        binding.downloadRecyclerView.adapter = adapter

    }
}