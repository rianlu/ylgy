package com.bedroom412.ylgy.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bedroom412.ylgy.R
import com.bedroom412.ylgy.databinding.ActivityMainBinding
import com.bedroom412.ylgy.databinding.ActivityPlayBinding

class PlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lyricTv.setOnClickListener {
            it.scrollBy(200,200)
        }
    }
}