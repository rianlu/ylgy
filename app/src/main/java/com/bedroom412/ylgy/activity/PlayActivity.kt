package com.bedroom412.ylgy.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.Player.REPEAT_MODE_ONE
import com.bedroom412.player.SingletonPlayer
import com.bedroom412.ylgy.R
import com.bedroom412.ylgy.databinding.ActivityPlayBinding
import com.bedroom412.ylgy.util.LyricsParser

class PlayActivity : AppCompatActivity(), Listener {

    private lateinit var binding: ActivityPlayBinding

    private val mHandler: Handler = Handler(Looper.myLooper()!!)

    private var updateProgressAction: Runnable = Runnable { updateProgress() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SingletonPlayer.getInstance().removeListener(this);
    }

    override fun onResume() {
        super.onResume()
        SingletonPlayer.getInstance().addListener(this)
        updateAll()
    }


    override fun onEvents(player: Player, events: Player.Events) {
        if (events.containsAny(
                Player.EVENT_PLAYBACK_STATE_CHANGED,
                Player.EVENT_PLAY_WHEN_READY_CHANGED,
                Player.EVENT_AVAILABLE_COMMANDS_CHANGED
            )
        ) {
            updatePlayPauseButton()
            Log.d("ylgy", "updatePlayPauseButton")
        }
        if (events.containsAny(
                Player.EVENT_PLAYBACK_STATE_CHANGED,
                Player.EVENT_PLAY_WHEN_READY_CHANGED,
                Player.EVENT_IS_PLAYING_CHANGED,
                Player.EVENT_AVAILABLE_COMMANDS_CHANGED
            )
        ) {
            updateProgress()
            Log.d("ylgy", "updateProgress")
        }
        if (events.containsAny(
                Player.EVENT_REPEAT_MODE_CHANGED,
                Player.EVENT_AVAILABLE_COMMANDS_CHANGED
            )
        ) {
            updateRepeatModeButton()
            Log.d("ylgy", "updateRepeatModeButton")
        }
        if (events.containsAny(
                Player.EVENT_SHUFFLE_MODE_ENABLED_CHANGED, Player.EVENT_AVAILABLE_COMMANDS_CHANGED
            )
        ) {
//            updateShuffleButton()
            Log.d("ylgy", "updateShuffleButton")

        }
        if (events.containsAny(
                Player.EVENT_REPEAT_MODE_CHANGED,
                Player.EVENT_SHUFFLE_MODE_ENABLED_CHANGED,
                Player.EVENT_POSITION_DISCONTINUITY,
                Player.EVENT_TIMELINE_CHANGED,
                Player.EVENT_SEEK_BACK_INCREMENT_CHANGED,
                Player.EVENT_SEEK_FORWARD_INCREMENT_CHANGED,
                Player.EVENT_AVAILABLE_COMMANDS_CHANGED
            )
        ) {
//            updateNavigation()
            Log.d("ylgy", "updateNavigation")

        }
        if (events.containsAny(
                Player.EVENT_POSITION_DISCONTINUITY,
                Player.EVENT_TIMELINE_CHANGED,
                Player.EVENT_AVAILABLE_COMMANDS_CHANGED
            )
        ) {
//            updateTimeline()
            Log.d("ylgy", "updateTimeline")

        }
        if (events.containsAny(
                Player.EVENT_PLAYBACK_PARAMETERS_CHANGED,
                Player.EVENT_AVAILABLE_COMMANDS_CHANGED
            )
        ) {
//            updatePlaybackSpeedList()
            Log.d("ylgy", "updatePlaybackSpeedList")

        }
        if (events.containsAny(
                Player.EVENT_TRACKS_CHANGED,
                Player.EVENT_AVAILABLE_COMMANDS_CHANGED
            )
        ) {
//            updateTrackLists()
            Log.d("ylgy", "updateTrackLists")

        }
    }

    private fun updateAll() {
        updatePlayPauseButton()
        updateNextButton()
        updateRepeatModeButton()
        updateLyrics()
        updateProgress()
    }

    private fun updateRepeatModeButton() {
        val player = SingletonPlayer.getInstance()
        if (player.shuffleModeEnabled) {
            binding.playModeBtn.setImageResource(R.drawable.ic_play_ramdom_play_mode)
        } else if (player.repeatMode == REPEAT_MODE_ONE) {
            binding.playModeBtn.setImageResource(R.drawable.ic_play_single_loop_mode)
        } else if (player.repeatMode == REPEAT_MODE_ALL) {
            binding.playModeBtn.setImageResource(R.drawable.ic_play_loop_play_mode)
        } else if (player.repeatMode == REPEAT_MODE_OFF) {
            Log.d("ylgy", "error play mode on player")
        }

    }

    private fun updateNextButton() {
        var player = SingletonPlayer.getInstance()
        if (player.nextMediaItemIndex == -1) {
            updateButton(false, binding.playNextBtn)
        } else {
            updateButton(true, binding.playNextBtn)
        }
        if (player.previousMediaItemIndex == -1) {
            updateButton(false, binding.playPrevBtn)
        } else {
            updateButton(true, binding.playPrevBtn)
        }
    }

    private fun updateButton(enabled: Boolean, view: View?) {
        if (view == null) {
            return
        }
        view.isEnabled = enabled
    }

    private fun updatePlayPauseButton() {
        val shouldShowPauseButton = shouldShowPauseButton()
        if (shouldShowPauseButton) {
            binding.playBtn.setImageResource(R.drawable.ic_play_pause)
        } else {
            binding.playBtn.setImageResource(R.drawable.ic_play_play)
        }
    }

    private fun shouldShowPauseButton(): Boolean {
        var player = SingletonPlayer.getInstance()
        return player.getPlaybackState() != Player.STATE_ENDED && player.getPlaybackState() != Player.STATE_IDLE && player.getPlayWhenReady()
    }


    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(updateProgressAction)
        SingletonPlayer.getInstance().removeListener(this);
    }

    fun updateLyrics() {
        var player = SingletonPlayer.getInstance()
        if (player.isCommandAvailable(Player.COMMAND_GET_CURRENT_MEDIA_ITEM)
        ) {
            player.currentMediaItem?.let {
                it.localConfiguration?.tag
            }?.let {
                (it as String).let {
                    val (_, lyrics) = LyricsParser.parse(it)
                    binding.playLyricsLayout.setLyrics(lyrics)
                    updateProgress()
                }
            }
        }
    }

    private fun updateProgress() {
        var player = SingletonPlayer.getInstance()
        var position: Long = 0
        if (player.isCommandAvailable(Player.COMMAND_GET_CURRENT_MEDIA_ITEM)) {
            position = player.currentPosition
        }

        if (binding.playLyricsLayout.isVisible) {
            binding.playLyricsLayout.onProcessing(position);
        }
        mHandler.removeCallbacks(updateProgressAction)
        val playbackState = player.playbackState ?: Player.STATE_IDLE
        if (player.isPlaying) {
            mHandler.postDelayed(updateProgressAction, 200)
        } else if (playbackState != Player.STATE_ENDED && playbackState != Player.STATE_IDLE) {
            mHandler.postDelayed(updateProgressAction, 1000)
        }
    }
}