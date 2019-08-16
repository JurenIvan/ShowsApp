package com.example.shows_jurenivan.data.dataStructures

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.shows_jurenivan.data.dataStructures.dao.ShowsDao

@Database(
    version = 1,
    entities = [
        Show::class
    ]
)
abstract class ShowsDatabase : RoomDatabase() {

    abstract fun showsDao(): ShowsDao

}