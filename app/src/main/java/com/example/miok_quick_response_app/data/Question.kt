package com.example.miok_quick_response_app.data

// Data class representing a question with two languages
class Question(
    var questionTextEng: String = "",
    var questionTextTR: String = "",
    var userInputAnswer: Boolean?,
    var imageResId: Int? = null // Add this property for the image resource ID
)