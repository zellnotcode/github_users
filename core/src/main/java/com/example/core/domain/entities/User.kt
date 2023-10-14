package com.example.core.domain.entities

data class User(
    val id: Int? = null,
    val name: String? = null,
    val login: String? = null,
    val followers: Int? = null,
    val avatarUrl: String? = null,
    val following: Int? = null,
    val type: String? = null,
)
