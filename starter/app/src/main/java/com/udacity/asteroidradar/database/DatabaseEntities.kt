package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfTheDay

@Entity(tableName = "table_asteroid")
data class DatabaseAsteroid constructor(
    @PrimaryKey var id: Long,
    var codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

@Entity(tableName = "table_picture_of_the_day")
data class DatabasePicture constructor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val explanation: String,
    val mediaType: String,
    val title: String,
    val url: String
)

fun DatabasePicture.asDomainModel(): PictureOfTheDay {
    return let {
        PictureOfTheDay(
            date = it.date,
            explanation = it.explanation,
            mediaType = it.mediaType,
            title = it.title,
            url = it.url
        )
    }
}