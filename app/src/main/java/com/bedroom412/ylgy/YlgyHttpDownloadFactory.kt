package com.bedroom412.ylgy

import com.bedroom412.ylgy.model.DownloadRecord
import com.core.download.HttpDownloadFactory
import com.core.download.HttpDownloadManagerImpl
import com.core.download.HttpDownloadTask

class YlgyHttpDownloadFactory(
    var httpDownloadManagerImpl: HttpDownloadManagerImpl
) : HttpDownloadFactory {

    var downloadRecordLists: List<DownloadRecord>? = mutableListOf()

    init {
        downloadRecordLists = YglyApplication.instance.db.downloadRecordDao().getAll()
    }

    override fun getTasks(): List<HttpDownloadTask>? {
        return downloadRecordLists?.map { downloadRecord ->
            HttpDownloadTask(
                taskId = downloadRecord.id?.toString(),
                url = downloadRecord.url,
                filePath = downloadRecord.downloadPath,
                fileName = downloadRecord.fileName,
                offset = downloadRecord.offset ?: 0,
                httpDownloadManagerRef = httpDownloadManagerImpl
            )
        }
    }

    fun addTask(downloadRecord: DownloadRecord) {

        downloadRecordLists = downloadRecordLists!!.plus(downloadRecord)
        httpDownloadManagerImpl.newTask(
            taskId = downloadRecord.id.toString(),
            url = downloadRecord.url,
            filePath = downloadRecord.downloadPath,
            fileName = downloadRecord.fileName,
            startImmediately = false,
            attachment = downloadRecord
        )
    }

    fun updateAll() {
        downloadRecordLists?.forEach {
            YglyApplication.instance.db.downloadRecordDao().update(it)
        }
    }

}