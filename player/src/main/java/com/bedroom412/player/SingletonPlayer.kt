package com.bedroom412.player

import android.app.Application
import android.content.ComponentName
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors

/**
 * @Author lu
 * @Date 2023/6/19 15:08
 * @ClassName: SingletonPlayer
 * @Description:
 */
class SingletonPlayer private constructor() {
    companion object {
        private lateinit var INSTAANCE: Player
        private lateinit var SESSION_TOKEN_INSTANCE: SessionToken

        fun init(application: Application) {
            SESSION_TOKEN_INSTANCE =
                SessionToken(application, ComponentName(application, MusicPlayerService::class.java))
            val mediaControllerFuture = MediaController.Builder(application, SESSION_TOKEN_INSTANCE).buildAsync()
            mediaControllerFuture.addListener({
                INSTAANCE = mediaControllerFuture.get()
            }, MoreExecutors.directExecutor())
        }

        fun getInstance(): Player {
            return if (this::INSTAANCE.isInitialized) {
                INSTAANCE
            } else {
                throw Exception("Player is not initialized！")
            }
        }

        fun getSession(): SessionToken? {
            return if (this::SESSION_TOKEN_INSTANCE.isInitialized) {
                SESSION_TOKEN_INSTANCE
            } else {
                null
            }

        }
    }
}