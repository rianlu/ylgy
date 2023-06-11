package com.bedroom412.ylgy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * 歌词
 */
@Entity(tableName = "lyric", indices = [Index("url"), Index("name")])
data class Lyric(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("name")
    var name: String?,
    @ColumnInfo("url")
    var url: String?,
    @ColumnInfo("file_path")
    var filePath: String?,
) {
}