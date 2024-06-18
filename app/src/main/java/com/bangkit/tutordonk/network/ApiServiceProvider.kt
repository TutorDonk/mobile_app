package com.bangkit.tutordonk.network

import android.content.Context
import android.widget.Toast
import com.bangkit.tutordonk.utils.handleResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiServiceProvider(private val context: Context) : KoinComponent {
    private val networkClient: NetworkClient by inject()

    val apiService: ApiServices
        get() = networkClient.retrofit.create(ApiServices::class.java)

    fun <T> createCallback(onSuccess: (response: T) -> Unit): Callback<T> {
        return object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.handleResponse(context)) response.body()?.let { onSuccess(it) }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
