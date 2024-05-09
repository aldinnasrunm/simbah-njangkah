package com.example.simbahnjangkah

import android.app.Application
import androidx.room.Room

class App : Application() {

    companion object{
        lateinit var userDatabase : AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        userDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }
}