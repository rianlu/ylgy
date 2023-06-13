package com.bedroom412.ylgy.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bedroom412.ylgy.DownloadViewModel
import com.bedroom412.ylgy.SourceViewModel
import com.bedroom412.ylgy.databinding.ActivityDownloadBinding
import com.bedroom412.ylgy.databinding.ActivitySourceBinding
import com.bedroom412.ylgy.recycleview.DownloadRecycleViewAdapter
import com.bedroom412.ylgy.recycleview.SourceRecycleViewAdapter

class SourceActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySourceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySourceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sourceViewModel =
            ViewModelProvider(this).get(SourceViewModel::class.java)
        val adapter = SourceRecycleViewAdapter(sourceViewModel)
        binding.sourceRecyclerView.adapter = adapter

    }
}