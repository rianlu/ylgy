package com.core.download

import okhttp3.Response
import java.lang.Exception

/**
 * 下载信息
 */
data class HttpInfo(

    var response: Response?,

    var e: Exception?,
    /**
     * (总片段)
     */
    var size: Long?,
    /**
     * 下载地址
     */
    var url: String,
    /**
     * (文件名称)
     */
    var fileName: String,


    ) {


}