package com.bedroom412.ylgy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * 音频表
 *
 * @property id
 * @property type
 * @property name
 * @property bitRate
 * @property duration
 * @property sourceId
 * @property sourcePartId
 * @property url
 * @property filePath
 * @property fileSize
 * @property coverUrl(可选 null的话从mp3里面拿)
 * @property coverFilePath (缓存的路径)
 * @property status (0不存在，1下载中，2正常)
 * @property lyricTimeOffset 时间偏移校正
 */
@Entity(tableName = "audio", indices = [Index("url"), Index("name")])
data class Audio(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo("type")
    var type: Int?,
    @ColumnInfo("name")
    var name: String?,
    @ColumnInfo("bit_rate")
    var bitRate: Long?,
    @ColumnInfo("duration")
    var duration: Long?,
    @ColumnInfo("source_id")
    var sourceId: Int?,
    @ColumnInfo("source_part_id")
    var sourcePartId: Int?,
    @ColumnInfo("url")
    var url: String?,
    @ColumnInfo("file_path")
    var filePath: String?,
    @ColumnInfo("file_size")
    var fileSize: Long?,
    @ColumnInfo("cover_url")
    var coverUrl: String?,
    @ColumnInfo("cover_file_path")
    var coverFilePath: String?,
    @ColumnInfo("status")
    var status: String?,

    @ColumnInfo("lyric_id")
    var lyricId: Int?,
    @ColumnInfo("lyric_time_offset")
    var lyricTimeOffset: Int?
) {
}