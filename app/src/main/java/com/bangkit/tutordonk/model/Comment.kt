package com.bangkit.tutordonk.model

data class Comment(
    val createdAt: CreatedAt = CreatedAt(),
    val nama: String = "",
    val content: String = "",
    val email: String = ""
)