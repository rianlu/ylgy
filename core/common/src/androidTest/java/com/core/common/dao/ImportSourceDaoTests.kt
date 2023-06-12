package com.core.common.dao

import android.content.Context
import android.graphics.Path
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bedroom412.ylgy.dao.AppDatabase
import com.bedroom412.ylgy.dao.ImportSourceDao
import com.bedroom412.ylgy.model.ImportSource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


@RunWith(AndroidJUnit4::class)
class ImportSourceDaoTests {


    private lateinit var mDb: AppDatabase
    private var dao: ImportSourceDao? = null

    @Before
    fun createDb() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

//        mDb = inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        mDb = Room.databaseBuilder(context, AppDatabase::class.java, "test.db").build()
        println("createDb")
//        dao = mDb!!.importSourceDao();
        dao = mDb.importSourceDao()

        val dbpath: File? = context.getDatabasePath("test.db")
        println("dbpath.absolutePath = ${dbpath}")
    }

    @After
    fun closeDb() {
        println("closeDb")
        if (this::mDb.isInitialized) {
            mDb.close()
        }
    }

    @Test
//    @Throws(Exception::class)
    fun testUpdate() {
        run {
            println("testUpdate")
            val importSource = ImportSource(
                id = null,
                name = "",
                url = "",
                type = 1,
                isNeedSync = 0,
                lastSyncTs = null,
                playlistId = null,
                // todo 数量
                cnt = 0
            )
            if (this::mDb.isInitialized) {
                dao!!.insert(importSource)
                println("result: $importSource.id")
            } else {
                println("为实例化")
            }
        }
    }

}