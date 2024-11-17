package com.example.miok_quick_response_app.viewmodel

import ContactDatabase
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.miok_quick_response_app.database.ProfileDatabase
import com.example.miok_quick_response_app.data.Question
import com.example.miok_quick_response_app.data.QuestionDatabase
import com.example.miok_quick_response_app.miscUtil.FirestoreHelper
import com.example.miok_quick_response_app.miscUtil.SmsHelper
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private var questionDb = QuestionDatabase(application)
    private var profileDb = ProfileDatabase(application)
    private var contactDb = ContactDatabase(application)

    //private var firestoreHelper = FirestoreHelper()
    private var smsHelper = SmsHelper(application)

    private var questionsAnswered : List<Question> = emptyList()

    // Used to store the csv of questions and results.
    private var csvResults: ByteArray? = null
    private var csvProfile: ByteArray? = null

    private var birthday = ""
    private lateinit var birthDate : LocalDate
    private val ageSeparator : Int = 13

    var questionsTamariki : List<Question> = emptyList()
    var questionsRangatahi : List<Question> = emptyList()

    fun getAllQuestions(): List<Question> {
        questionsTamariki = questionDb.getAllQuestionsFromTamariki()
        questionsRangatahi = questionDb.getAllQuestionsFromRangatahi()

        if (profileDb.hasProfile()) {
            // Get the birthday string
            birthday = profileDb.getProfile()?.getBirthDay().toString()

            // Check if birthday is neither null nor empty
            if (!birthday.isNullOrEmpty()) {
                try {
                    // Parse the string to a LocalDate
                    birthDate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    if (isOlderThan(birthDate, ageSeparator)) {
                        return questionsRangatahi
                    } else {
                        return questionsTamariki
                    }
                } catch (e: DateTimeParseException) {
                    // Handle the exception if parsing fails
                    Log.e("QuizViewModel", "Error parsing birth date: $birthday", e)
                }
            }
        }
        // If no birthdate can be found or there's an error, return the default list
        return questionsTamariki
    }

    private fun isOlderThan(birthDate: LocalDate, age: Int): Boolean {
        // Get the current date
        val currentDate = LocalDate.now()

        // Calculate the period (difference) between the birth date and current date
        val period = Period.between(birthDate, currentDate)

        // If the age difference (in years) is greater than or equal to the target age, return true
        return period.years >= age
    }

    fun collectResponses(question: Question){
        questionsAnswered += question
    }

    fun quizCompleted(){
//        csvResults = convertQuestionsToCSVInMemory(questionsAnswered)
//        csvProfile = convertProfileToCSVInMemory(profileDb.getProfile())
        if (csvResults == null || csvProfile == null){
            // Raise error
            // TODO NOT YET IMPLEMENTED
        }
        else
        {   // Send results to remote database.
            // TODO NOT YET IMPLEMENTED
        }
        for (e in questionsRangatahi){
            println(e.questionTextEng + e.userInputAnswer.toString())
        }
        //smsMsgAllContact(questionsAnswered)
        // Clear list
        questionsAnswered = emptyList()
    }

    // Function to add profile and get the generated ID
//    fun addProfile(profile: Profile) {
//        viewModelScope.launch {
//            val profileId = firestoreHelper.addProfile(profile)
//            if (profileId != null) {
//                // Do something with the generated ID (e.g., save it, associate with other data)
//                println("Profile added with ID: $profileId")
//            } else {
//                println("Failed to add profile")
//            }
//        }
//    }


    fun smsMsgAllContact(questions: List<Question>){
        var contactNumList : List<String> = emptyList()
        for (e in contactDb.getAllContacts()){contactNumList += e.phone_number}

        var smsStrArr = Array(questions.size+1) { "" }
        smsStrArr[0] = profileDb.getProfile()?.name.toString() + " has completed a MIOK safety survey.\n Questions and answers " +
                "will be displayed in a series of messages as follows:"
        var i : Int = 1
        for (e in questions ) {
            smsStrArr[i] = e.questionTextEng + "\n"
            smsStrArr[i] = e.questionTextTR + "\n"
            smsStrArr[i] = e.userInputAnswer.toString() + "\n"
            i ++
        }
        for (phNum in contactNumList){
            for (str in smsStrArr)
                smsHelper.sendSms(phNum, str)
        }
    }




}

