package com.bedroom412.ylgy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bedroom412.ylgy.model.DownloadRecord


class DownloadViewModel : ViewModel(
) {

    val data: MutableLiveData<List<DownloadRecord>> =
        MutableLiveData<List<DownloadRecord>>().apply {
            value = geAllTasks()
        }

    private fun geAllTasks(): List<DownloadRecord> {
        var ylgyHttpDownloadFactory =
            DownloadService.httpDownloadManagerImpl.downloadFactory as YlgyHttpDownloadFactory
        return ylgyHttpDownloadFactory.geAllTasks();
    }

    fun updateAll() {
        data.value = geAllTasks()
    }
}