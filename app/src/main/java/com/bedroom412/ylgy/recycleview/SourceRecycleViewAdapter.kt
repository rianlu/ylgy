package com.bedroom412.ylgy.recycleview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bedroom412.ylgy.ImportSourceModel
import com.bedroom412.ylgy.SourceViewModel
import com.bedroom412.ylgy.databinding.ActivityDownloadRecycleItemBinding
import com.bedroom412.ylgy.databinding.ActivitySourceRecycleItemBinding

class SourceRecycleViewAdapter(val vm: SourceViewModel) :
    RecyclerView.Adapter<SourceRecycleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceRecycleViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycleview_item_fragment_discover_content_category, null)
        val _binding = ActivitySourceRecycleItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SourceRecycleViewHolder(_binding)
    }

    override fun getItemCount(): Int {
        return vm.data.value?.size ?: 0
    }


    override fun onBindViewHolder(holder: SourceRecycleViewHolder, position: Int) {
        vm.data.value?.let {
            val importSourceModel: ImportSourceModel = it[position];
            holder._binding.sourceCountTV.text = importSourceModel.importSource.cnt?.toString() ?:""
            holder._binding.sourceNameTV.text = importSourceModel.importSource.name?:""
            holder._binding.playListNameTV.text = importSourceModel.playList?.name?:""
            holder._binding.sourceSyncPB.progress = importSourceModel.importSourceSyncRecord?.percent?.toInt()?:0

        }
    }
}

class SourceRecycleViewHolder(
    val _binding: ActivitySourceRecycleItemBinding
) : RecyclerView.ViewHolder(_binding.root) {

}