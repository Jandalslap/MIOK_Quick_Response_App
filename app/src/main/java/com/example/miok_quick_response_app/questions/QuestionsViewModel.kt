package com.example.miok_quick_response_app.questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miok_quick_response_app.data.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.miok_quick_response_app.data.QuestionDao
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val questionDao: QuestionDao
    //private val state: SavedStateHandle
) : ViewModel() {

    // ViewModel method (with async coroutine)
    fun getQuestions(): List<Question> {
        var questionsList: List<Question> = listOf()

        // Launch a coroutine to collect the questions and wait for the result
        viewModelScope.launch {
            // Collect the flow and store the list of questions
            questionsList = questionDao.getAllQuestions().first()  // Use `first` to block until data is available
        }

        return questionsList
    }

    // You can access saved state values like this
    // Example:
    // val someValue = state.get<String>("key")

    // You can add functions here to interact with your DAO, etc.

}