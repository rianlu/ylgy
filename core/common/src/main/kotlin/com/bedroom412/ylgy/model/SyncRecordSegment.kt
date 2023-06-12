package com.bedroom412.ylgy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * 导入记录
 */
@Entity(tableName = "sync_record_segment")
data class SyncRecordSegment(

    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    @ColumnInfo("record_id")
    var recordId: Int?,
    @ColumnInfo("source_id")
    var sourceId: Int?,
    @ColumnInfo("url")
    var url: String?,
    @ColumnInfo("sync_start_ts")
    var syncStartTs: Long?,
    @ColumnInfo("sync_end_ts")
    var syncEndTs: Long?,
    @ColumnInfo("download_id")
    var downloadId: Int?,
    @ColumnInfo("audio_id")
    var audioId: Int?,
    @ColumnInfo("status")
    var status: Int?,
) {
}