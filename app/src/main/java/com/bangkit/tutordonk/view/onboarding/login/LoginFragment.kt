package com.bangkit.tutordonk.view.onboarding.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.databinding.FragmentLoginBinding
import com.bangkit.tutordonk.view.navigateWithAnimation
import com.bangkit.tutordonk.view.student.StudentHomeActivity

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var name = ""

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
        tietEmail.doOnTextChanged { text, _, _, _ ->
            name = text.toString().substringBefore("@")
        }
        btnLogin.setOnClickListener {
            startActivity(Intent(requireContext(), StudentHomeActivity::class.java).apply {
                this.putExtra(StudentHomeActivity.NAME, name)
                setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
        btnRegister.setOnClickListener { navController.navigateWithAnimation(R.id.loginFragmentToRegisterFragment) }
    }
}