package com.bangkit.tutordonk.view.model

data class ListTutor(
    val data: List<Tutor> = emptyList()
)

data class Tutor(
    val name: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val rate: String = "",
    val urlCertificate: String = "",
)
