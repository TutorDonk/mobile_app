package com.bangkit.tutordonk.view.student.study

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.databinding.FragmentStudentStudyForumBinding
import com.bangkit.tutordonk.view.component.customrecyclerview.model.ForumItem
import com.bangkit.tutordonk.view.navigateWithAnimation
import com.bangkit.tutordonk.view.student.study.detail.DetailForumFragment.Companion.ARG_FORUM_ITEM
import com.google.gson.Gson

class StudyForumFragment : Fragment() {

    private var _binding: FragmentStudentStudyForumBinding? = null
    private val binding get() = _binding!!
    private var isSortClicked = false
    private var spinnerInteracted = false
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentStudyForumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        setupSortIcon()
        setupSortSpinner()
        setupRecyclerView()
    }

    private fun setupSortIcon() = with(binding) {
        ivSort.setOnClickListener {
            toggleSortOptions()
        }
    }

    private fun toggleSortOptions() = with(binding) {
        isSortClicked = !isSortClicked
        spinnerSort.visibility = if (isSortClicked) View.VISIBLE else View.GONE
        ivSort.rotation = if (isSortClicked) 180f else 0f
    }

    private fun setupSortSpinner() = with(binding) {
        spinnerSort.setOnItemSelectedListener {
            if (!spinnerInteracted) {
                spinnerInteracted = true
                return@setOnItemSelectedListener
            }
            if (isSortClicked) {
                applySort(it)
            }
        }
    }

    private fun applySort(selectedSort: String) = with(binding) {
        toggleSortOptions()
        val sortedItems = rvForum.getAllItems().sortedByDescending { item ->
            when (selectedSort.lowercase()) {
                "popularitas" -> item.popularity
                "like" -> item.like
                else -> item.comment
            }
        }
        rvForum.setInitialItems(sortedItems)
    }

    private fun setupRecyclerView() = with(binding) {
        rvForum.setMaxPage(5)
        rvForum.setInitialItems(listOf(ForumItem(0, "User 1", "Initial Title", "Initial Subtitle", 0, 0, 0)))
        rvForum.setOnItemClickListener { forumItem ->
            navigateToDetailForum(forumItem)
        }
    }

    private fun navigateToDetailForum(forumItem: ForumItem) {
        val args = bundleOf(ARG_FORUM_ITEM to Gson().toJson(forumItem))
        navController.navigateWithAnimation(R.id.studyForumFragmenttoDetailForumFragment, args = args)
    }
}
