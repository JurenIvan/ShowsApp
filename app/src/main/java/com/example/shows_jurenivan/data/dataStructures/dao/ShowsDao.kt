package com.example.shows_jurenivan.data.dataStructures.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.example.shows_jurenivan.data.dataStructures.Show

@Dao
interface ShowsDao {

    @Query("SELECT * from shows where id=:showId")
    fun getShow(showId: String): LiveData<Show>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertShow(vararg show: Show)

    @Update
    fun updateShow(show: Show)

}