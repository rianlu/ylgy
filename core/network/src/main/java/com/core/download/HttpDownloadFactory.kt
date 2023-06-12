package com.core.download

interface HttpDownloadFactory {
    fun getTasks(): List<HttpDownloadTask>?
}