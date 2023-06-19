package com.bedroom412.player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

/**
 * @Author lu
 * @Date 2023/6/17 17:30
 * @ClassName: MusicPlayerService
 * @Description:
 */
class MusicPlayerService: MediaLibraryService() {

    lateinit var player: Player
    lateinit var session: MediaLibrarySession
    private val PLAYBACK_CHANNEL_ID = "playback_channel"
    private val PLAYBACK_NOTIFICATION_ID = 1
    private lateinit var playerNotificationManager: PlayerNotificationManager

    override fun  onCreate() {
        super.onCreate()

        player = ExoPlayer.Builder(applicationContext)
            .setRenderersFactory(
                DefaultRenderersFactory(this).setExtensionRendererMode(
                    DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER /* We prefer extensions, such as FFmpeg */
                )
            ).build()

        session = MediaLibrarySession.Builder(this, player,
            object: MediaLibrarySession.Callback {
                override fun onAddMediaItems(
                    mediaSession: MediaSession,
                    controller: MediaSession.ControllerInfo,
                    mediaItems: MutableList<MediaItem>
                ): ListenableFuture<MutableList<MediaItem>> {
                    val updatedMediaItems = mediaItems.map { it.buildUpon().setUri(it.mediaId).build() }.toMutableList()
                    return Futures.immediateFuture(updatedMediaItems)
                }
            }).build()

        initNotification()
    }

    private fun initNotification() {
        playerNotificationManager = PlayerNotificationManager.Builder(applicationContext,
            PLAYBACK_NOTIFICATION_ID, PLAYBACK_CHANNEL_ID)
            .build()
        playerNotificationManager.setPlayer(player)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return session
    }

    override fun onDestroy() {
        session.release()
        playerNotificationManager.setPlayer(null)
        player.release()
        super.onDestroy()
    }
}