package com.bedroom412.ylgy.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bedroom412.ylgy.model.Audio
import com.bedroom412.ylgy.model.DownloadRecord
import com.bedroom412.ylgy.model.ImportSource
import com.bedroom412.ylgy.model.ImportSourceSyncRecord
import com.bedroom412.ylgy.model.Lyric
import com.bedroom412.ylgy.model.PlayList
import com.bedroom412.ylgy.model.PlaylistAudioCrossRef
import com.bedroom412.ylgy.model.SourceAudioCrossRef
import com.bedroom412.ylgy.model.SyncRecordSegment

/**
 *
 *
 */
@Database(
    entities = [
        Audio::class,
        DownloadRecord::class,
        ImportSource::class,
        ImportSourceSyncRecord::class,
        Lyric::class,
        PlayList::class,
        PlaylistAudioCrossRef::class,
        SourceAudioCrossRef::class,
        SyncRecordSegment::class], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     *
     *
     * @return
     */
    abstract fun importSourceDao(): ImportSourceDao
    abstract fun importSourceSyncRecordDao(): ImportSourceSyncRecordDao
    abstract fun syncRecordSegmentDao(): SyncRecordSegmentDao
    abstract fun downloadRecordDao(): DownloadRecordDao

    abstract fun playListDao(): PlayListDao
}