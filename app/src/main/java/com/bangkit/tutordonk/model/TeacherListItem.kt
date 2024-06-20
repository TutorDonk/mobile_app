package com.bangkit.tutordonk.model

data class TeacherListItem(
    val id: String = "",
    val idMLTutor: Int = 0,
    val password: String = "",
    val role: String = "",
    val nama: String = "",
    val email: String = "",
    val idML: Int = 0,
    val phoneNumber: String = "",
    val gender: String = "",
    val educationLevel: Int = 0,
    val subjects: List<String> = listOf(),
    var selectedSubjects: String = "",
    var selectedDateTime: String = "",
    val feePerHour: Int = 0,
    val domicile: String = "",
    val certifications: List<CertificationData> = listOf()
)