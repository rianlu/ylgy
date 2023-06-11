package com.core.download

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.InputStream
import java.io.RandomAccessFile
import java.util.regex.Matcher
import java.util.regex.Pattern


//  (0_就绪_1_下载中_2_成功_3_暂停_4_失败
const val STATUS_READY = 0
const val STATUS_DOWNLOADING = 1
const val STATUS_SUCCESS = 2
const val STATUS_PAUSED = 3
const val STATUS_ERROR = 4

class HttpDownloadTask(
    var url: String,
    var filePath: String,
    var fileName: String? = null,
    var offset: Long = 0,
    val taskId: String? = null,
    private var httpDownloadManagerRef: HttpDownloadManagerImpl,
) {

    @Volatile
    var status = STATUS_READY

    var call: Call? = null
    var listeners: List<HttpDownloadTaskListener> = mutableListOf()

    fun pause(): Boolean {
        if (isPaused(status)) {
            return true
        } else if (isSuccess(status)) {
            return false
        }
        call?.cancel();
        status = STATUS_PAUSED
        return true
    }


    fun addListener(listener: HttpDownloadTaskListener) {
        for (li in listeners) {
            if (li == listener) {
                return
            }
        }
        listeners = listeners + listener
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun startAsync() {

        val client = httpDownloadManagerRef.newClient()

        /*   client.interceptors().add(object : Interceptor {
               override fun intercept(chain: Interceptor.Chain): Response {
                   val response = this.intercept(chain)
                   return decorateResponse(response, chain)
               }
           })*/
        var builder = Request.Builder()
        builder.url(url)
        if (offset > 0) {
            builder.header("Range", "bytes=${offset}-")
        }
        val request = builder.build()

        val job = GlobalScope.launch(Dispatchers.IO) {
            doStart(client, request)
        }
    }

    private fun doStart(client: OkHttpClient, request: Request) {

        status = STATUS_DOWNLOADING
        started()
        // TODO: 协程
        call = client.newCall(request)

        var response: Response?
        var gResponseBody: ResponseBody? = null

        try {
            response = call!!.execute()
            resolveFileNameIfNecessary(response)

            gResponseBody = response.body()!!
//            gResponseBody = responseBody

            val contentLength = gResponseBody.contentLength()

            val ins: InputStream = gResponseBody.byteStream()
            val accessFile = RandomAccessFile("${filePath}/${fileName}", "rw")

            accessFile.seek(offset)

            //Specify the size of the cache buffer for 4MB.
            val b = ByteArray(4 * 1024 * 1024)
            var readBytes = offset
            var len: Int
            //   每次读取的字节长度
            while (status == STATUS_DOWNLOADING) {

                var sMillis = System.currentTimeMillis()
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
            paused()
            return
        }

        status = STATUS_SUCCESS
        finished();
        response?.body()!!.close()
    }

    private fun resolveFileNameIfNecessary(response: Response) {
        val contentDispositionHeader = response.header("Content-Disposition")
        if (contentDispositionHeader != null) {
            val matcher: Matcher =
                Pattern.compile("filename=\"?([^\"]+)\"?").matcher(contentDispositionHeader)
            if (matcher.find()) {
                fileName = matcher.group(1) as String
            }
        }
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

    private fun paused() {
        for (listener in listeners) {
            listener.onPaused(this)
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

