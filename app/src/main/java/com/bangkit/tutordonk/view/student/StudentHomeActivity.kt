package com.bangkit.tutordonk.view.student

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.databinding.ActivityStudentHomeBinding
import com.bangkit.tutordonk.model.ShareViewModel
import com.bangkit.tutordonk.utils.navigateWithAnimation

class StudentHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentHomeBinding

    private lateinit var navController: NavController

    val data: ShareViewModel by viewModels()

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
            data.name.observe(this@StudentHomeActivity) {
                tvGreeting.text = getString(R.string.dashboard_greeting, it)
            }

            tvGreeting.setOnClickListener {
                if (navController.currentDestination?.id != R.id.editProfileFragment) {
                    navController.navigateWithAnimation(R.id.editProfileFragment)
                }
            }
        }
    }
}