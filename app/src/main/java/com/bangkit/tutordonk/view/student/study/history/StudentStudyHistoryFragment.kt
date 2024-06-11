package com.bangkit.tutordonk.view.student.study.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.databinding.FragmentStudentStudyHistoryBinding
import com.bangkit.tutordonk.view.component.historyrecyclerview.model.HistoryItem
import com.bangkit.tutordonk.view.navigateWithAnimation
import com.bangkit.tutordonk.view.student.booking.BookingTutorFragment
import com.google.gson.Gson

class StudentStudyHistoryFragment : Fragment() {
    private var _binding: FragmentStudentStudyHistoryBinding? = null
    private val binding get() = _binding!!

    private var isSortClicked = false
    private var spinnerInteracted = false
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentStudyHistoryBinding.inflate(inflater, container, false)
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
        val sortedItems = rvHistory.getAllItems().sortedByDescending { item ->
            when (selectedSort.lowercase()) {
                "matkul" -> item.major
                else -> item.status
            }
        }
        rvHistory.setInitialItems(sortedItems)
    }

    private fun setupRecyclerView() = with(binding) {
        rvHistory.setMaxPage(5)
        rvHistory.setInitialItems(listOf(HistoryItem(0, "SUCCESS", "Kalkulus", "Prof Hamka", "24-10-2024")))
        rvHistory.setOnItemClickListener { data ->
            if (data.status == "SUCCESS") navigateToBookingTutor(data)
        }
    }

    private fun navigateToBookingTutor(data: HistoryItem) {
        val args = bundleOf(
            BookingTutorFragment.ARG_HISTORY_ITEM to Gson().toJson(data),
            BookingTutorFragment.ARG_FROM_HISTORY to true
        )
        navController.navigateWithAnimation(R.id.bookingTutorFragment, args = args)
    }
}