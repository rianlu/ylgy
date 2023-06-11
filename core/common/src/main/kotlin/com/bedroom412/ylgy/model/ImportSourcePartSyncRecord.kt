package com.bedroom412.ylgy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * 导入记录
 */
@Entity(tableName = "import_source_part_sync_record")
data class ImportSourcePartSyncRecord(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo("source_id")
    var sourceId: Int?,
    @ColumnInfo("source_part_id")
    var sourcePartId: Int?,
    @ColumnInfo("status")
    var status: Int?,
    @ColumnInfo("percent")
    var percent: Float?,
    @ColumnInfo("sync_start_ts")
    var syncStartTs: Long?,
    @ColumnInfo("sync_end_ts")
    var syncEndTs: Long?,
    @ColumnInfo("sync_cnt")
    var syncCnt: Int?,
    @ColumnInfo("finished_cnt")
    var finishedCnt: Int?,
    @ColumnInfo("handled_count")
    var handledCount: Int?,


    ) {
}