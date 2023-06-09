package com.core.download

import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile


//  (0_就绪_1_下载中_2_成功_3_暂停_4_失败
const val STATUS_READY = 0
const val STATUS_DOWNLOADING = 1
const val STATUS_SUCCESS = 2
const val STATUS_PAUSED = 3
const val STATUS_ERROR = 4

class HttpDownloadTask(
    var url: String,
    var filePath: String,
    var fileName: String,
    var offset: Long = 0,
    val taskId: String? = null,
    var attachment: Any? = null,
    private var httpDownloadManagerRef: HttpDownloadManagerImpl,
) {


    var rateLimiter:RateLimiter?=null
    @Volatile
    var status = STATUS_READY

    var call: Call? = null
    var listeners: List<HttpDownloadTaskListener> = mutableListOf()

    fun pause(): Boolean {
        synchronized(this) {
            if (isPaused(status)) {
                return true
            } else if (isSuccess(status)) {
                return false
            }
            status = STATUS_PAUSED
            return true
        }
    }


    fun addListener(listener: HttpDownloadTaskListener) {
        for (li in listeners) {
            if (li == listener) {
                return
            }
        }
        listeners = listeners + listener
    }

     fun start() {
        synchronized(this) {
            if (isDownloading(status)) {
                return
            }
            try {
                val client = httpDownloadManagerRef.newClient()
                val builder = Request.Builder()
                builder.url(url)
                if (offset > 0) {
                    builder.header("Range", "bytes=${offset}-")
                }
                val request = builder.build()
                doStart(client, request)
            } catch (e: Exception) {
                if (isSuccess(status)) {
                    return
                }
                status = STATUS_ERROR
                error(e)
            }
        }
    }

    private fun doStart(client: OkHttpClient, request: Request) {

        status = STATUS_DOWNLOADING
        started()
        // TODO: 协程
        call = client.newCall(request)

        val response: Response?
        var gResponseBody: ResponseBody? = null
        var readBytes = offset
        var contentLength = -1L

        status = STATUS_DOWNLOADING

        call = client.newCall(request)


        try {
            response = call!!.execute()
//            resolveFileNameIfNecessary(response)

            gResponseBody = response.body()!!
//            gResponseBody = responseBody

            contentLength = gResponseBody.contentLength()

            val ins: InputStream = gResponseBody.byteStream()
            val accessFile = RandomAccessFile(File(filePath, fileName), "rw")

            accessFile.seek(offset)

            //Specify the size of the cache buffer for 4MB.
            val b = ByteArray( 1024)

            var len: Int
            //   每次读取的字节长度
            while (status == STATUS_DOWNLOADING) {

                var sMillis = System.currentTimeMillis()

                runBlocking {
                    rateLimiter?.acquire(1024)
                }

                if ((ins.read(b).also { len = it } == -1)) {
                    break
                }
                var eMillis = System.currentTimeMillis()

                // The length of bytes read each time.
                readBytes += len
                // Write the length of bytes read each time.
                accessFile.write(b, 0, len)
                onRead(len, readBytes, contentLength, eMillis - sMillis)
            }
            // Close the connection.
            gResponseBody.close()
        } catch (e: Exception) {
            status = STATUS_ERROR
            error(e)
            gResponseBody?.close()
            return
        }

        // if paused through there
        if (isPaused(status)) {
            paused(readBytes, contentLength)
            return
        }

        status = STATUS_SUCCESS
        finished();
        gResponseBody.close()
    }

    fun isReady(status: Int): Boolean {
        return status == STATUS_READY
    }

    fun isDownloading(status: Int): Boolean {
        return status == STATUS_DOWNLOADING
    }

    fun isSuccess(status: Int): Boolean {
        return status == STATUS_SUCCESS
    }

    fun isPaused(status: Int): Boolean {
        return status == STATUS_PAUSED
    }

    fun isError(status: Int): Boolean {
        return status == STATUS_ERROR
    }

    private fun started() {
        for (listener in listeners) {
            listener.onStarted(this)
        }
    }

    private fun error(e: Exception) {
        for (listener in listeners) {
            listener.onError(this, e)
        }
    }

    private fun finished() {
        for (listener in listeners) {
            listener.onCompleted(this)
        }
    }

    private fun paused( totalReadBytes: Long, contentLength: Long,) {
        for (listener in listeners) {
            listener.onPaused(this, totalReadBytes, contentLength)
        }
    }

//    private fun decorateResponse(response: Response, chain: Interceptor.Chain): Response {
//        return response.newBuilder().body(
//            ProcessingMonitorResponseBody(response.body()!!, this@HttpDownloadTask)
//        ).build()
//    }

    private fun onRead(readBytes: Int, totalReadBytes: Long, contentLength: Long, cost: Long) {
        for (listener in listeners) {
            // TODO:
            listener.onProgressing(this, readBytes, totalReadBytes, contentLength, cost)
        }
    }
}


/*
class ProcessingMonitorResponseBody(
    private var delegate: ResponseBody, var httpDownloadTask: HttpDownloadTask
) : ResponseBody() {

    override fun contentType(): MediaType? {
        return delegate.contentType()
    }

    override fun contentLength(): Long {
        return delegate.contentLength()
    }

    override fun source(): BufferedSource {
        return object : DelegateBufferedSource(delegate.source()) {
            override fun read(sink: Buffer, byteCount: Long): Long {
                val read = super.read(sink, byteCount)
                httpDownloadTask.onRead(read, contentLength())
                return read
            }
        }
    }
}
*/

