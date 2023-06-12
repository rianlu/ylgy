package com.bedroom412.ylgy

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.bedroom412.ylgy.model.DownloadRecord
import com.bedroom412.ylgy.model.ImportSourceSyncRecord
import com.bedroom412.ylgy.model.SyncRecordSegment
import com.core.download.HttpClientFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.Timer
import java.util.TimerTask


class DownloadService : Service() {
    var timer = Timer();

    override fun onCreate() {
        super.onCreate()
        YglyApplication.instance.httpDownloadManagerImpl.start()
        var timer = Timer()

        // 定义您要定期运行的任务

        // 定义您要定期运行的任务
        var task = object : TimerTask() {
            override fun run() {
                var ylgyHttpDownloadFactory =
                    YglyApplication.instance.httpDownloadManagerImpl.downloadFactory as YlgyHttpDownloadFactory
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
        YglyApplication.instance.httpDownloadManagerImpl.stop()
        var ylgyHttpDownloadFactory =
            YglyApplication.instance.httpDownloadManagerImpl.downloadFactory as YlgyHttpDownloadFactory
        ylgyHttpDownloadFactory.updateAll()
    }

    class DownloadBinder(
        private var downloadDownloadService: DownloadService

    ) : Binder() {

        fun downLoad(url: String) {

            val httpInfo = HttpClientFactory.getMeta(url)
            val generateUniqueFileName =
                YglyApplication.instance.httpDownloadManagerImpl.generateUniqueFileName(
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
                downloadPath = YglyApplication.instance.httpDownloadManagerImpl.downloadConfig.downloadPath,
                retryTimes = 0,
                status = 0,
                url = httpInfo.url,
            )

            (YglyApplication.instance.httpDownloadManagerImpl.downloadFactory as YlgyHttpDownloadFactory).addTask(
                downloadRecord
            );
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun syncSource(sourceId: Int, urls: List<String>) {

            GlobalScope.async(Dispatchers.IO) {

                var importSource =
                    YglyApplication.instance.db.importSourceDao().getById(sourceId)

                val recordId = YglyApplication.instance.db.importSourceSyncRecordDao().insert(
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
                    var segmentIds = YglyApplication.instance.db.syncRecordSegmentDao()
                        .insert(segment)

                    segment.id = segmentIds[0].toInt()


                    val generateUniqueFileName =
                        YglyApplication.instance.httpDownloadManagerImpl.generateUniqueFileName(
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
                        downloadPath = YglyApplication.instance.httpDownloadManagerImpl.downloadConfig.downloadPath,
                        retryTimes = 0,
                        status = 0,
                        url = httpInfo.url,
                    )

                    val dlIds = YglyApplication.instance.db.downloadRecordDao()
                        .insert(downloadRecord)

                    segment.downloadId = dlIds[0].toInt()

                    YglyApplication.instance.db.syncRecordSegmentDao()
                        .update(segment)

                    (YglyApplication.instance.httpDownloadManagerImpl.downloadFactory as YlgyHttpDownloadFactory).addTask(
                        downloadRecord
                    );

                }
            }
        }
    }

}