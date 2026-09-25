package com.mzansi.campushub

/**
 * Data model representing a single campus event returned by the backend API.
 *
 * @property id unique identifier for the event
 * @property title short title of the event
 * @property description longer description of what the event is about
 * @property date human-readable date string for when the event takes place
 */
data class CampusEvent(
    val id: Int,
    val title: String,
    val description: String,
    val date: String
)
