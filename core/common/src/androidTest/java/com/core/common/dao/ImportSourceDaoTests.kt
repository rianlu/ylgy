package com.core.common.dao

import android.content.Context
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bedroom412.ylgy.dao.AppDatabase
import com.bedroom412.ylgy.dao.ImportSourceDao
import com.bedroom412.ylgy.model.ImportSource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ImportSourceDaoTests {


    private var mDb: AppDatabase? = null
    private var dao: ImportSourceDao? = null

    @Before
    fun createDb() {
        val context: Context =  InstrumentationRegistry.getInstrumentation().targetContext
        mDb = inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = mDb!!.importSourceDao();
    }

    @After
    fun closeDb() {
        mDb!!.close()
    }

    @Test
//    @Throws(Exception::class)
    fun testUpdate() {

        var importSource = ImportSource(
            type = 1,
            url = "http://",
            id = null!!
        )
        dao!!.update(importSource)
    }

}