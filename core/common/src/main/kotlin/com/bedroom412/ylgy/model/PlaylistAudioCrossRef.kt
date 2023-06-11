package com.bedroom412.ylgy.model

import androidx.room.ColumnInfo
import androidx.room.Entity


/**
 * 关联关系
 *
 * @property playlistId
 * @property audioId
 * @property type (0,自动加入，1 手动加入) 有时候同步的时候可能防止
 */
@Entity(tableName = "playlist_audio", primaryKeys = ["playlist_id", "audio_id"])
data class PlaylistAudioCrossRef(
    @ColumnInfo("playlist_id")
    var playlistId: Int,
    @ColumnInfo("audio_id")
    var audioId: Int,
    @ColumnInfo("type")
    var type: Int,
) {

}
