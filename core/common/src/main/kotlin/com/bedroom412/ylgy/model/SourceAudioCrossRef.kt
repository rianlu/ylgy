package com.bedroom412.ylgy.model

import androidx.room.ColumnInfo
import androidx.room.Entity


/**
 * 音频导入关联表， 用于再次同步的时候进行筛选和过滤
 *
 * @property sourcePartId
 * @property audioId
 */
@Entity(tableName = "source_audio", primaryKeys = ["source_id", "audio_id"])
data class SourceAudioCrossRef(
    @ColumnInfo("source_id")
    var sourcePartId: Int,
    @ColumnInfo("audio_id")
    var audioId: Int,
) {

}
