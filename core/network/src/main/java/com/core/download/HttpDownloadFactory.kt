package com.core.download

interface HttpDownloadFactory {
    fun acquireTask(): HttpDownloadTask?
}