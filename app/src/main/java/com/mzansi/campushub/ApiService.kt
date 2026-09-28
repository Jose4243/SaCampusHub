package com.mzansi.campushub

import retrofit2.Call
import retrofit2.http.GET

/**
 * Retrofit service definition describing the REST endpoints consumed by the
 * Mzansi Campus Hub application.
 */
interface ApiService {

    /**
     * Fetches the list of campus events from the backend.
     * Expected endpoint: GET {baseUrl}/events
     *
     * Returns a [Call] (rather than a suspend function) so callers such as
     * [MainActivity] can drive it asynchronously with [Call.enqueue].
     */
    @GET("events")
    fun getEvents(): Call<List<CampusEvent>>
}
