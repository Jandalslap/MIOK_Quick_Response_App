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
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private var questionDb = QuestionDatabase(application)
    private var profileDb = ProfileDatabase(application)

    private var questionsAnswered : List<Question> = emptyList()

    // Used to store the csv of questions and results.
    private var csvResults: ByteArray? = null
    private var csvProfile: ByteArray? = null

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

    fun collectResponces(question: Question){
        questionsAnswered += question
    }

    fun quizCompleted(){
        csvResults = convertQuestionsToCSVInMemory(questionsAnswered)
        csvProfile = convertProfileToCSVInMemory(profileDb.getProfile())
        if (csvResults == null || csvProfile == null){
            // Raise error
            // TODO NOT YET IMPLEMENTED
        }
        else
        {   // Send results to remote database.
            // TODO NOT YET IMPLEMENTED
        }
        for (e in questionsAnswered){
            println(e.questionTextEng + e.userInputAnswer.toString())
        }
    }

    // Used to prepare response to be sent out as a csv to firebase.
    fun convertQuestionsToCSVInMemory(questions: List<Question>): ByteArray? {
        // Create an in-memory byte array output stream
        val byteArrayOutputStream = ByteArrayOutputStream()

        try {
            // Write CSV data to the output stream using OutputStreamWriter
            val writer = OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8)

            // Write the header row
            writer.append("question_text_eng,question_text_tr,user_input_answer,image_res_id\n")

            // Write each question as a row
            for (question in questions) {
                writer.append("${question.questionTextEng},${question.questionTextTR},${question.userInputAnswer ?: "null"},${question.imageResId ?: "null"}\n")
            }

            // Flush and close the writer to ensure all data is written
            writer.flush()

            // Return the byte array representing the CSV data
            return byteArrayOutputStream.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                byteArrayOutputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun convertProfileToCSVInMemory(profile: Profile?): ByteArray? {
        if (profile == null){
            return null
        }
        else {
            // Create an in-memory byte array output stream
            val byteArrayOutputStream = ByteArrayOutputStream()

            try {
                // Write CSV data to the output stream using OutputStreamWriter
                val writer = OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8)

                // Write the header row (matching the Profile object properties)
                writer.append("id,name,email,birthday,address,father_name,mother_name,image_url\n")

                // Write the profile data as a row
                writer.append("${profile.id ?: "null"},")
                writer.append("${profile.name},")
                writer.append("${profile.email},")
                writer.append("${profile.birthday},")
                writer.append("${profile.address},")
                writer.append("${profile.fatherName},")
                writer.append("${profile.motherName},")
                writer.append("${profile.imageUrl ?: "null"}\n")

                // Flush and close the writer to ensure all data is written
                writer.flush()

                // Return the byte array representing the CSV data
                return byteArrayOutputStream.toByteArray()
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            } finally {
                try {
                    byteArrayOutputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}

