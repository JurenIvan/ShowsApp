package com.example.shows_jurenivan.data.repositories

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Room
import com.example.shows_jurenivan.MyShowsApp
import com.example.shows_jurenivan.data.dataStructures.Show
import com.example.shows_jurenivan.data.dataStructures.ShowsDatabase
import java.util.concurrent.Executors

object ShowsDatabaseRepositoryRepository {

    private val database: ShowsDatabase =
        Room.databaseBuilder(MyShowsApp.instance, ShowsDatabase::class.java, "shows-database")
            .fallbackToDestructiveMigration()
            .build()

    private val executor = Executors.newSingleThreadExecutor()

    fun getShow(id: String): LiveData<Show> = database.showsDao().getShow(id)

    fun insertShow(show: Show) {
        executor.execute { database.showsDao().insertShow(show) }
    }

}