package com.example.batmobiletest.network

import com.example.batmobiletest.models.YoubikeStop
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("youbike_immediate.json")
    fun getYoubikeStop(): Call<YoubikeStop>
}