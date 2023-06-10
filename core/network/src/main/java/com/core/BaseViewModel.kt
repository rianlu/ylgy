package com.core

import androidx.lifecycle.ViewModel
import com.core.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @Author lu
 * @Date 2023/6/11 00:03
 * @ClassName: BaseViewModel
 * @Description:
 */
class BaseViewModel: ViewModel() {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //TODO 返回类型还没写
    suspend fun getData() {
        return withContext(Dispatchers.IO) {
            val apiService = retrofit.create(ApiService::class.java)
            apiService.getPlaylist()
        }
    }

}

const val BASE_URL = "https://api.uomg.com/"