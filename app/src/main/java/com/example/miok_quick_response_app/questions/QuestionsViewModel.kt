package com.example.miok_quick_response_app.questions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.miok_quick_response_app.data.QuestionDao
import kotlinx.coroutines.launch
import androidx.room.Room
import com.example.miok_quick_response_app.data.Question
import com.example.miok_quick_response_app.data.QuestionDatabase


class QuestionsViewModel(application: Application) : AndroidViewModel(application) {

    private val questionDao: QuestionDao

    init {
        // Initialize the Room database manually
        val database = Room.databaseBuilder(
            application.applicationContext,
            QuestionDatabase::class.java, "question_db"
        )
            .fallbackToDestructiveMigration() // In case of database version upgrade
            .build()

        questionDao = database.questionDao() // Get DAO instance from the database
    }

    // Get list of questions asynchronously
    fun getQuestions(): List<Question> {
        return questionDao.getAllQuestions()  // Synchronously return the list
    }
}