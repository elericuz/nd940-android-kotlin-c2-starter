package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfTheDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {
    private val apiKey = "rhsa3d06cHbmVC6ufqkSzb86KFEUJPuemi0dqX4d"
    private val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    private val calendar = Calendar.getInstance()
    private val currentTime = calendar.time
    private val startDate = dateFormat.format(currentTime)

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAllAsteroids()) {
            it.asDomainModel()
        }

    val pictureOfTheDay: LiveData<PictureOfTheDay> =
        Transformations.map(database.asteroidDao.getPictureOfTheDay()) {
            it.asDomainModel()
        }

    suspend fun refreshPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            try {
                val pictureOfTheDay = Network.pictureOfTheDay.getPictureOfTheDay(apiKey).await()
                database.asteroidDao.insertPictureOfTheDay(pictureOfTheDay.asDatabaseModel())
            } catch (e: HttpException) {
                Timber.d("e.localizedMessage")
            }
        }
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)
                val endDate = dateFormat.format(calendar.time)

                val asteroids = Network.asteroids.getAsteroids(apiKey, startDate, endDate).await()
                val asteroidsParsed = parseAsteroidsJsonResult(JSONObject(asteroids))
                database.asteroidDao.insertAll(asteroidsParsed)
            } catch (e: HttpException) {
                Timber.d(e.localizedMessage)
            }
        }
    }

    suspend fun truncateTables() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.truncateAsteroidsTable()
            database.asteroidDao.truncatePictureOfTheDayTable()
        }
    }
}
