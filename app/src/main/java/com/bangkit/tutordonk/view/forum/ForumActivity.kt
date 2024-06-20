package com.bangkit.tutordonk.view.forum

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.databinding.ActivityForumBinding
import com.bangkit.tutordonk.model.ShareViewModel
import com.bangkit.tutordonk.utils.SharedPreferencesHelper
import com.bangkit.tutordonk.utils.navigateWithAnimation
import org.koin.android.ext.android.inject

class ForumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForumBinding
    private lateinit var navController: NavController

    private val sharedPreferences: SharedPreferencesHelper by inject()

    val data: ShareViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fcv_forum) as NavHostFragment
        navController = navHostFragment.navController

        if (intent?.hasExtra(INTENT_FORUM_ITEM) == true) checkIntent()
        setupUI()
    }

    private fun checkIntent() {
        val bundle = bundleOf(INTENT_FORUM_ITEM to intent?.getStringExtra(INTENT_FORUM_ITEM))
        navController.navigateWithAnimation(R.id.forumDetailFragment, true, R.id.forumListFragment, args = bundle)
    }

    private fun setupUI() {
        with(binding) {
            tvGreeting.text = getString(R.string.dashboard_greeting, sharedPreferences.getName())
            tvGreeting.setOnClickListener {
                if (sharedPreferences.getRole() == "siswa") {
                    navController.setGraph(R.navigation.student_nav_graph)
                    navController.navigateWithAnimation(R.id.editProfileFragment)
                } else {
                    navController.setGraph(R.navigation.teacher_nav_graph)
                    navController.navigateWithAnimation(R.id.teacherEditProfileFragment)
                }
            }
        }
    }

    companion object {
        const val INTENT_FORUM_ITEM = "FORUM_ITEM"
    }
}