package com.bedroom412.ylgy.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import com.bedroom412.ylgy.model.ImportSource

@Dao
interface ImportSourceDao {

    @Update
    fun update(vararg arr: ImportSource): Int

    @Insert
    fun insert(vararg arr: ImportSource): Array<Long>


}