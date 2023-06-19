package com.bedroom412.player

import android.content.ComponentName
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.bedroom412.player.databinding.ActivityPlayerBinding
import com.google.common.util.concurrent.MoreExecutors

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val musicDataList = MetadataReaderUtils.getMusicDataList(this)
        val sessionToken =
            SessionToken(applicationContext, ComponentName(this, MusicPlayerService::class.java))
        val mediaControllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        mediaControllerFuture.addListener({
            player = mediaControllerFuture.get()
            musicDataList.forEach {
                addMediaItem(it.uri)
            }
            val dataAdapter = MusicDataAdapter(musicDataList)
            binding.recyclerView.adapter = dataAdapter
            dataAdapter.setOnItemClickListener(object : MusicDataAdapter.OnItemClickListener {
                override fun onItemCLick(musicData: MusicData, position: Int) {
                    player.seekTo(position, 0)
                    player.prepare()
                    player.play()
                }
            })
        }, MoreExecutors.directExecutor())
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