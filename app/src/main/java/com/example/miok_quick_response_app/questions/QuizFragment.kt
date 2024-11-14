package com.example.miok_quick_response_app.questions

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.R

class QuizFragment : Fragment() {

    private val quizViewModel = QuizViewModel()

    // Access the shared ViewModel scoped to the activity
    private val sharedViewModel: SharedViewModel by activityViewModels()

    // Sample question list
    private val questions = quizViewModel.getAllQuestions()

    private var currentQuestionIndex = 0

    private lateinit var questionTextView: TextView
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_quiz, container, false)

        // Initialize UI components
        questionTextView = rootView.findViewById(R.id.questionText)
        trueButton = rootView.findViewById(R.id.trueButton)
        falseButton = rootView.findViewById(R.id.falseButton)

        // Set up listeners for the buttons
        trueButton.setOnClickListener {
            questions[currentQuestionIndex].userInputAnswer = true
            goToNextQuestion()
        }

        falseButton.setOnClickListener {
            questions[currentQuestionIndex].userInputAnswer = false
            goToNextQuestion()
        }

        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            // Update the UI or perform actions based on the new language value
            updateLanguageUI(language)
        }


        // Display the first question
        displayCurrentQuestion()

        return rootView
    }

    // Function to display the current question
    private fun displayCurrentQuestion() {
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]
            questionTextView.text = question.questionTextEng
        }
    }

    // Function to move to the next question
    private fun goToNextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex >= questions.size) {
            // Once we reach the end of the questions list, navigate to the result screen
            findNavController().navigate(R.id.action_quizFragment_to_quizResultFragment)
        } else {
            // Otherwise, display the next question
            displayCurrentQuestion()
        }
    }

    private fun updateLanguageUI(language: String) {
        // Update TextView text based on language
        if (language == "Māori") {
            trueButton.text = "True(TR)"
            falseButton.text = "False(Tr)"
            questionTextView.text = questions[currentQuestionIndex].questionTextTR
        } else {
            // Default to English
            trueButton.text = "True(Eng)"
            falseButton.text = "False(Eng)"
            questionTextView.text = questions[currentQuestionIndex].questionTextEng
        }
    }
}
