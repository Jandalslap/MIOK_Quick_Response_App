package com.example.miok_quick_response_app.questions

import Contact
import ContactDatabase
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.database.ProfileDatabase
import com.example.miok_quick_response_app.model.Profile
import dagger.hilt.android.internal.Contexts.getApplication
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class QuizViewModel(application: Application) : AndroidViewModel(application) {

   private var questionDb = QuestionDatabase(application)
   private var profileDb = ProfileDatabase(application)

    private val _profile = MutableLiveData<Profile?>()
    private var birthday = ""
    private lateinit var birthDate : LocalDate
    private val ageSeparator : Int = 14

    fun getAllQuestions(): List<Question> {
        questionDb.addQuestionsToDatabase()
        val questionsTamariki : List<Question> = questionDb.getAllQuestionsFromTamariki()
        val questionsRangatahi : List<Question> = questionDb.getAllQuestionsFromRangatahi()

        if (profileDb.hasProfile()) {
            // Get the birthday string
            birthday = profileDb.getProfile()?.getBirthDay().toString()

            // Check if birthday is neither null nor empty
            if (!birthday.isNullOrEmpty()) {
                try {
                    // Parse the string to a LocalDate
                    birthDate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
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

//    fun getAllQuestions(): List<Question>{
//        return questionsTamariki
//    }

    private fun isOlderThan(birthDate: LocalDate, age: Int): Boolean {
        // Get the current date
        val currentDate = LocalDate.now()

        // Calculate the period (difference) between the birth date and current date
        val period = Period.between(birthDate, currentDate)

        // If the age difference (in years) is greater than or equal to the target age, return true
        return period.years >= age
    }
}

