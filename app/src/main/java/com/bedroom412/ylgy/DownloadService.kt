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

class DownloadService : Service() {


    override fun onBind(intent: Intent): IBinder {
        return DownloadBinder(this)
    }


    class DownloadBinder(
        private var downloadDownloadService: DownloadService

    ) : Binder() {

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

                    YglyApplication.instance.httpDownloadManagerImpl.newTask(
                        taskId = segment.downloadId.toString(),
                        url = downloadRecord.url,
                        filePath = downloadRecord.downloadPath,
                        fileName = downloadRecord.fileName,
                        startImmediately = false
                    )
                }
            }
        }
    }

}