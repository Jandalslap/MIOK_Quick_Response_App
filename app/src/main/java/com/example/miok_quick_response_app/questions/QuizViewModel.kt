package com.example.miok_quick_response_app.questions

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.database.ProfileDatabase
import com.example.miok_quick_response_app.model.Profile
import dagger.hilt.android.internal.Contexts.getApplication
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class QuizViewModel() : ViewModel() {

    //private val context: Context = getApplication() // Get application context

    private var questionsTamariki = listOf(
        Question("Are you okay?(Eng)(questionsTamariki)", "Question text 4(TR)",null, R.drawable.emoji_happy),
        Question("Are you hurt?(Eng)(questionsTamariki)", "Question text 4(TR)", null, R.drawable.emoji_hurt),
        Question("Are you clean and fed?(Eng)(questionsTamariki)", "Question text 4(TR)", null, R.drawable.emoji_clean),
        Question("Is someone yelling at you?(Eng)(questionsTamariki)", "Question text 4(TR)", null, R.drawable.emoji_yelling)
    )

    private val questionsRangatahi = listOf(
        Question("Are you okay?(Eng)(questionsRangatahi)", "Question text 4(TR)",null, R.drawable.emoji_happy),
        Question("Are you hurt?(Eng)(questionsRangatahi)", "Question text 4(TR)", null, R.drawable.emoji_hurt),
        Question("Are you clean and fed?(Eng)(questionsRangatahi)", "Question text 4(TR)", null, R.drawable.emoji_clean),
        Question("Is someone yelling at you?(Eng)(questionsRangatahi)", "Question text 4(TR)", null, R.drawable.emoji_yelling)
    )

    //private val profileDatabase = ProfileDatabase(context)
    private val _profile = MutableLiveData<Profile?>()
    private var birthday = ""
    private lateinit var birthDate : LocalDate
    private val ageSeparator : Int = 14

//    fun getAllQuestions(): List<Question>{
//        if (profileDatabase.hasProfile())
//            birthday = (profileDatabase.getProfile()?.getBirthDay().toString())
//            if (birthday != null)
//                // Parse the string to a LocalDate
//                birthDate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//                if (isOlderThan(birthDate, ageSeparator))
//                    return questionsRangatahi
//                else{return questionsTamariki}
//        // If no birthdate can be found use return questionsTamariki
//        return questionsTamariki
//    }

    fun getAllQuestions(): List<Question>{
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
}

