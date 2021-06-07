package com.udacity.asteroidradar.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.*
import com.udacity.asteroidradar.AsteroidApplication
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)

    private val asteroidRepository = AsteroidRepository(database)

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid> get() = _navigateToSelectedAsteroid

    init {
        if (AsteroidApplication.checkConnectivity(application.applicationContext)) {
            viewModelScope.launch {
                asteroidRepository.refreshPictureOfTheDay()
                asteroidRepository.refreshAsteroids()
            }
        } else {
            Timber.w("There's no internet connection, Retrieving Data from Cache...")
        }
    }

    val asteroidList = asteroidRepository.asteroids
    val pictureOfTheDay = asteroidRepository.pictureOfTheDay

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    fun displayDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }
}