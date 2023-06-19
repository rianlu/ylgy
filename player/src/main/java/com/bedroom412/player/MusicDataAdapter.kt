package com.bedroom412.player

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * @Author lu
 * @Date 2023/6/18 16:49
 * @ClassName: MusicDataAdapter
 * @Description:
 */
class MusicDataAdapter(private val musicDataList: List<MusicData>) : RecyclerView.Adapter<MusicDataAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music_data_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = musicDataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            val musicData = musicDataList[position]
            if (musicData.album != null) {
                findViewById<ImageView>(R.id.coverImg).setImageBitmap(musicData.album)
            }
            findViewById<TextView>(R.id.musicTitle).text = musicData.name
            findViewById<TextView>(R.id.artistName).text = musicData.singer
            setOnClickListener {
                listener?.onItemCLick(musicData, position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemCLick(musicData: MusicData, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}