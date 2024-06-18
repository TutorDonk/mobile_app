package com.bangkit.tutordonk.component.forumrecyclerview.model

data class ForumItem(
    val id: Int = 0,
    val user: String = "",
    val title: String = "",
    val subtitle: String = "",
    val like: Int = 0,
    val comment: Int = 0,
    val popularity: Int = 0,
    val listComment: User = User(),
)
