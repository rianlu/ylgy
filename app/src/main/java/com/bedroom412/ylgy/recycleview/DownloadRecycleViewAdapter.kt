package com.bedroom412.ylgy.recycleview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bedroom412.ylgy.DownloadViewModel
import com.bedroom412.ylgy.databinding.ActivityDownloadRecycleItemBinding
import com.bedroom412.ylgy.model.DownloadRecord

class DownloadRecycleViewAdapter(val vm: DownloadViewModel) :
    RecyclerView.Adapter<DownloadRecycleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadRecycleViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycleview_item_fragment_discover_content_category, null)
        val _binding = ActivityDownloadRecycleItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DownloadRecycleViewHolder(_binding)
    }

    override fun getItemCount(): Int {
        return vm.data.value?.size ?: 0
    }


    override fun onBindViewHolder(holder: DownloadRecycleViewHolder, position: Int) {
        vm.data.value?.let {
            val downloadRecord: DownloadRecord = it[position];
            holder._binding.percentTV.text = downloadRecord.percent?.toString() ?: "0.00%"
            holder._binding.titleTV.text = downloadRecord.fileName
        }
    }
}

class DownloadRecycleViewHolder(
    val _binding: ActivityDownloadRecycleItemBinding
) : RecyclerView.ViewHolder(_binding.root) {

}