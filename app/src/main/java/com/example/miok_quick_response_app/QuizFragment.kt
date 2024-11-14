package com.example.miok_quick_response_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.data.Question
import com.example.miok_quick_response_app.data.QuestionDatabase
import com.example.miok_quick_response_app.questions.QuestionAdapter
import com.example.miok_quick_response_app.questions.QuestionsViewModel

class QuizFragment : Fragment() {

    private lateinit var viewModel: QuestionsViewModel
    private lateinit var questionsRecyclerView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var questionList: List<Question>

    // Access the shared ViewModel scoped to the activity
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        // Initialize RecyclerView
        questionsRecyclerView = view.findViewById(R.id.questionsRecyclerView)
        questionsRecyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize the Room database and get the QuestionDao
        val database = Room.databaseBuilder(requireContext(), QuestionDatabase::class.java, "app_db")
            .fallbackToDestructiveMigration()
            .build()
        val questionDao = database.questionDao()

        // Manually create the ViewModel
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(QuestionsViewModel::class.java)) {
                    return QuestionsViewModel(requireActivity().application) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }).get(QuestionsViewModel::class.java)

        // Use the ViewModel to get the questions
        questionList = viewModel.getQuestions()

        // Set the adapter
        questionAdapter = QuestionAdapter(questionList)
        questionsRecyclerView.adapter = questionAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the currentLanguage LiveData from SharedViewModel
        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            updateLanguageUI(language)
        }
    }

    private fun updateLanguageUI(language: String) {
        // Update UI based on language
    }
}