package com.core.network

import com.core.download.DownloadConfig
import com.core.download.HttpDownloadManagerImpl
import com.core.download.HttpDownloadTask
import com.core.download.HttpDownloadTaskListener
import org.junit.Test

class HttpDownloadManagerImplTests {

    var dm: HttpDownloadManagerImpl = HttpDownloadManagerImpl(
        DownloadConfig()
    )

    @Test
    fun testDownload() {
        dm.listeners += object : HttpDownloadTaskListener {
            override fun onStarted(task: HttpDownloadTask) {
                print("onStart| task.filePath = ${task.filePath}", )
            }

            override fun onProgressing(
                task: HttpDownloadTask,
                readBytes: Int,
                totalReadBytes: Long,
                contentLength: Long,
                cost: Long
            ) {
                print("onProgressing | readBytes = ${readBytes},  totalReadBytes = ${totalReadBytes}, contentLength = ${contentLength}, cost = ${cost}", )
            }

            override fun onError(task: HttpDownloadTask, ex: Throwable) {
                ex.printStackTrace()
            }

            override fun onCompleted(task: HttpDownloadTask) {
                print("onCompleted")
            }
        }
        dm.newTask(url = "https://baidu.com");

    }
}