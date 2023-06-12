package com.core.download

interface HttpDownloadManager {


//    /**
//     * 任务列表
//     *
//     * @return 任务列表
//     */
//    fun tasks(): List<HttpDownloadTask>

    /**
     * 新建一个任务
     *
     * @param url
     * @param startImmediately 是否立即开始
     * @return
     */
    fun newTask(
        taskId: String? = null,
        url: String,
        filePath: String,
        fileName: String,
        offset: Long = 0,
        startImmediately: Boolean = true
    ): HttpDownloadTask

    /**
     * 开始一个任务
     *
     * @param task
     * @return
     */
    fun startTask(task: HttpDownloadTask): HttpDownloadTask

    /**
     * 暂停一个任务
     *
     * @param task
     * @return
     */
    fun pauseTask(task: HttpDownloadTask): HttpDownloadTask

}