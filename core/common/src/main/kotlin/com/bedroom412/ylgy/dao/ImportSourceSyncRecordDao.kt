package com.bedroom412.ylgy.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bedroom412.ylgy.model.ImportSource
import com.bedroom412.ylgy.model.ImportSourceSyncRecord

@Dao
interface ImportSourceSyncRecordDao {

    @Query("SELECT * FROM import_source_sync_record WHERE source_id = :sourceId")
    fun getBySourceId(sourceId: Int): ImportSourceSyncRecord

    @Insert
    fun insert(importSourceSyncRecord: ImportSourceSyncRecord):Long
//
//    fun getBySourceId(sourceId : Int?): ImportSourceSyncRecord
}