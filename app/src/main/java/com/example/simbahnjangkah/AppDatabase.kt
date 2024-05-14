package com.example.simbahnjangkah

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [User::class, Training::class, TestData::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun trainingDao(): TrainingDao
    abstract fun testDataDao(): TestDataDao

    companion object {
        val DATABASE_NAME = "userDatabase"
    }
}