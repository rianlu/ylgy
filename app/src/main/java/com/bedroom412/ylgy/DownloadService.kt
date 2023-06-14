package com.bedroom412.ylgy

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.bedroom412.ylgy.dao.AppDatabase
import com.bedroom412.ylgy.model.DownloadRecord
import com.bedroom412.ylgy.model.ImportSourceSyncRecord
import com.bedroom412.ylgy.model.SyncRecordSegment
import com.core.download.DownloadConfig
import com.core.download.HttpClientFactory
import com.core.download.HttpDownloadManagerImpl
import com.core.download.HttpDownloadTask
import com.core.download.HttpDownloadTaskListener
import com.core.download.STATUS_DOWNLOADING
import com.core.download.STATUS_ERROR
import com.core.download.STATUS_PAUSED
import com.core.download.STATUS_SUCCESS
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask


class DownloadService : Service() {
    var timer = Timer();


//    lateinit var httpDownloadManagerImpl: HttpDownloadManagerImpl

    var TAG = "DownloadService"

    var ls: HttpDownloadTaskListener = object : HttpDownloadTaskListener {
        override fun onStarted(task: HttpDownloadTask) {
            Log.d(TAG, "TASK ${task} is Started.. ")
            task.attachment?.let {
                val downloadRecord = task.attachment as DownloadRecord
                downloadRecord.status = task.status
                with(downloadRecord) {
                    status = STATUS_DOWNLOADING
                    // continue ...
                    if (startTs == null) {
                        startTs = System.currentTimeMillis()
                    }
                }
                db.downloadRecordDao().update(downloadRecord)
            }
        }

        override fun onCompleted(task: HttpDownloadTask) {
            Log.d(TAG, "TASK ${task} is Completed.. ")


            val downloadRecord = task.attachment as DownloadRecord
            with(downloadRecord) {
                status = STATUS_SUCCESS
                endTs = System.currentTimeMillis()
                db.downloadRecordDao().update(this)
            }

        }

        override fun onError(task: HttpDownloadTask, ex: Throwable) {
            Log.d(TAG, "TASK ${task} is Error.. ")
            val downloadRecord = task.attachment as DownloadRecord
            downloadRecord.status = task.status
            with(downloadRecord) {
                status = STATUS_ERROR
                endTs = System.currentTimeMillis()
            }
            db.downloadRecordDao().update(downloadRecord)
        }

        override fun onPaused(task: HttpDownloadTask, totalReadBytes: Long, contentLength: Long) {
            Log.d(TAG, "TASK ${task} is Error.. ")
            val downloadRecord = task.attachment as DownloadRecord
            downloadRecord.status = task.status
            with(downloadRecord) {
                status = STATUS_PAUSED
                offset = totalReadBytes
                allSize = contentLength
                avgSpeed = totalReadBytes * 1f / (System.currentTimeMillis() - startTs!!)
            }
            db.downloadRecordDao().update(downloadRecord)
        }

        override fun onProgressing(
            task: HttpDownloadTask,
            readBytes: Int,
            totalReadBytes: Long,
            contentLength: Long,
            cost: Long
        ) {
            Log.d(TAG, "TASK ${task} is onProgressing.. ")
            val downloadRecord = task.attachment as DownloadRecord
            with(downloadRecord) {
                status = task.status
                offset = totalReadBytes
                allSize = contentLength
                avgSpeed = totalReadBytes * 1f / (System.currentTimeMillis() - startTs!!)
            }
            db.downloadRecordDao().update(downloadRecord)
        }
    }

    companion object {
        lateinit var httpDownloadManagerImpl: HttpDownloadManagerImpl
        lateinit var bulgyHttpDownloadRepository: BulgyHttpDownloadRepository
        lateinit var db: AppDatabase
    }


    override fun onCreate() {
        super.onCreate()

        Log.d(TAG,"Service is started")
        // 初始化数据库
        db = YglyApplication.instance.db
        // 初始化下载
        httpDownloadManagerImpl =
            HttpDownloadManagerImpl(
                DownloadConfig()
            )


        bulgyHttpDownloadRepository = BulgyHttpDownloadRepository(
            httpDownloadManagerImpl
        )

        httpDownloadManagerImpl.registerListener(ls)


        httpDownloadManagerImpl.start()
        val timer = Timer()


        val task = object : TimerTask() {
            override fun run() {
                bulgyHttpDownloadRepository.updateAll()
            }
        }

        // 设置定时器以每隔5秒运行一次任务

        timer.scheduleAtFixedRate(task, 0, 3 * 60 * 1000)


    }

    override fun onBind(intent: Intent): IBinder {
        return DownloadBinder(this)
    }

    override fun onDestroy() {

        Log.d(TAG,"Service is destroyed.")
        super.onDestroy()
        timer.cancel()
        httpDownloadManagerImpl.stop()
        bulgyHttpDownloadRepository.updateAll()
    }

    class DownloadBinder(
        private var downloadDownloadService: DownloadService

    ) : Binder() {

        var TAG = "DownloadBinder"
        fun downLoad(url: String) {

            val httpInfo = HttpClientFactory.getMeta(url)
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
                startTs = null,
                endTs = null,
            )

            (bulgyHttpDownloadRepository).addAndStartTask(
                downloadRecord, isStart = true
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
                    launch(Dispatchers.IO) {

                        Log.d(TAG, "Begin to Parse Url Info ${it}")
                        val httpInfo = HttpClientFactory.getMeta(it)
                        Log.d(TAG, "Parse Result Of Url Info ${it} is ${httpInfo}")

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

                        val downloadRecord = DownloadRecord(
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
                            startTs = null,
                            endTs = null
                        )

                        val dlIds = db.downloadRecordDao()
                            .insert(downloadRecord)

                        segment.downloadId = dlIds[0].toInt()

                        db.syncRecordSegmentDao()
                            .update(segment)

                        bulgyHttpDownloadRepository.addAndStartTask(
                            downloadRecord, isStart = true
                        );
                    }

                }
            }
        }
    }

}