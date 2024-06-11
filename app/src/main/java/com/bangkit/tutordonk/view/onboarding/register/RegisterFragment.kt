package com.bangkit.tutordonk.view.onboarding.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.bangkit.tutordonk.databinding.FragmentRegisterBinding
import com.bangkit.tutordonk.view.student.StudentHomeActivity
import com.bangkit.tutordonk.view.teacher.TeacherActivity

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private var name = ""
    private var isSiswa = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayoutListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setLayoutListener() = with(binding) {
        spinnerRole.setOnItemSelectedListener {
            name = it
            isSiswa = it.lowercase() == "siswa"
            groupStudent.visibility = if (isSiswa) View.VISIBLE else View.GONE
            groupTeacher.visibility = if (isSiswa.not()) View.VISIBLE else View.GONE

            if (isSiswa.not()) {
                customChip.setChips(
                    listOf(
                        "Kalkulus",
                        "Algoritm",
                        "B Inggris",
                    )
                )
                customChip.setOnChipSelectedListener {/* no-op */ }
            }
        }

        tietAchievements.doOnTextChanged { text, _, _, _ ->
            val isMatch = text.toString().isTextMatchingKeywords()
            if (isMatch) tietAchievements.error = null
            else tietAchievements.error = "Link yang dimasukkan salah !"
        }

        btnRegister.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    if (isSiswa) StudentHomeActivity::class.java else TeacherActivity::class.java
                ).apply {
                    this.putExtra(StudentHomeActivity.NAME, name)
                    setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                })
        }
    }

    private fun String.isTextMatchingKeywords(keywords: String = "https://drive.google.com"): Boolean {
        val keywordList = this.split(",").map { it.trim() }
        return keywordList.all { keyword -> keyword.contains(keywords, ignoreCase = true) }
    }
}