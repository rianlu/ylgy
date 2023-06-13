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
import java.util.LinkedList
import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicLong

class HttpDownloadManagerImpl(
    var downloadConfig: DownloadConfig,
    var listeners: List<HttpDownloadTaskListener> = mutableListOf(),
    var downloadFactory: HttpDownloadFactory?
) : HttpDownloadManager {

    private var TAG = "HttpDownloadManager"
    private var totalReceiveBytes = AtomicLong(0)
    private var totalTimeUsed = AtomicLong(0)
    private val downloadQueue = LinkedList<HttpDownloadTask>()
    private val downloadingQueue = LinkedList<HttpDownloadTask>()

    private val haveTaskToHandle = Object()


    private val maxConcurrentDownloads = Semaphore(downloadConfig.maxConcurrentDownloads)

    private val scope = CoroutineScope(Job() + Dispatchers.Default)

    private var isStart = false

    fun init() {
        listeners += StatHttpDownloadTaskListener(this)
        downloadFactory?.getTasks()?.let {
            downloadQueue.addAll(it)
        };

        synchronized(haveTaskToHandle){
            haveTaskToHandle.notify()
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

                    maxConcurrentDownloads.acquire()

                    downloadQueue.firstOrNull {
                        it.status == STATUS_READY
                    }?.let {
                        downloadQueue.remove(it)
                        downloadingQueue.offer(it)
                        // execute task
                        launch(Dispatchers.IO) {
                            Log.d(TAG, "task begin ${it}")
                            it.start()
                        }
                    } ?: {
                        synchronized(haveTaskToHandle){
                            haveTaskToHandle.wait()
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
        downloadingQueue.forEach {
            it.pause()
        }
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
            synchronized(haveTaskToHandle){
                haveTaskToHandle.notify()
            }

        }
        return httpDownloadTask
    }

    override fun startTask(task: HttpDownloadTask): HttpDownloadTask {
        val t = hookTask(task);
        downloadQueue.offer(t)
        synchronized(haveTaskToHandle){
            haveTaskToHandle.notify()
        }
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

        override fun onCompleted(task: HttpDownloadTask) {
            httpDownloadManagerImpl.downloadingQueue.filter { it.taskId.equals(task.taskId) }.also {
                httpDownloadManagerImpl.downloadingQueue.removeAll(
                    it
                )
            }
            httpDownloadManagerImpl.maxConcurrentDownloads.release()
        }

        override fun onError(task: HttpDownloadTask, ex: Throwable) {
            httpDownloadManagerImpl.downloadingQueue.filter { it.taskId.equals(task.taskId) }.also {
                httpDownloadManagerImpl.downloadingQueue.removeAll(
                    it
                )
            }
            httpDownloadManagerImpl.maxConcurrentDownloads.release()

        }

        override fun onPaused(task: HttpDownloadTask) {
            httpDownloadManagerImpl.downloadingQueue.filter { it.taskId.equals(task.taskId) }.also {
                httpDownloadManagerImpl.downloadingQueue.removeAll(
                    it
                )
            }
            httpDownloadManagerImpl.maxConcurrentDownloads.release()
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