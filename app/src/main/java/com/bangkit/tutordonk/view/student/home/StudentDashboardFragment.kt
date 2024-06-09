package com.bangkit.tutordonk.view.student.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.databinding.FragmentStudentDashboardBinding
import com.bangkit.tutordonk.view.component.customrecyclerview.model.ForumItem
import com.bangkit.tutordonk.view.navigateWithAnimation

class StudentDashboardFragment : Fragment() {
    private var _binding: FragmentStudentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setLayoutListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setLayoutListener() = with(binding) {
        rvForumHottest.setItemsPerPage(10)
        rvForumHottest.setMaxPage(3)
        rvForumHottest.setInitialItems(listOf(ForumItem(0, "User 1", "Initial Title", "Initial Subtitle", 1, 1, 1)))

        cvBookingTutor.setOnClickListener { navController.navigateWithAnimation(R.id.homeFragmentTobookingTutorFragment) }
        cvForumHistory.setOnClickListener { navController.navigateWithAnimation(R.id.homeFragmentTostudyForumFragment) }
    }
}