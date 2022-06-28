package com.example.android.politicalpreparedness.network

import android.util.Log
import okhttp3.OkHttpClient

class CivicsHttpClient : OkHttpClient() {

    companion object {

        const val API_KEY = "AIzaSyDBrFKBO5ifVcVyM5qGh_CAUIxqDJSlr-c"

        fun getClient(): OkHttpClient {
            return Builder()
                .addInterceptor { chain ->
                    val original = chain.request()

                    val url = original
                        .url()
                        .newBuilder()
                        .addQueryParameter("key", API_KEY)
                        .build()

                    Log.e("CivisHttpClient: ",url.toString())

                    val request = original
                        .newBuilder()
                        .url(url)
                        .build()

                    Log.e("CivisHttpClient: ",request.toString())
                    chain.proceed(request)
                }
                .build()
        }

    }

}