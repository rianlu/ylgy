package com.core.network

import com.core.download.DownloadConfig
import com.core.download.HttpDownloadManagerImpl
import com.core.download.HttpDownloadTask
import com.core.download.HttpDownloadTaskListener
import org.junit.Test
import java.util.concurrent.CountDownLatch

class HttpDownloadManagerImplTests {

    var dm: HttpDownloadManagerImpl = HttpDownloadManagerImpl(
        DownloadConfig()
    )

    @Test
    fun testDownload() {
        dm.listeners += object : HttpDownloadTaskListener {
            override fun onStarted(task: HttpDownloadTask) {
                println("onStart| task.filePath = ${task.filePath}", )
            }

            override fun onProgressing(
                task: HttpDownloadTask,
                readBytes: Int,
                totalReadBytes: Long,
                contentLength: Long,
                cost: Long
            ) {
                println("onProgressing | readBytes = ${readBytes},  totalReadBytes = ${totalReadBytes}, contentLength = ${contentLength}, cost = ${cost}", )
            }

            override fun onError(task: HttpDownloadTask, ex: Throwable) {
                ex.printStackTrace()
            }

            override fun onCompleted(task: HttpDownloadTask) {
                println("onCompleted")
            }
        }
        dm.newTask(url = "http://music.163.com/song/media/outer/url?id=436346833");

        CountDownLatch(1).await()
    }
}