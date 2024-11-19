package com.example.miok_quick_response_app.model

// Data class representing a user's profile with personal information
data class Profile(
    val id: Int? = null,
    val name: String,
    val email: String,
    val birthday: String,
    val address: String,
    val fatherName: String,
    val motherName: String,
    val imageUrl: String? = null
) {
    fun getBirthDay(): String? {
        if (!birthday.contentEquals("") || !birthday.equals(null))
            return birthday.toString()
        else return null
    }
}

