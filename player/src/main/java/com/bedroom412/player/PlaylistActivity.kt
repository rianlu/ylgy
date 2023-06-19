package com.bedroom412.player

import android.content.ComponentName
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.bedroom412.player.databinding.ActivityPlaylistBinding
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaylistBinding
    lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val musicDataList = MetadataReaderUtils.getMusicDataList(this)
        val dataAdapter = MusicDataAdapter(musicDataList)
        binding.recyclerView.adapter = dataAdapter
        dataAdapter.setOnItemClickListener(object : MusicDataAdapter.OnItemClickListener {
            override fun onItemCLick(musicData: MusicData, position: Int) {
                musicDataList.forEach {
                    addMediaItem(it.uri)
                }
                player.seekTo(position, 0)
                player.prepare()
                player.play()
            }
        })

        CoroutineScope(Dispatchers.Main).launch {
            // 等一会不然还没初始化好
            delay(2000)
            player = SingletonPlayer.getInstance()
            musicDataList.forEach {
                addMediaItem(it.uri)
            }
        }
    }

    fun addMediaItem(uri: Uri) {
        val newItem = MediaItem.Builder()
            .setMediaId("$uri")
            .build()
        player.addMediaItem(newItem)
    }

    fun loadMediaItem(uri: Uri) {
        val newItem = MediaItem.Builder()
            .setMediaId("$uri")
            .build()
        player.setMediaItem(newItem)
    }
}