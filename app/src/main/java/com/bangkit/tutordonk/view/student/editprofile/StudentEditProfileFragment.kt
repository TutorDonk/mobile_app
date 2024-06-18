package com.bangkit.tutordonk.view.student.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bangkit.tutordonk.databinding.FragmentStudentEditProfileBinding
import com.bangkit.tutordonk.model.UserProfileResponse
import com.bangkit.tutordonk.network.ApiServiceProvider
import com.bangkit.tutordonk.utils.SharedPreferencesHelper
import org.koin.android.ext.android.inject

class StudentEditProfileFragment : Fragment() {
    private var _binding: FragmentStudentEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val apiServiceProvider: ApiServiceProvider by inject()
    private val sharedPreferences: SharedPreferencesHelper by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentEditProfileBinding.inflate(inflater, container, false)
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
        btnUpdate.setOnClickListener { doUpdateData() }
    }

    private fun doUpdateData() {
        with(binding) {
            val reqBody = mapOf(
                "domicile" to tietAddress.text.toString(),
                "phoneNumber" to tietPhoneNumber.text.toString(),
                "parentPhoneNumber" to tietParentPhoneNumber.text.toString(),
                "parentName" to tietParentName.text.toString(),
                "educationLevel" to tietEducationLevel.text.toString(),
                "gender" to spinnerGender.value,
            )

            val callback = apiServiceProvider.createCallback<UserProfileResponse> { response ->
                sharedPreferences.saveUsername(response.nama)
                navController.popBackStack()
            }

            apiServiceProvider.apiService.userUpdateProfile(reqBody).enqueue(callback)
        }
    }
}