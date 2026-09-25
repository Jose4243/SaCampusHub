package com.mzansi.campushub

import retrofit2.Response
import retrofit2.http.GET

/**
 * Retrofit service definition describing the REST endpoints consumed by the
 * Mzansi Campus Hub application.
 */
interface ApiService {

    /**
     * Fetches the list of campus events from the backend.
     * Expected endpoint: GET {baseUrl}/events
     */
    @GET("events")
    suspend fun getEvents(): Response<List<CampusEvent>>
}
