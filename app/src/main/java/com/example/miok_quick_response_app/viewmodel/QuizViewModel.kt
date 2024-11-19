package com.example.miok_quick_response_app.viewmodel

import ContactDatabase
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.miok_quick_response_app.data.Question
import com.example.miok_quick_response_app.data.QuestionDatabase
import com.example.miok_quick_response_app.database.ProfileDatabase
import com.example.miok_quick_response_app.util.SmsHelper
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// ViewModel class for managing quiz-related data and logic for the QuizFragment.
class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private var questionDb = QuestionDatabase(application)
    private var profileDb = ProfileDatabase(application)
    private var contactDb = ContactDatabase(application)

    private lateinit var smsHelper: SmsHelper

    private var questionsAnswered: List<Question> = emptyList()

    private var birthday = ""
    private lateinit var birthDate: LocalDate
    private var ageRange: String = ""
    private val ageSeparator: Int = 13

    var questionsTamariki: List<Question> = emptyList()
    var questionsRangatahi: List<Question> = emptyList()

    // Initializes the SmsHelper instance with the application context.
    fun initializeSmsHelper() {
        smsHelper = SmsHelper(getApplication())
    }

    // Retrieves a list of questions based on the user's age range, either Tamariki or Rangatahi, using the stored profile's birth date. If the birth date is invalid or unavailable, it defaults to Tamariki questions.
    fun getAllQuestions(): List<Question> {
        questionsTamariki = questionDb.getAllQuestionsFromTamariki()
        questionsRangatahi = questionDb.getAllQuestionsFromRangatahi()

        if (profileDb.hasProfile()) {
            birthday = profileDb.getProfile()?.getBirthDay().toString()

            if (!birthday.isNullOrEmpty()) {
                try {
                    birthDate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    if (isOlderThan(birthDate, ageSeparator)) {
                        ageRange = "Rangatahi"
                        return questionsRangatahi
                    } else {
                        ageRange = "Tamariki"
                        return questionsTamariki
                    }
                } catch (e: DateTimeParseException) {
                    Log.e("QuizViewModel", "Error parsing birth date: $birthday", e)
                }
            }
        }
        return questionsTamariki
    }

    // Checks if the given birth date corresponds to an age greater than or equal to the specified age.
    private fun isOlderThan(birthDate: LocalDate, age: Int): Boolean {
        val currentDate = LocalDate.now()
        val period = Period.between(birthDate, currentDate)
        return period.years >= age
    }

    // Adds the given question to the list of answered questions.
    fun collectResponses(question: Question) {
        questionsAnswered += question
    }

    // Checks if the quiz has been completed by verifying if any questions have been answered, then sends SMS to all contacts and clears the list of answered questions.
    fun quizCompleted() {
        if (questionsAnswered.isEmpty()) {
            // Handle error or notify user that no questions were answered
            Toast.makeText(getApplication(), "No questions answered", Toast.LENGTH_SHORT).show()
            return
        }
        smsMsgAllContact(questionsAnswered)
        questionsAnswered = emptyList() // Clear list after sending messages
    }

    // Prepares and sends a safety survey SMS message to all contacts, including user responses and formatted with specific questions for the age group.
    fun smsMsgAllContact(questions: List<Question>) {
        initializeSmsHelper()
        var contactNumList: MutableList<String> =
            contactDb.getAllContacts().map { it.phone_number }.toMutableList()
        // Adds "+64" New Zealand country code prefix to each number in contacts.
        for (i in contactNumList.indices) {
            contactNumList[i] = "+64" + contactNumList[i]
        }
        // Prepare SMS message content
        val smsStrArr = Array(questions.size + 1) { "" }
        smsStrArr[0] = profileDb.getProfile()?.name.toString() + " has completed a MIOK safety survey, results are:"

        // I know this is ugly but I would have to change the question object to contain an abbreviated
        // term and make a database method to retrieve a list of them and its too late :(
        if (ageRange == "Rangatahi"){
            smsStrArr[1] = "Feel uncomfortable? ~ "
            smsStrArr[2] = "Threatening? ~ "
            smsStrArr[3] = "Scared at home? ~ "
            smsStrArr[4] = "Yelled at you - feel unsafe? ~ "
            smsStrArr[5] = "Need help? ~ "
        } else {
            smsStrArr[1] = "Okay? ~ "
            smsStrArr[2] = "Hurt? ~ "
            smsStrArr[3] = "Clean and fed? ~ "
            smsStrArr[4] = "Yelling? ~ "
            smsStrArr[5] = "Need help? ~ "
        }

        // Adds response to questions in message.
        var i = 0
        for (e in questions) {
            smsStrArr[i+1] += e.userInputAnswer.toString()
            smsStrArr[i] += "\n"
            i++
        }

        var message :  String = ""
        for (line in smsStrArr) {
            message += line
        }
        // Opens a multi recipient sms group and prefills input box for user to press send.
        for (phNum in contactNumList) {
            smsHelper.openSmsAppWithMessage(contactNumList, message)
        }
    }
}

