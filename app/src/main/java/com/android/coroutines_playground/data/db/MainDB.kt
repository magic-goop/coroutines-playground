package com.android.coroutines_playground.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.coroutines_playground.data.db.dao.DetectiveRiddlesDao
import com.android.coroutines_playground.data.db.entity.detectiveriddles.CategoryEntity
import com.android.coroutines_playground.data.db.entity.detectiveriddles.QuestionEntity

@Database(
    entities = [
        CategoryEntity::class,
        QuestionEntity::class
    ],
    version = Common.DATABASE_VERSION,
    exportSchema = false
)
abstract class MainDB : RoomDatabase() {
    abstract fun detectiveRiddlesDao(): DetectiveRiddlesDao
}