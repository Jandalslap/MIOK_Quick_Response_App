package com.example.miok_quick_response_app.ui

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.viewmodel.QuizViewModel
import com.example.miok_quick_response_app.data.Question

class QuizFragment : Fragment() {

    private lateinit var quizViewModel : QuizViewModel

    // Access the shared ViewModel scoped to the activity
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var currentQuestionIndex = 0
    lateinit var questions : List<Question>

    private lateinit var questionTextView: TextView
    private lateinit var questionImageView: ImageView
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    private var currentLanguage : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_quiz, container, false)

        quizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        questions = quizViewModel.getAllQuestions()

        // Initialize UI components
        questionTextView = rootView.findViewById(R.id.questionText)
        questionImageView = rootView.findViewById(R.id.questionImageView) // Initialize ImageView
        trueButton = rootView.findViewById(R.id.trueButton)
        falseButton = rootView.findViewById(R.id.falseButton)

        // Set up listeners for the buttons
        trueButton.setOnClickListener {
            questions[currentQuestionIndex].userInputAnswer = true
            goToNextQuestion(questions)
        }

        falseButton.setOnClickListener {
            questions[currentQuestionIndex].userInputAnswer = false
            goToNextQuestion(questions)
        }

        // Hide the back arrow by removing the up button from the action bar.
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false) // Hides the back arrow

        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            // Update the UI or perform actions based on the new language value
            updateLanguageUI(language, questions)
            currentLanguage = language
        }

        //quizViewModel = ViewModel Provider(this).get(QuizViewModel::class.java)


        // Display the first question
        displayCurrentQuestion(questions)

        return rootView
    }



    // Function to display the current question
    private fun displayCurrentQuestion(questions : List<Question>) {
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]
            updateLanguageUI(currentLanguage, questions)
            //questionTextView.text = question.questionTextEng

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
    private fun goToNextQuestion(questions : List<Question>) {
        quizViewModel.collectResponces(questions[currentQuestionIndex])
        currentQuestionIndex++
        if (currentQuestionIndex >= questions.size) {
            //this.questions = emptyList()
            quizViewModel.quizCompleted()
            // Once we reach the end of the questions list, navigate to the result screen
            findNavController().navigate(R.id.action_quizFragment_to_quizResultFragment)
        } else {
            // Otherwise, display the next question
            displayCurrentQuestion(questions)
        }

    }

    private fun updateLanguageUI(language: String, questions : List<Question>) {
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
