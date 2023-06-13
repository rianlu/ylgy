package com.bedroom412.ylgy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bedroom412.ylgy.model.ImportSourceSyncRecord
import com.bedroom412.ylgy.model.PlayList


class SourceViewModel : ViewModel(
) {

    val data: MutableLiveData<List<ImportSourceModel>> =
        MutableLiveData<List<ImportSourceModel>>().apply {
            value = mutableListOf()
        }


    private fun getImportSourceModel(): List<ImportSourceModel> {

        val importSources = YglyApplication.instance.db.importSourceDao().getAll()

        val importSourceModels = importSources?.let {


            it.map {
                var playList: PlayList? = null
                var importSourceSyncRecord: ImportSourceSyncRecord? = null

                it.playlistId?.let {
                    playList = YglyApplication.instance.db.playListDao().getById(it)
                }

                importSourceSyncRecord =
                    YglyApplication.instance.db.importSourceSyncRecordDao().getBySourceId(it.id!!)

                ImportSourceModel(
                    playList = playList,
                    importSource = it,
                    importSourceSyncRecord = importSourceSyncRecord
                )
            }

        }

        return importSourceModels ?: mutableListOf()
    }

    fun updateAll() {
        data.value = getImportSourceModel()
    }
}