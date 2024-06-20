package com.bangkit.tutordonk.model


data class ListBookingItem(
    val id: String = "",
    val createdAt: CreatedAt = CreatedAt(),
    val idTutor: String = "",
    val namaTutor: String = "",
    val course: String = "",
    val idSiswa: String = "",
    val namaSiswa: String = "",
    val jamTutor: String = "",
    val status: Int = 0
)