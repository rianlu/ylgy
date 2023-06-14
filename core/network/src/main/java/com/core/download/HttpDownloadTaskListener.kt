package com.core.download


interface HttpDownloadTaskListener {

    fun onStarted(task: HttpDownloadTask) {

    }

    fun onProgressing(
        task: HttpDownloadTask,
        readBytes: Int,
        totalReadBytes: Long, contentLength: Long,
        cost: Long
    ) {

    }

    fun onPaused(task: HttpDownloadTask,  totalReadBytes: Long, contentLength: Long) {

    }

    fun onCompleted(task: HttpDownloadTask) {

    }

    fun onError(task: HttpDownloadTask, ex: Throwable) {

    }
}