package com.bedroom412.ylgy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * 播放单
 *
 * @property id
 * @property name
 * @property type (类型0内置的，例如我喜爱的，默认歌单,1普通的)
 * @property coverPath
 * @property createTs
 * @property updateTs
 * @property isDel
 */
@Entity(tableName = "playlist")
data class PlayList(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo("name")
    var name: String?,
    @ColumnInfo("type")
    var type: Int?,
    @ColumnInfo("cover_path")
    var coverPath: String?,
    @ColumnInfo("create_ts")
    var createTs: Long?,
    @ColumnInfo("update_ts")
    var updateTs: Long?,
    @ColumnInfo("is_del")
    var isDel: String?,


    ) {
}