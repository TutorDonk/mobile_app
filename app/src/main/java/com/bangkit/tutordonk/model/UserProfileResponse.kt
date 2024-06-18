package com.bangkit.tutordonk.model

data class UserProfileResponse(
    val id: String = "",
    val idMLUser: Int = 0,
    val role: String = "",
    val nama: String = "",
    val email: String = "",
    val idML: Int = 0,
    val parentName: String = "",
    val phoneNumber: String = "",
    val gender: String = "",
    val educationLevel: Int = 0,
    val domicile: String = "",
    val parentPhoneNumber: String = ""
)