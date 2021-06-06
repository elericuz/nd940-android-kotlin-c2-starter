package com.udacity.asteroidradar.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabasePicture
import com.udacity.asteroidradar.domain.PictureOfTheDay

@JsonClass(generateAdapter = true)
data class NetworkPictureOfTheDay(
    val date: String,
    val explanation: String,
    @Json(name = "media_type") val mediaType: String,
    val title: String,
    val url: String
)

fun NetworkPictureOfTheDay.asDomainModel(): PictureOfTheDay {
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

fun NetworkPictureOfTheDay.asDatabaseModel(): DatabasePicture {
    return let {
        DatabasePicture(
            id = 1,
            date = it.date,
            explanation = it.explanation,
            mediaType = it.mediaType,
            title = it.title,
            url = it.url
        )
    }
}