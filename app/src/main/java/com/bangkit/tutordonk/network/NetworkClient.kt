package com.bangkit.tutordonk.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClient(private val context: Context, private val token: String = "") {

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .addInterceptor(AuthInterceptor(token))
            .build()
    }

    private val gson = GsonBuilder()
        .setStrictness(Strictness.LENIENT)
        .create()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://backendapi-d4en3rtxvq-et.a.run.app/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private class AuthInterceptor(private val token: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest: Request = chain.request()
            val requestBuilder = originalRequest.newBuilder()

            if (token.isNotEmpty()) requestBuilder.header("Authorization", "Bearer $token")

            val requestWithHeaders: Request = requestBuilder.build()
            return chain.proceed(requestWithHeaders)
        }
    }
}
