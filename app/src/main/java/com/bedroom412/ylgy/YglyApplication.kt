package com.bedroom412.ylgy

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.bedroom412.ylgy.dao.AppDatabase
import com.bedroom412.ylgy.model.DownloadRecord
import com.core.download.DownloadConfig
import com.core.download.HttpDownloadFactory
import com.core.download.HttpDownloadManagerImpl
import com.core.download.HttpDownloadTask

class YglyApplication : Application() {

    private var TAG = "YglyApplication"

    lateinit var httpDownloadManagerImpl: HttpDownloadManagerImpl

    lateinit var db: AppDatabase


    override fun onCreate() {
        super.onCreate()
        instance = this

        // 初始化数据库
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "test.db").build()
        // 初始化下载
        httpDownloadManagerImpl =
            HttpDownloadManagerImpl(
                DownloadConfig(), downloadFactory = YlgyHttpDownloadFactory(
                    httpDownloadManagerImpl
                )
            )
        httpDownloadManagerImpl.init()
//        httpDownloadManagerImpl.start()
        Log.d(TAG, "Application Stared.")
    }

    override fun onTerminate() {
//        httpDownloadManagerImpl.stop()
        super.onTerminate()
    }

    companion object {

        @JvmStatic
        lateinit var instance: YglyApplication

//        fun getInstance(): YglyApplication {
//            return instance
//        }

    }

}