package com.mzansi.campushub

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "campus_events")
data class EventEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val date: String
)