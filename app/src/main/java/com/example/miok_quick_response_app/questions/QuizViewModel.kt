package com.example.miok_quick_response_app.questions

class QuizViewModel {

    val questions = listOf(
        Question("Question text 1(Eng)", "Question text 4(TR)", null),
        Question("Question text 2(Eng)", "Question text 4(TR)", null),
        Question("Question text 3(Eng)", "Question text 4(TR)", null),
        Question("Question text 4(Eng)", "Question text 4(TR)", null)
    )

    fun getAllQuestions(): List<Question>{
        return questions
    }
}

