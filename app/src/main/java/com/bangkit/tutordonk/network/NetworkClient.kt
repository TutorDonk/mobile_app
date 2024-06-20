package com.bangkit.tutordonk.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClient(context: Context) {

    private var currentToken: String = ""
    private val retrofit: Retrofit

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $currentToken")
                    .build()
                chain.proceed(newRequest)
            }
            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("https://backendapi-d4en3rtxvq-et.a.run.app/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun updateToken(newToken: String) {
        currentToken = newToken
    }

    fun getRetrofit(): Retrofit = retrofit

}
