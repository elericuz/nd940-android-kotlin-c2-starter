package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Database

@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroid: List<DatabaseAsteroid>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictureOfTheDay(picture: DatabasePicture)

    @Query("select * from table_asteroid order by closeApproachDate asc")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from table_picture_of_the_day limit 1")
    fun getPictureOfTheDay(): LiveData<DatabasePicture>

    @Query("delete from table_asteroid")
    fun truncateAsteroidsTable()

    @Query("delete from table_picture_of_the_day")
    fun truncatePictureOfTheDayTable()
}

@Database(entities = [DatabaseAsteroid::class, DatabasePicture::class], version = 2, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "theasteroid"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}