package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AsteroidDatabaseDao {

    @Insert
    suspend fun insert(asteroid: Asteroid)

    @Update
    suspend fun update(asteroid: Asteroid)

    @Query("select * from asteroid_table where id = :id")
    suspend fun get(id: Long): Asteroid?

    @Query("select * from asteroid_table order by id desc")
    fun getAllAsteroids(): LiveData<List<Asteroid>>
}