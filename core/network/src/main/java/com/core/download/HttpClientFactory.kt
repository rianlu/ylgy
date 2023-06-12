package com.core.download

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

object HttpClientFactory {

    fun newClient(): OkHttpClient {
        return OkHttpClient.Builder().build();
    }


    fun getMeta(url: String): HttpInfo {

        var ex : Exception? = null
        var response: Response? = null
        var redirectUrl: String = ""
        var fileName: String = url.substring(url.lastIndexOf('/') + 1)
        var size: Long = -1
        var contentLength: String? = null

        try {
            response = newClient().newCall(Request.Builder().url(url).head().build()).execute()
            print(response.headers())


            if (!response.isSuccessful) {
                throw Exception("Failed to retrieve file information: ${response.code()}")
            }

            redirectUrl = url

            if (response.isRedirect) {
                redirectUrl = response.header("Location")!!
                response =
                    newClient().newCall(Request.Builder().url(redirectUrl).head().build()).execute()
            }


            // Try to extract the filename from the Content-Disposition header
            val contentDisposition = response.header("Content-Disposition")
            if (contentDisposition != null) {
                val startIndex = contentDisposition.indexOf("filename=")
                if (startIndex != -1) {
                    fileName = contentDisposition.substring(startIndex + 9).replace("\"", "")
                }
            }
            contentLength = response.header("Content-Length")

            if (!contentLength.isNullOrBlank()) {
                size = contentLength.toLong()
            }
        } catch (e: Exception) {
            ex = e
        }


        return HttpInfo(
            response = response,
            size = size,
            url = redirectUrl,
            fileName = fileName,
            e = ex
        )
    }
}