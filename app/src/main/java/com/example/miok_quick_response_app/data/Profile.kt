package com.example.miok_quick_response_app.model

data class Profile(
    val id: Int? = null,
    val name: String,
    val email: String,
    val birthday: String,
    val address: String,
    val fatherName: String,
    val motherName: String,
    val imageUrl: String? = null
)
