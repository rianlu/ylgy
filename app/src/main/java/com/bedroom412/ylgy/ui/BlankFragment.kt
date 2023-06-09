package com.bedroom412.ylgy.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.bedroom412.player.MetadataReaderUtils
import com.bedroom412.player.MusicData
import com.bedroom412.player.MusicDataAdapter
import com.bedroom412.player.SingletonPlayer
import com.bedroom412.ylgy.databinding.FragmentBlankBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BlankFragment : Fragment() {

    private var _binding: FragmentBlankBinding? = null
    private val binding get() = _binding!!
    lateinit var player: Player

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlankBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val musicDataList = MetadataReaderUtils.getMusicDataList(requireContext())
        val dataAdapter = MusicDataAdapter(musicDataList)
        binding.recyclerView.adapter = dataAdapter
        dataAdapter.setOnItemClickListener(object : MusicDataAdapter.OnItemClickListener {
            override fun onItemCLick(musicData: MusicData, position: Int) {
                musicDataList.forEach {
                    addMediaItem(it.uri)
                }
                player.seekTo(position, 0)
                player.prepare()
                player.play()
            }
        })

        CoroutineScope(Dispatchers.Main).launch {
            // 等一会不然还没初始化好
            delay(2000)
            player = SingletonPlayer.getInstance()
            musicDataList.forEach {
                addMediaItem(it.uri)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun addMediaItem(uri: Uri) {
        val newItem = MediaItem.Builder()
            .setMediaId("$uri")
            .build()
        player.addMediaItem(newItem)
    }
}