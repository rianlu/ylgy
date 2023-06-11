package com.bedroom412.ylgy.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * 导入源
 *
 * @property url 地址
 * @property type
 */
@Entity(tableName = "import_source")
data class ImportSource(

    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    @ColumnInfo("url")
    var url: String?,
    @ColumnInfo("type")
    var type: Int?

) {
}