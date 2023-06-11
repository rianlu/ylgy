package com.bedroom412.ylgy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "download_record", indices = [Index("url"), Index("file_name")])
data class DownloadRecord(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("all_size")
    var allSize: String?,
    @ColumnInfo("offset")
    var offset: Long?,
    @ColumnInfo("percent")
    var percent: Float?,
    @ColumnInfo("avg_speed")
    var avgSpeed: Float?,
    @ColumnInfo("http_agent")
    var httpAgent: String?,
    @ColumnInfo("http_header")
    var httpHeader: String?,
    @ColumnInfo("file_name")
    var fileName: String?,
    @ColumnInfo("download_path")
    var downloadPath: String?,
    @ColumnInfo("retry_times")
    var retryTimes: Int?,
    @ColumnInfo("status")
    var status: Int?,
    @ColumnInfo("url")
    var url: String?,

    ) {
}