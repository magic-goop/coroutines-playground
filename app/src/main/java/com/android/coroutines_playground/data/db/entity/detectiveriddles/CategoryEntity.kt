package com.android.coroutines_playground.data.db.entity.detectiveriddles

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CategoryEntity.TABLE_NAME)
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = ID) var id: Int,
    @ColumnInfo(name = TITLE) var title: String?,
    @ColumnInfo(name = DESCRIPTION) var description: String?,
    @ColumnInfo(name = IMAGE) var image: String?,
    @ColumnInfo(name = PRIORITY) var priority: Int = 0,
    @ColumnInfo(name = TOTAL) var total: Int = 0,
    @ColumnInfo(name = ANSWERED) var answered: Int = 0
) {
    companion object {
        const val TABLE_NAME = "Category"
        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val IMAGE = "image"
        const val TOTAL = "total_count"
        const val ANSWERED = "answered"
        const val PRIORITY = "priority"
    }
}