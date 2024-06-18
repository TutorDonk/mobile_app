package com.bangkit.tutordonk.model

data class TeacherProfileResponse(
    val nama: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val gender: String = "",
    val educationLevel: Int = 0,
    val subjects: List<String> = listOf(),
    val domicile: String = "",
    val certifications: List<CertificationData> = listOf()
)

data class CertificationData(
    val name: String = "",
    val url: String = "",
)