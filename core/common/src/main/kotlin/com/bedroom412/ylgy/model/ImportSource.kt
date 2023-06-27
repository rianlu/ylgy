package com.bedroom412.ylgy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * 导入源
 *
 * @property url 地址
 * @property type {@link com.bedroom412.ylgy.model.SourceType}
 */
@Entity(tableName = "import_source")
data class ImportSource(

    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    @ColumnInfo("url")
    var url: String,
    @ColumnInfo("type")
    var type: Int?,
    @ColumnInfo("name")
    var name: String?,
//
//    @ColumnInfo("cnt")
//    var cnt: Int?,

    @ColumnInfo("last_sync_ts")
    var lastSyncTs: Int?,

    @ColumnInfo("playlist_id")
    var playlistId: Int?,

    @ColumnInfo("is_need_sync")
    var isNeedSync: Int,

    @ColumnInfo("proxy_type")
    var proxyType: Int?,

    @ColumnInfo("proxy_on")
    var proxyOn: Int?,

    @ColumnInfo("proxy_address")
    var proxyAddress: String?,
) {
}