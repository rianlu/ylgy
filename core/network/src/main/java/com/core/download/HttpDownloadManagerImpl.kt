package com.core.download

import okhttp3.OkHttpClient
import java.util.concurrent.atomic.AtomicLong

class HttpDownloadManagerImpl(
    var downloadConfig: DownloadConfig,
    var listeners: List<HttpDownloadTaskListener> = mutableListOf()
) : HttpDownloadManager {

    private var totalReceiveBytes = AtomicLong(0)
    private var totalTimeUsed = AtomicLong(0)
    private val downloadQueue = mutableListOf<HttpDownloadTask>().asReversed()


    init {
        listeners += StatHttpDownloadTaskListener(this)
    }


    fun newClient(): OkHttpClient {
        return OkHttpClient.Builder().build();
    }

    override fun tasks(): List<HttpDownloadTask> {
        return listOf(*downloadQueue.toTypedArray())
    }

    override fun newTask(
        taskId: String?,
        url: String,
        filePath: String?,
        offset: Long,
        startImmediately: Boolean
    ): HttpDownloadTask {

        var fp = filePath
        if (fp == null) {
            fp = downloadConfig.downloadPath
        }


        val httpDownloadTask = hookTask(
            HttpDownloadTask(
                taskId = taskId,
                url = url,
                filePath = fp,
                offset = offset,
                httpDownloadManagerRef = this
            )
        )

        downloadQueue.add(0, httpDownloadTask)
        if (startImmediately) {
            httpDownloadTask.startAsync()
        }
        return httpDownloadTask
    }

    override fun startTask(task: HttpDownloadTask): HttpDownloadTask {
        val t = hookTask(task);
        t.startAsync()
        return t
    }


    override fun pauseTask(task: HttpDownloadTask): HttpDownloadTask {
        val t = hookTask(task);
        t.pause()
        return t
    }

    private fun hookTask(task: HttpDownloadTask): HttpDownloadTask {
        for (listener in listeners) {
            task.addListener(listener)
        }
        return task
    }


    private class StatHttpDownloadTaskListener(
        val httpDownloadManagerImpl: HttpDownloadManagerImpl

    ) : HttpDownloadTaskListener {

        override fun onProgressing(
            task: HttpDownloadTask,
            readBytes: Int,
            totalReadBytes: Long,
            contentLength: Long,
            cost: Long
        ) {
            httpDownloadManagerImpl.totalReceiveBytes.addAndGet(readBytes.toLong())
            httpDownloadManagerImpl.totalTimeUsed.addAndGet(cost)

        }
    }
}