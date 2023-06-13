package com.bedroom412.ylgy

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
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


    private lateinit var downloadBinder: DownloadService.DownloadBinder

    lateinit var db: AppDatabase
    private var connection: ServiceConnection? = null

    override fun onCreate() {
        super.onCreate()
        instance = this


        // 初始化数据库
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "test.db").build()

        connection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                downloadBinder = service as DownloadService.DownloadBinder
                Log.d(TAG, "Service bind ${name} ${downloadBinder}")
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                Log.d(TAG, "Service unbind ${name}")
            }
        }
        var intent = Intent(this, DownloadService::class.java)
        bindService(intent, connection!!, Context.BIND_AUTO_CREATE)
//        httpDownloadManagerImpl.start()
        Log.d(TAG, "Application Stared.")
    }

    override fun onTerminate() {
//        httpDownloadManagerImpl.stop()
        connection?.let {
            unbindService(it)
        }
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