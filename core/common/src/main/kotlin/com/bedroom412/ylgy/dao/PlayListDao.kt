package com.bedroom412.ylgy.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bedroom412.ylgy.model.ImportSource
import com.bedroom412.ylgy.model.PlayList

@Dao
interface PlayListDao {

    @Query("SELECT * FROM playlist WHERE id = :id")
    fun getById(id: Int): PlayList

}