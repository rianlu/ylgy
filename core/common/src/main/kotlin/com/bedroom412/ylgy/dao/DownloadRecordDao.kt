package com.bedroom412.ylgy.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bedroom412.ylgy.model.DownloadRecord

@Dao
interface DownloadRecordDao {

    @Insert
    fun insert(vararg arr: DownloadRecord): Array<Long>

    @Query("select * from download_record  where status in (0,1,2,3) order by id asc limit 1 ")
    fun getDownloadRecord(): DownloadRecord?

    @Query("select * from download_record order by id asc limit 1 ")
    fun getAll(): List<DownloadRecord>?

    @Update
    fun update(downloadRecord: DownloadRecord): Int?

}