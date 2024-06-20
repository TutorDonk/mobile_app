package com.bangkit.tutordonk.network

import com.bangkit.tutordonk.model.*
import retrofit2.Call
import retrofit2.http.*

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

    @GET("booking/pelajaran")
    fun listStudy(): Call<List<String>>

    @GET("booking/tutor/{course}")
    fun listTeacher(
        @Path("course") course: String
    ): Call<List<TeacherListItem>>

    @GET("booking")
    fun bookingHistory(): Call<List<ListBookingItem>>

    @POST("booking")
    fun bookingTutor(
        @Body fields: Map<String, String>
    ): Call<Unit>

    @POST("forum")
    fun createForum(
        @Body fields: Map<String, String>
    ): Call<Unit>

    @GET("forum")
    fun listForum(): Call<List<ListForumItem>>

    @POST("forum/{id}/comment")
    fun sendComment(
        @Path("id") forumId: String,
        @Body fields: Map<String, String>
    ): Call<Unit>

    @PATCH("forum/{id}/like")
    fun sendLikes(
        @Path("id") forumId: String,
        @Body fields: Map<String, Boolean>
    ): Call<Unit>
}
