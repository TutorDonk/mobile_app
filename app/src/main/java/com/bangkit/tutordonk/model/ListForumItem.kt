package com.bangkit.tutordonk.model

data class ListForumItem(
    val id: String = "",
    val createdAt: CreatedAt = CreatedAt(),
    val nama: String = "",
    val tag: List<String> = listOf(),
    val title: String = "",
    val content: String = "",
    val email: String = "",
    val likes: Int = 0,
    val comments: List<Comment> = listOf()
)