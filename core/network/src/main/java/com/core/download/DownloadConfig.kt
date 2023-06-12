package com.core.download

import java.util.concurrent.Semaphore

class DownloadConfig(
    val downloadPath: String = "/downloads",
    val maxConcurrentDownloads:Int = 5
) {
}