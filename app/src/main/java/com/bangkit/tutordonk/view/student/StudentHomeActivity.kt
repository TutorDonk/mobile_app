package com.bangkit.tutordonk.view.student

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.databinding.ActivityStudentHomeBinding
import com.bangkit.tutordonk.view.navigateWithAnimation

class StudentHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentHomeBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fcv_home_user) as NavHostFragment
        navController = navHostFragment.navController

        setupUI()
    }

    private fun setupUI() {
        with(binding) {
            tvGreeting.text = getString(R.string.dashboard_greeting, intent.getStringExtra(NAME))
            tvGreeting.setOnClickListener {
                navController.navigateWithAnimation(R.id.editProfileFragment)
            }
        }
    }

    companion object {
        const val NAME = "NAME"
    }
}