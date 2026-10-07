package com.jastin.expensync.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val profilePictureUrl: String? = null
)