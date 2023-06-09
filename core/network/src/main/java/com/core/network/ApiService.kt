package com.core.network

import retrofit2.http.GET

/**
 * @Author lu
 * @Date 2023/6/10 00:32
 * @ClassName: ApiService
 * @Description:
 */
interface ApiService {

    @GET
    suspend fun getPlaylist()
}