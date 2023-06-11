package com.bedroom412.ylgy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 *
 *
 * @property url 链接地址（可能为目录）
 * @property type 类型
 */
@Entity(tableName = "import_source_part", indices = [Index("url")])
data class ImportSourcePart(

    /**
     *
     */
    @PrimaryKey
    var id: Int,
    /**
     * 链接地址（可能为目录）
     */
    @ColumnInfo("url")
    var url: String,
    /**
     * 类型
     */
    @ColumnInfo("type")
    var type: Int?,
    /**
     * 源ID
     */
    @ColumnInfo("source_id")
    var sourceId: Int
) {
}