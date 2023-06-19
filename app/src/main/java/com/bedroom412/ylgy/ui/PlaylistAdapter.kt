package com.bedroom412.ylgy.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseDifferAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder

/**
 * @Author lu
 * @Date 2023/6/15 17:53
 * @ClassName: PlaylistAdapter
 * @Description:
 */
class PlaylistAdapter: BaseDifferAdapter<Playlist, QuickViewHolder>(EntityDiffCallback()) {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        // 创建 ViewHolder
        return QuickViewHolder(R.layout.item_playlist_card, parent)
    }

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: Playlist?) {
        val coverImageView = holder.getView<ImageView>(R.id.coverImage)
        item?.apply {
            Glide.with(context).load(coverUrl)
                .dontAnimate()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d(LOG_TAG, "Error: ${e?.message.toString()}}")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                })
                .into(coverImageView)
            holder.getView<TextView>(R.id.playlistTitle).text = title
            holder.getView<TextView>(R.id.playlistInfo).text = info
        }
    }

    class EntityDiffCallback: DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.title == newItem.title
        }
    }
}