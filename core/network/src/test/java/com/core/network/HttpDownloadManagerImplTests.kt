package com.core.network

import com.core.download.DownloadConfig
import com.core.download.HttpDownloadManagerImpl
import com.core.download.HttpDownloadTask
import com.core.download.HttpDownloadTaskListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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
//        dm.newTask(url = "http://music.163.com/song/media/outer/url?id=436346833");
//
        CountDownLatch(1).await()
    }


    @Test
    fun test111() {
       runBlocking {

           launch {
               coroutineScope {
                   delay(4000)
                   println("1111")
               }

               withContext(Dispatchers.IO){
                   delay(1000)
                   println("44444")
               }

               println("5555")
           }


           println("3333")
       }
    }
}