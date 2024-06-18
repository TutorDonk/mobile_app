package com.bangkit.tutordonk.view.student.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.component.forumrecyclerview.model.ForumItem
import com.bangkit.tutordonk.databinding.FragmentStudentDashboardBinding
import com.bangkit.tutordonk.model.UserProfileResponse
import com.bangkit.tutordonk.network.ApiServiceProvider
import com.bangkit.tutordonk.utils.SharedPreferencesHelper
import com.bangkit.tutordonk.utils.isAllFieldsNotEmpty
import com.bangkit.tutordonk.utils.navigateWithAnimation
import com.bangkit.tutordonk.view.detailforum.DetailForumActivity
import com.bangkit.tutordonk.view.student.StudentHomeActivity
import com.google.gson.Gson
import org.koin.android.ext.android.inject

class StudentDashboardFragment : Fragment() {
    private var _binding: FragmentStudentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val apiServiceProvider: ApiServiceProvider by inject()
    private val sharedPreferences: SharedPreferencesHelper by inject()

    private fun shareVM() = (activity as StudentHomeActivity).data

    override fun onResume() {
        super.onResume()
        checkIsAllFieldNotEmpty()
    }

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
        rvForumHottest.setOnItemClickListener { data ->
            startActivity(Intent(requireContext(), DetailForumActivity::class.java).also {
                it.putExtra(DetailForumActivity.INTENT_FORUM_ITEM, Gson().toJson(data))
            })
        }

        cvBookingTutor.setOnClickListener { navController.navigateWithAnimation(R.id.homeFragmentTobookingTutorFragment) }
        cvForumHistory.setOnClickListener { navController.navigateWithAnimation(R.id.homeFragmentTostudyForumFragment) }
        cvStudyHistory.setOnClickListener { navController.navigateWithAnimation(R.id.studyHistoryFragment) }
    }

    private fun checkIsAllFieldNotEmpty() {
        val callback = apiServiceProvider.createCallback<UserProfileResponse> { response ->
            shareVM().name.value = response.nama
            sharedPreferences.saveUsername(response.nama)
            if (response.isAllFieldsNotEmpty().not()) navController.navigateWithAnimation(R.id.editProfileFragment)
        }
        apiServiceProvider.apiService.userGetProfile().enqueue(callback)
    }
}