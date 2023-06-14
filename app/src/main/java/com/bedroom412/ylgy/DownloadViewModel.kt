package com.bedroom412.ylgy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bedroom412.ylgy.model.DownloadRecord


class DownloadViewModel : ViewModel(
) {

    val data: MutableLiveData<List<DownloadRecord>> =
        MutableLiveData<List<DownloadRecord>>().apply {
            value = DownloadService.bulgyHttpDownloadRepository.downloadRecordLists
        }


    fun updateAll() {
        data.value = DownloadService.bulgyHttpDownloadRepository.downloadRecordLists
    }
}