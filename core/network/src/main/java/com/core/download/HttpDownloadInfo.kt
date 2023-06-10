package com.core.download

/**
 * 下载信息
 */
class HttpDownloadInfo(

    var id: String? = null,
    /**
     * (总片段)
     */
    var allSize: Long = 0,
    /**
     * (已下载片段)
     */
    var offset: Long = 0,
    /**
     * (下载百分比)
     */
    var percent: Float? = null,
    /**
     * 下载地址
     */
    var url: String? = null,
    /**
     * 存放路径
     */
    var filePath: String? = null,
    /**
     * (平均速度)
     */
    var avgSpeed: Int? = 0,
    /**
     * (请求头)
     */
    var httpHeader: String? = null,
    /**
     * (文件名称)
     */
    var fileName: String? = null,
    /**
     * (重试次数)
     */
    var retryTimes: Int? = 0,
    /**
     * (0_就绪_1_下载中_2_成功_3_暂停_4_失败
     */
    var status: Int? = 0,
    /**
     * 开始时间
     */
    var startTs: Int? = null,
    /**
     * 结束时间
     */
    var endTs: Int? = null,

    /**
     * 是否删除
     */
    var isDel: Int? = null,

    ) {



}