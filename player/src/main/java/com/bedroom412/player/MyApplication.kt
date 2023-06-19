package com.bedroom412.player

import android.app.Application

/**
 * @Author lu
 * @Date 2023/6/19 15:47
 * @ClassName: MyApplication
 * @Description:
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 初始化播放器及服务
        SingletonPlayer.init(this)
    }
}