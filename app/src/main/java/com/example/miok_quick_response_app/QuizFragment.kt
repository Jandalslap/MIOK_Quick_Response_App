package com.example.miok_quick_response_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.data.Question
import com.example.miok_quick_response_app.questions.QuestionAdapter
import com.example.miok_quick_response_app.questions.QuestionsViewModel

class QuizFragment : Fragment() {

    private val viewModel: QuestionsViewModel by viewModels()

    private lateinit var questionsRecyclerView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var questionList: List<Question>

    // Access the shared ViewModel scoped to the activity
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        // Initialize the RecyclerView
        questionsRecyclerView = view.findViewById(R.id.questionsRecyclerView)
        questionsRecyclerView.layoutManager = LinearLayoutManager(context)

        // Create a list of questions (you would probably get this from a ViewModel or a database)
//        questionList = listOf(
//            Question("Is 2 + 2 equal to 4?", answerTrue = true),
//            Question("Is the sky green?", answerTrue = false)
//            // Add more questions as needed
//        )
        questionList = viewModel.getQuestions()

        // Set the adapter for the RecyclerView
        questionAdapter = QuestionAdapter(questionList)
        questionsRecyclerView.adapter = questionAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the currentLanguage LiveData from SharedViewModel
        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            // Handle changes to the language (update UI, etc.)
            updateLanguageUI(language)
        }
    }

    // Function to update UI based on the language
    private fun updateLanguageUI(language: String) {
        // Update any UI elements or perform other actions based on the new language
        // For example:
        // textViewLanguage.text = language
    }
}