package com.core.download

import android.util.Log
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicLong

class HttpDownloadManagerImpl(
    var downloadConfig: DownloadConfig,
    var listeners: List<HttpDownloadTaskListener> = mutableListOf(),
    var managerListener: List<HttpDownloadManagerListener> = mutableListOf(),
) : HttpDownloadManager {

    private var TAG = "HttpDownloadManager"

    private var totalReceiveBytes = AtomicLong(0)

    private var totalTimeUsed = AtomicLong(0)

    // ready queue
    private val downloadQueue = LinkedBlockingQueue<HttpDownloadTask>()

    // downloading queue
    private val downloadingQueue = LinkedBlockingQueue<HttpDownloadTask>()

    private val maxConcurrentDownloads = Semaphore(downloadConfig.maxConcurrentDownloads)

    private val scope = CoroutineScope(Job() + Dispatchers.Default)

    private var isStart = false

    private lateinit var rateLimiter: RateLimiter

    init {
        rateLimiter = RateLimiter(downloadConfig.bytesPerSecond?:Double.MAX_VALUE)
    }


    private val statHttpDownloadTaskListener = StatHttpDownloadTaskListener(this)

    fun registerListener(listener: HttpDownloadTaskListener) {
        if (listener !in listeners) {
            listeners += listener
        }
    }

    fun unRegisterListener(listener: HttpDownloadTaskListener) {
        if (listener in listeners) {
            listeners -= listener
        }
    }

    fun registerManagerListener(listener: HttpDownloadManagerListener) {
        if (listener !in managerListener) {
            managerListener += listener
        }
    }

    fun unRegisterManagerListener(listener: HttpDownloadManagerListener) {
        if (listener in managerListener) {
            managerListener -= listener
        }
    }

    fun start() {

        if (isStart) {
            return
        }

        scope.launch {
            try {
                Log.d(TAG, "HttpDownloadManagerImpl started...")
                isStart = true
                while (isStart) {
                    withContext(Dispatchers.IO) {
                        maxConcurrentDownloads.acquire()
                        downloadQueue.take().let {
                            if (it.isReady(it.status)) {
                                synchronized(it) {
                                    if (it.isReady(it.status)) {
                                        downloadQueue.remove(it)
                                        downloadingQueue.offer(it)
                                        // execute task
                                        launch {
                                            Log.d(TAG, "task begin ${it}")
                                            it.start()
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            } catch (e: CancellationException) {
                isStart = false
                Log.d(TAG, "HttpDownloadManagerImpl canceled...")
                stop()
            }
        }
    }


    fun stop() {
        isStart = false;
        pauseAll();
        Log.d(TAG, "HttpDownloadManagerImpl stopped...")
    }

    private fun pauseAll() {
        downloadQueue.clear()
        downloadingQueue.forEach {
            it.pause()
        }
        downloadingQueue.clear()
    }

    fun newClient(): OkHttpClient {
        return HttpClientFactory.newClient()
    }
//
//    override fun tasks(): List<HttpDownloadTask> {
//        return listOf(*downloadQueue.toTypedArray())
//    }

    override fun newTask(
        taskId: String?,
        url: String,
        filePath: String,
        fileName: String,
        offset: Long,
        startImmediately: Boolean,
        attachment: Any?,
    ): HttpDownloadTask {

        val httpDownloadTask = hookTask(
            HttpDownloadTask(
                taskId = taskId,
                url = url,
                filePath = filePath,
                fileName = fileName,
                offset = offset,
                httpDownloadManagerRef = this,
                attachment = attachment,
            )
        )

        if (startImmediately) {
            httpDownloadTask.start()
        } else {
            downloadQueue.offer(httpDownloadTask)
        }
        return httpDownloadTask
    }

    override fun newTask(task: HttpDownloadTask) {
        val t = hookTask(task);
        downloadQueue.offer(t)
    }


    override fun pauseTask(task: HttpDownloadTask) {
        val t = hookTask(task);
        t.pause()
    }

    private fun hookTask(task: HttpDownloadTask): HttpDownloadTask {
        for (listener in listeners) {
            task.addListener(listener)
        }
        task.addListener(statHttpDownloadTaskListener)
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

        override fun onCompleted(task: HttpDownloadTask) {
            removeTaskInDownloading(task)
            notifyCompleteTaskEventIfNecessary()
        }


        override fun onError(task: HttpDownloadTask, ex: Throwable) {
            removeTaskInDownloading(task)
            notifyCompleteTaskEventIfNecessary()
        }

        override fun onPaused(task: HttpDownloadTask, totalReadBytes: Long, contentLength: Long) {
            removeTaskInDownloading(task)
            notifyCompleteTaskEventIfNecessary()
        }

        fun removeTaskInDownloading(task: HttpDownloadTask) {
            httpDownloadManagerImpl.downloadingQueue.filter { it.taskId.equals(task.taskId) }.also {
                httpDownloadManagerImpl.downloadingQueue.removeAll(
                    it
                )
                httpDownloadManagerImpl.maxConcurrentDownloads.release(it.size)
            }
        }

        private fun notifyCompleteTaskEventIfNecessary() {
            if (httpDownloadManagerImpl.downloadingQueue.isEmpty() && httpDownloadManagerImpl.downloadQueue.isEmpty()) {
                for (listener in httpDownloadManagerImpl.managerListener) {
                    listener.onCompleteAll()
                }
            }
        }

    }


    fun generateUniqueFileName(fileName: String): String {
        val filePath = downloadConfig.downloadPath
        val file = File(filePath, fileName)
        if (!file.exists()) {
            return fileName
        }
        val nameWithoutExtension = file.nameWithoutExtension
        val extension = file.extension
        var counter = 1
        var uniqueName: String
        do {
            uniqueName = "${nameWithoutExtension}_$counter.$extension"
            counter++
        } while (File(filePath, uniqueName).exists())
        return uniqueName
    }
}