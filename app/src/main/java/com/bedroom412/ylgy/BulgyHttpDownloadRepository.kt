package com.bedroom412.ylgy

import android.util.Log
import com.bedroom412.ylgy.model.DownloadRecord
import com.core.download.HttpDownloadManagerImpl
import com.core.download.HttpDownloadTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Collections

class BulgyHttpDownloadRepository(
    var httpDownloadManagerImpl: HttpDownloadManagerImpl
) {

    var TAG = "BulgyHttpDownloadRepository"
    var downloadRecordLists: List<DownloadRecord> = Collections.synchronizedList(mutableListOf())

    suspend fun loadAllTasks() {
        withContext(Dispatchers.IO) {
            downloadRecordLists =
                YglyApplication.instance.db.downloadRecordDao().getAll() ?: mutableListOf()
            Log.d(TAG,"Loaded count of task from db was ${downloadRecordLists.size}")
        }
    }

    fun addAndStartTask(downloadRecord: DownloadRecord, isStart:Boolean ) {
        downloadRecordLists = downloadRecordLists.plus(downloadRecord)
        if (isStart) {
            httpDownloadManagerImpl.newTask(
                taskId = downloadRecord.id.toString(),
                url = downloadRecord.url,
                filePath = downloadRecord.downloadPath,
                fileName = downloadRecord.fileName,
                startImmediately = false,
                attachment = downloadRecord
            )
        }
    }





    fun updateAll() {
        downloadRecordLists.forEach {
            YglyApplication.instance.db.downloadRecordDao().update(it)
        }
    }

}