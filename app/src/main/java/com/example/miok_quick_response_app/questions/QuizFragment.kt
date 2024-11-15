package com.example.miok_quick_response_app.questions

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.application
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.R

class QuizFragment : Fragment() {

    private val quizViewModel = QuizViewModel()
    var questions : List<Question> = quizViewModel.getAllQuestions()

    // Access the shared ViewModel scoped to the activity
    private val sharedViewModel: SharedViewModel by activityViewModels()

    // Sample question list
    //private val questions = quizViewModel.getAllQuestions()
//    private var questions : List<Question> = listOf(
//    Question("Are you okay?(Eng)(questionsTamariki)", "Question text 4(TR)",null, R.drawable.emoji_happy),
//    Question("Are you hurt?(Eng)(questionsTamariki)", "Question text 4(TR)", null, R.drawable.emoji_hurt),
//    Question("Are you clean and fed?(Eng)(questionsTamariki)", "Question text 4(TR)", null, R.drawable.emoji_clean),
//    Question("Is someone yelling at you?(Eng)(questionsTamariki)", "Question text 4(TR)", null, R.drawable.emoji_yelling)
//    )

    private var currentQuestionIndex = 0

    private lateinit var questionTextView: TextView
    private lateinit var questionImageView: ImageView
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
        questionImageView = rootView.findViewById(R.id.questionImageView) // Initialize ImageView
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

        //quizViewModel = ViewModel Provider(this).get(QuizViewModel::class.java)


        // Display the first question
        displayCurrentQuestion()

        return rootView
    }



    // Function to display the current question
    private fun displayCurrentQuestion() {
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]
            questionTextView.text = question.questionTextEng

            // Set image if available, otherwise hide the ImageView
            question.imageResId?.let {
                questionImageView.setImageResource(it)
                questionImageView.visibility = View.VISIBLE
            } ?: run {
                questionImageView.visibility = View.GONE
            }
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
