package com.bangkit.tutordonk.view.onboarding.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.databinding.FragmentLoginBinding
import com.bangkit.tutordonk.model.UserResponse
import com.bangkit.tutordonk.network.ApiServiceProvider
import com.bangkit.tutordonk.utils.SharedPreferencesHelper
import com.bangkit.tutordonk.utils.navigateWithAnimation
import com.bangkit.tutordonk.view.student.StudentHomeActivity
import com.bangkit.tutordonk.view.teacher.TeacherActivity
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val sharedPreferences: SharedPreferencesHelper by inject()
    private val apiServiceProvider: ApiServiceProvider by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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
        btnLogin.setOnClickListener { doLogin() }
        btnRegister.setOnClickListener { navController.navigateWithAnimation(R.id.loginFragmentToRegisterFragment) }
    }

    private fun doLogin() {
        with(binding) {
            val callback = apiServiceProvider.createCallback<UserResponse>(
                onSuccess = { userResponse ->
                    apiServiceProvider.updateToken(userResponse.token)
                    sharedPreferences.saveRole(userResponse.role)
                    startActivity(
                        Intent(
                            requireContext(),
                            if (userResponse.role == "siswa") StudentHomeActivity::class.java else TeacherActivity::class.java
                        ).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                }
            )

            apiServiceProvider.apiService.authLogin(
                mapOf(
                    "email" to tietEmail.text.toString(),
                    "password" to tietPassword.text.toString()
                )
            ).enqueue(callback)
        }
    }
}