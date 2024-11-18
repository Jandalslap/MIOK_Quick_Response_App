package com.example.miok_quick_response_app.ui

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.viewmodel.QuizViewModel
import com.example.miok_quick_response_app.data.Question

class QuizFragment : Fragment(R.layout.fragment_quiz) {

    private lateinit var quizViewModel: QuizViewModel
    private lateinit var sendSmsPermissionRequest: ActivityResultLauncher<String>

    // Access the shared ViewModel scoped to the activity
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var currentQuestionIndex = 0
    lateinit var questions: List<Question>

    private lateinit var questionTextView: TextView
    private lateinit var questionImageView: ImageView
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button

    private var currentLanguage: String = ""
    private lateinit var progressBar: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_quiz, container, false)

        quizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        questions = quizViewModel.getAllQuestions()

        // Initialize UI components
        questionTextView = rootView.findViewById(R.id.questionText)
        questionImageView = rootView.findViewById(R.id.questionImageView) // Initialize ImageView
        trueButton = rootView.findViewById(R.id.trueButton)
        falseButton = rootView.findViewById(R.id.falseButton)

        // Initialize the progress bar
        progressBar = rootView.findViewById(R.id.progressBar)

        // Set up listeners for the buttons
        trueButton.setOnClickListener {
            questions[currentQuestionIndex].userInputAnswer = true
            goToNextQuestion(questions)
            updateProgressBar(currentQuestionIndex)
        }

        falseButton.setOnClickListener {
            questions[currentQuestionIndex].userInputAnswer = false
            goToNextQuestion(questions)
            updateProgressBar(currentQuestionIndex)
        }

        // Hide the back arrow by removing the up button from the action bar.
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false) // Hides the back arrow

        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            // Update the UI or perform actions based on the new language value
            updateLanguageUI(language, questions)
            currentLanguage = language
        }

        // Register for permission request to send SMS
        sendSmsPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, send SMS
                quizViewModel.smsMsgAllContact(questions)
            } else {
                Toast.makeText(requireContext(), "SMS permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        // Display the first question
        displayCurrentQuestion(questions)
        updateProgressBar(currentQuestionIndex)

        return rootView
    }

    private fun updateProgressBar(currentQuestionIndex: Int) {

        // Adjust the index to reflect progress after the question is answered
        val adjustedIndex = currentQuestionIndex - 1

        for (i in 0 until progressBar.childCount) {
            val view = progressBar.getChildAt(i)

            // Set all progress bar elements to inactive color by default
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.inactive_color))

            if (i <= adjustedIndex) {
                // Set active color for elements up to the adjusted index
                view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.question_color))
            }
        }
    }

    private fun displayCurrentQuestion(questions: List<Question>) {
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]
            updateLanguageUI(currentLanguage, questions)
            // Set image if available, otherwise hide the ImageView
            question.imageResId?.let {
                questionImageView.setImageResource(it)
                questionImageView.visibility = View.VISIBLE
            } ?: run {
                questionImageView.visibility = View.GONE
            }
        }
    }

    private fun goToNextQuestion(questions: List<Question>) {
        quizViewModel.collectResponses(questions[currentQuestionIndex])
        currentQuestionIndex++
        if (currentQuestionIndex >= questions.size) {
            // Once quiz is completed, send SMS
            quizViewModel.quizCompleted()

            // Navigate to the result screen
            findNavController().navigate(R.id.action_quizFragment_to_quizResultFragment)
        } else {
            // Otherwise, display the next question
            displayCurrentQuestion(questions)
        }
    }

    private fun updateLanguageUI(language: String, questions: List<Question>) {
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
