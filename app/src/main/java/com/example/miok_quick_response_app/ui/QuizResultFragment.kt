package com.example.miok_quick_response_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.miok_info_app.viewmodel.SharedViewModel
import com.example.miok_quick_response_app.R
import com.example.miok_quick_response_app.databinding.FragmentQuizResultBinding

// Fragment responsible for displaying the results of the quiz, showing the user's performance and feedback.
class QuizResultFragment : Fragment() {

    private var _binding: FragmentQuizResultBinding? = null
    private val binding get() = _binding!!

    // Access the shared ViewModel scoped to the activity
    private val sharedViewModel: SharedViewModel by activityViewModels()

    // onCreateView inflates the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize the binding
        _binding = FragmentQuizResultBinding.inflate(inflater, container, false)
        return binding.root
            }

    // onViewCreated is called after the view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide the back arrow by removing the up button from the action bar.
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false) // Hides the back arrow


        // Observe the currentLanguage LiveData from SharedViewModel
        sharedViewModel.currentLanguage.observe(viewLifecycleOwner) { language ->
            // Update the UI or perform actions based on the new language value
            updateLanguageUI(language)
        }

        // Access the views by their IDs
        val quizResultImageView: ImageView = view.findViewById(R.id.quizResultImageView)
        val quizResultTextView: TextView = view.findViewById(R.id.quizResultTextView)


    }

    // Function to update UI based on the language
    private fun updateLanguageUI(language: String) {
        // Update TextView text based on language
        if (language == "Māori") {
            binding.quizResultTextView.text = getString(R.string.quizResultTextView_mr)

        } else {
            // Default to English
            binding.quizResultTextView.text = getString(R.string.quizResultTextView)
        }
    }

    // Clear the binding when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}