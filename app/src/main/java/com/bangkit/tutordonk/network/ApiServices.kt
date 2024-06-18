package com.bangkit.tutordonk.network

import com.bangkit.tutordonk.model.TeacherProfileResponse
import com.bangkit.tutordonk.model.UserProfileResponse
import com.bangkit.tutordonk.model.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServices {
    @POST("auth/login")
    fun authLogin(
        @Body fields: Map<String, String>
    ): Call<UserResponse>

    @POST("auth/register")
    fun authRegister(
        @Body fields: Map<String, String>
    ): Call<UserResponse>

    @GET("profile")
    fun userGetProfile(): Call<UserProfileResponse>

    @POST("profile/siswa")
    fun userUpdateProfile(
        @Body fields: Map<String, String>
    ): Call<UserProfileResponse>

    @GET("profile")
    fun teacherGetProfile(): Call<TeacherProfileResponse>

    @POST("profile/tutor")
    fun teacherUpdateProfile(
        @Body fields: TeacherProfileResponse
    ): Call<TeacherProfileResponse>
}
