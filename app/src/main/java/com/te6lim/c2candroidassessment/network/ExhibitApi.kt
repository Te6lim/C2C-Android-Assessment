package com.te6lim.c2candroidassessment.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.te6lim.c2candroidassessment.model.Exhibit
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://my-json-server.typicode.com/Reyst/exhibit_db/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()


interface ExhibitApiService {

    @GET("list")
    fun getExhibitsAsync(): Deferred<List<Exhibit>>
}

object ExhibitApi {
    val retrofitService: ExhibitApiService by lazy {
        retrofit.create(ExhibitApiService::class.java)
    }
}