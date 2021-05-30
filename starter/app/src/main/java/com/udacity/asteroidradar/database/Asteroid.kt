package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asteroid_table")
data class Asteroid(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous: Boolean = false
)