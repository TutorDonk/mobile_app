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
import com.bangkit.tutordonk.databinding.FragmentStudentStudyHistoryBinding
import com.bangkit.tutordonk.model.ListBookingItem
import com.bangkit.tutordonk.network.ApiServiceProvider
import com.bangkit.tutordonk.utils.navigateWithAnimation
import com.bangkit.tutordonk.view.student.booking.BookingTutorFragment
import com.google.gson.Gson
import org.koin.android.ext.android.inject

class StudentStudyHistoryFragment : Fragment() {
    private var _binding: FragmentStudentStudyHistoryBinding? = null
    private val binding get() = _binding!!

    private var isSortClicked = false
    private var spinnerInteracted = false
    private lateinit var navController: NavController

    private val apiServiceProvider: ApiServiceProvider by inject()

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

    override fun onResume() {
        super.onResume()
        val callback = apiServiceProvider.createCallback<List<ListBookingItem>>(
            onSuccess = {
                with(binding) {
                    rvHistory.setInitialItems(it)
                    rvHistory.setOnItemClickListener { data ->
                        if (data.status == 0) navigateToBookingTutor(data)
                    }
                }
            }
        )

        apiServiceProvider.apiService.bookingHistory().enqueue(callback)
    }

    private fun setupUI() {
        setupSortIcon()
        setupSortSpinner()
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
                "matkul" -> item.course
                else -> item.status.toString()
            }
        }
        rvHistory.setInitialItems(sortedItems)
    }

    private fun navigateToBookingTutor(data: ListBookingItem) {
        val args = bundleOf(
            BookingTutorFragment.ARG_HISTORY_ITEM to Gson().toJson(data),
            BookingTutorFragment.ARG_FROM_HISTORY to true
        )
        navController.navigateWithAnimation(R.id.bookingTutorFragment, args = args)
    }
}