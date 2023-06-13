package com.bedroom412.ylgy

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.room.Room
import com.bedroom412.ylgy.dao.AppDatabase
import com.bedroom412.ylgy.model.DownloadRecord
import com.bedroom412.ylgy.model.ImportSourceSyncRecord
import com.bedroom412.ylgy.model.SyncRecordSegment
import com.core.download.DownloadConfig
import com.core.download.HttpClientFactory
import com.core.download.HttpDownloadManagerImpl
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.Timer
import java.util.TimerTask


class DownloadService : Service() {
    var timer = Timer();


//    lateinit var httpDownloadManagerImpl: HttpDownloadManagerImpl



    companion object{
        lateinit var httpDownloadManagerImpl: HttpDownloadManagerImpl
        lateinit var db: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        // 初始化数据库
        db = YglyApplication.instance.db
        // 初始化下载
        httpDownloadManagerImpl =
            HttpDownloadManagerImpl(
                DownloadConfig(),
                downloadFactory = null
            )

        val downloadFactory  = YlgyHttpDownloadFactory(
            httpDownloadManagerImpl
        )
        httpDownloadManagerImpl.downloadFactory = downloadFactory

        httpDownloadManagerImpl.init()

        httpDownloadManagerImpl.start()
        var timer = Timer()


        var task = object : TimerTask() {
            override fun run() {
                var ylgyHttpDownloadFactory =
                    httpDownloadManagerImpl.downloadFactory as YlgyHttpDownloadFactory
                ylgyHttpDownloadFactory.updateAll()
            }
        }

        // 设置定时器以每隔5秒运行一次任务

        timer.scheduleAtFixedRate(task, 0, 3 * 60 * 1000)


    }

    override fun onBind(intent: Intent): IBinder {
        return DownloadBinder(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        httpDownloadManagerImpl.stop()
        var ylgyHttpDownloadFactory =
            httpDownloadManagerImpl.downloadFactory as YlgyHttpDownloadFactory
        ylgyHttpDownloadFactory.updateAll()
    }

    class DownloadBinder(
        private var downloadDownloadService: DownloadService

    ) : Binder() {

        fun downLoad(url: String) {

            val httpInfo = HttpClientFactory.getMeta(url)
            val generateUniqueFileName =
                DownloadService.httpDownloadManagerImpl.generateUniqueFileName(
                    httpInfo.fileName
                )
            var downloadRecord = DownloadRecord(
                id = null,
                allSize = httpInfo.size,
                offset = 0,
                percent = null,
                avgSpeed = null,
                httpAgent = null,
                httpHeader = null,
                fileName = generateUniqueFileName,
                downloadPath = httpDownloadManagerImpl.downloadConfig.downloadPath,
                retryTimes = 0,
                status = 0,
                url = httpInfo.url,
            )

            (httpDownloadManagerImpl.downloadFactory as YlgyHttpDownloadFactory).addTask(
                downloadRecord
            );
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun syncSource(sourceId: Int, urls: List<String>) {

            GlobalScope.async(Dispatchers.IO) {

                var importSource =
                    db.importSourceDao().getById(sourceId)

                val recordId = db.importSourceSyncRecordDao().insert(
                    ImportSourceSyncRecord(
                        id = null,
                        sourceId = sourceId,
                        status = 0,
                        percent = null,
                        syncStartTs = null,
                        syncEndTs = null,
                        syncCnt = urls.size,
                        finishedCnt = 0,
                        handledCount = 0
                    )
                )

                urls.forEach {

                    val httpInfo = HttpClientFactory.getMeta(it)

                    val segment = SyncRecordSegment(
                        id = null,
                        recordId = recordId.toInt(),
                        sourceId = null,
                        url = null,
                        syncStartTs = null,
                        syncEndTs = null,
                        downloadId = null,
                        audioId = null,
                        status = null,
                    )
                    var segmentIds = db.syncRecordSegmentDao()
                        .insert(segment)

                    segment.id = segmentIds[0].toInt()


                    val generateUniqueFileName =
                        httpDownloadManagerImpl.generateUniqueFileName(
                            httpInfo.fileName
                        )

                    var downloadRecord = DownloadRecord(
                        id = null,
                        allSize = httpInfo.size,
                        offset = 0,
                        percent = null,
                        avgSpeed = null,
                        httpAgent = null,
                        httpHeader = null,
                        fileName = generateUniqueFileName,
                        downloadPath = httpDownloadManagerImpl.downloadConfig.downloadPath,
                        retryTimes = 0,
                        status = 0,
                        url = httpInfo.url,
                    )

                    val dlIds = db.downloadRecordDao()
                        .insert(downloadRecord)

                    segment.downloadId = dlIds[0].toInt()

                    db.syncRecordSegmentDao()
                        .update(segment)

                    (httpDownloadManagerImpl.downloadFactory as YlgyHttpDownloadFactory).addTask(
                        downloadRecord
                    );

                }
            }
        }
    }

}