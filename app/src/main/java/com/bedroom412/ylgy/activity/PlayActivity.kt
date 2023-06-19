package com.bedroom412.ylgy.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.bedroom412.ylgy.databinding.ActivityPlayBinding
import com.bedroom412.ylgy.util.Lyric
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lyricTv.setOnClickListener {
            it.scrollBy(200,200)
        }

        var listOf = listOf<Lyric>(
            Lyric(0, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefg",99000),
            Lyric(99000, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefg",99000 *2),
            Lyric(99000 *2 , "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefg",99000 *3),
//            Lyric(3000, "你好啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊",7000),
//            Lyric(7000, "这个不是少时诵诗书所所所所所所所所所所所所所所",20000),
//            Lyric(0, "这个不是少时诵诗书所所所所所所所所所所所所所所",0),
//            Lyric(0, "这个不是少时诵诗书所所所所所所所所所所所所所所",0),
//            Lyric(0, "这个不是少时诵诗书所所所所所所所所所所所所所所",0),
        )
        binding.playLyricsLayout.setLyrics(listOf)

        val player = ExoPlayer.Builder(this)
            .build()
        val mediaItem = MediaItem.fromUri("https://pan.zzxw02.xyz/0:/%E9%82%93%E7%B4%AB%E6%A3%8B/G.E.M. 邓紫棋 - 光年之外.mp3")
        player.setMediaItem(mediaItem)
        player.prepare()
//        player.play()

//        player.addListener(object : Listener {
//            override fun onSeekProcessed() {
//                super.onSeekProcessed()
//            }
//
//            override fun onPositionDiscontinuity(
//                oldPosition: Player.PositionInfo,
//                newPosition: Player.PositionInfo,
//                reason: Int
//            ) {
//                super.onPositionDiscontinuity(oldPosition, newPosition, reason)
//            }
//        })

        var i = 0L;
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                i  = i+1000
                var currentPosition = player.currentPosition
                binding.playLyricsLayout.onProcessing(i)
                delay(200)
            }
        }
    }


//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        when (event?.action) {
//            MotionEvent.ACTION_DOWN -> {
//                Log.d("ylgy", "onInterceptTouchEvent PlayActivity ACTION_DOWN")
//            }
//
//            MotionEvent.ACTION_MOVE -> {
//                Log.d("ylgy", "onInterceptTouchEvent PlayActivity ACTION_MOVE")
//                var lyricsLayout = binding.playContentLyricsLayout.findViewById<LyricsLayout>(R.id.play_lyrics_layout)
//                lyricsLayout.dispatchTouchEvent(event)
//
//                return false
//            }
//        }
//
//
//        return super.onTouchEvent(event)
//    }
}