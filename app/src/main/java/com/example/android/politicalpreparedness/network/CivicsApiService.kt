package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.ElectionOfficial
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

//we need moshi to parse json into kotlin objects
private val moshi = Moshi.Builder()
    .add(ElectionAdapter())
    .add(Date::class.java, Rfc3339DateJsonAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()

//in order for Moshi's annotations to work with kotlin we need KotlinJsonAdapterFactory

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(CivicsHttpClient.getClient())
    .baseUrl(BASE_URL)
    .build()

//to enable retrofit to produce coroutines based api: CoroutineCallAdapterFactory
//version 2.6.0 + retrofit has built in suspend support.
//so no need for addCallAdapter(CoroutineCallAdapterFactory())

//we need scalar factory to parse json as a string
private val retrofitScalar = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(CivicsHttpClient.getClient())
    .baseUrl(BASE_URL)
    .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {

    @GET("elections")
    suspend fun getElections(): ElectionResponse

    @GET("voterinfo")
    suspend fun getVoterInfo(
        @Query("address") address: String,
        @Query("electionId") electionId: String,
        @Query("officialOnly") official: Boolean = false,
        @Query("returnAllAvailableData") available: Boolean = true
    ): VoterInfoResponse

    //TODO: Add representatives API Call
    @GET("representatives")
    suspend fun getRepresentativesInfo(@Query("address") address: String):RepresentativeResponse
}

object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}