package com.example.miok_quick_response_app.questions

import com.example.miok_quick_response_app.R

class QuizViewModel {

    val questions = listOf(
        Question("Are you okay?(Eng)", "Question text 4(TR)",null, R.drawable.emoji_happy),
        Question("Are you hurt?(Eng)", "Question text 4(TR)", null, R.drawable.emoji_hurt),
        Question("Are you clean and fed?(Eng)", "Question text 4(TR)", null, R.drawable.emoji_clean),
        Question("Is someone yelling at you?(Eng)", "Question text 4(TR)", null, R.drawable.emoji_yelling)
    )

    fun getAllQuestions(): List<Question>{
        return questions
    }
}

