package com.bedroom412.ylgy.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bedroom412.ylgy.model.ImportSource
import com.bedroom412.ylgy.model.SyncRecordSegment

@Dao
interface SyncRecordSegmentDao {

    @Query("SELECT * FROM sync_record_segment WHERE download_id = :downloadId")
    fun getByDownloadId(downloadId: Int): SyncRecordSegment

    @Insert
    fun insert(vararg arr: SyncRecordSegment): Array<Long>

    @Update
    fun update(segment : SyncRecordSegment): Int
}