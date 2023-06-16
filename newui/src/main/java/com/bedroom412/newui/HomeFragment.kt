package com.bedroom412.newui

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.bedroom412.newui.databinding.FragmentHomeBinding
import com.chad.library.adapter.base.BaseQuickAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataList = listOf(
            Playlist(1, "http://p1.music.126.net/W_fiVVPasqeytxfmHdkwxA==/109951165834048703.jpg?param=177y177", "Kikujiro (Original Motion Picture Soundtrack)", "音楽：久石譲、映画の監督・主演はもちろん北野武 「菊次郎の夏」 トヨタカローラのＣＭ曲としてもおなじみの、 ピアノが奏でる、懐かしさと優しさいっぱいの あのメロディーは、このサウンドトラックから。 それまでの路線からハートウォーム路線に 挑戦した北野映画らしく、 久石の音楽も、今までにまして、親しみやすさが いっぱいのロングセラー盤。"),
            Playlist(2, "http://p2.music.126.net/5WBF8uSFXhLVjvMOCeqflQ==/109951167859171747.jpg?param=177y177", "启示录\n", "华语创作天后G.E.M.邓紫棋第八张全创作专辑《启示录》\n" +
                    "\n" +
                    "七封致天堂的信 七封天堂的回信\n" +
                    "\n" +
                    "为爱的启示掀开神圣序幕\n" +
                    "\n" +
                    "史无前例首创14集音乐连续剧MV G.E.M.为流行乐坛展开历史的新一页"),
            Playlist(3, "http://p1.music.126.net/kVwk6b8Qdya8oDyGDcyAVA==/1364493930777368.jpg?param=177y177", "新的心跳\n", "暌违三年，褪去青涩，已然成熟的G.E.M. 交出这张《新的心跳》； 新的心跳，带来新的声音、新的感触；对过去说再见，她带着轻盈的脚步向着前方的一束光迈进。\n" +
                    "\n" +
                    "《新的心跳》是邓紫棋的第五张专辑，蕴酿三年的制作过程，十首新作，所有词曲由G.E.M.个人创作。以打破传统宣传发行的模式，实体专辑发布前便已在各大网络平台上推出十首单曲及MV，更邀得国际精英导演为所有单曲量身定制MV。视听合一，让听众打开全部感官，自由地接收和体会每一首歌。\n" +
                    "\n" +
                    "一种新的方式，一场新的体验。这是G.E.M.邓紫棋的首张视听专辑， “新的心跳”。"),
            Playlist(4, "https://p1.music.126.net/4NCWg3v2J2yjxk0-ciJs7w==/109951168669105675.jpg?param=200y200", "自然之音/沉浸大自然 静谧与美好之中\n", "像是偏临黄昏时混杂着青绿与烟黄的深山，隐居于此的道士悠然拨动了沉睡的琴弦。恍惚隔世间，雨滴凝聚在微倾的檐侧，晕染一片青苔。猛然惊觉，却是黄粱一梦，歇了车马，散了秋凉."),
            Playlist(5, "https://p1.music.126.net/g2_Gv0dtAicJ3ChTYu28_g==/1393081239628722.jpg?param=200y200", "深度睡眠 |重度失眠者专用歌单\n", "介绍： 好希望这里可以成为大家的一个庇护所\n" +
                    "在深夜安安静静不吵不闹\n" +
                    "路过的朋友留下一个故事或沉默\n" +
                    "大家都彼此默契\n" +
                    "等待天明。"),
            Playlist(6, "https://p1.music.126.net/ndGlV_dfHvAonoDr5SL3XA==/109951164247329551.jpg?param=200y200", "公路音乐|纵情追逐梦想与远方\n", "介绍： 关于公路\n" +
                    "当提到公路的时候，第一时间想到应该是Route 66、Highway 25之类的美国公路。美国一直就有着特有的公路文化,比如说美国50号公路,就被冠以全美最孤独的公路之名。而这个独特的66号公路,则有着近百年的历史了,同时见证了美国历史的变化,也衍生出了其特有的文化与影响力，被美国人亲切地唤作“母亲之路”，除了这些世界还有成千上万条公路。"),
        )

        val adapter = PlaylistAdapter()
        binding.playlistRecyclerView.adapter = adapter
        binding.playlistRecyclerView.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(35, 35, 35, 35)
            }
        })
        adapter.setOnItemClickListener{ adapter, view, position ->

        }
        adapter.submitList(dataList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}