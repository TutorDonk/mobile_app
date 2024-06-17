package com.bangkit.tutordonk.view.onboarding.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.bangkit.tutordonk.databinding.FragmentRegisterBinding
import com.bangkit.tutordonk.databinding.PopupAddCertificateBinding
import com.bangkit.tutordonk.view.base.BaseCustomDialog
import com.bangkit.tutordonk.view.setReadOnly
import com.bangkit.tutordonk.view.student.StudentHomeActivity
import com.bangkit.tutordonk.view.teacher.TeacherActivity
import java.text.NumberFormat
import java.util.Locale

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialogCertificate: BaseCustomDialog<PopupAddCertificateBinding>

    private var isSiswa = false
    private val listOfCertificate: MutableList<String> = mutableListOf()

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
        setDialogAddCertificate()
    }

    private fun setDialogAddCertificate() {
        dialogCertificate = BaseCustomDialog(
            context = requireContext(),
            bindingInflater = PopupAddCertificateBinding::inflate,
            bind = { binding ->
                with(binding) {
                    tietCertificate.doOnTextChanged { text, _, _, _ ->
                        val isMatch = text.toString().isTextMatchingKeywords()
                        if (isMatch) tietCertificate.error = null
                        else tietCertificate.error = "Link yang dimasukkan salah !"
                    }
                    ivClose.setOnClickListener { dialogCertificate.cancel() }
                    btnAdd.setOnClickListener {
                        val url = tietCertificate.text.toString()
                        val returnValue = buildString {
                            append(tietName.text.toString().plus(" : "))
                            append(tietCertificate.text.toString())
                        }
                        if (url.isTextMatchingKeywords()) {
                            dialogCertificate.onAddButtonClick?.invoke(returnValue)
                            dialogCertificate.cancel()
                        }
                    }
                }
            },
            onAddButtonClick = { link ->
                listOfCertificate.add(link)
                binding.tietAchievements.setText(
                    if (listOfCertificate.size > 2) listOfCertificate.joinToString(
                        separator = ",\n"
                    ).replaceFirst(",\n", "") else link
                )
            },
            onDismiss = { bindingDialog ->
                with(bindingDialog) {
                    tietCertificate.text?.clear()
                    tietCertificate.error = null
                    tietCertificate.clearFocus()
                    tietName.text?.clear()
                    tietName.clearFocus()
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setLayoutListener() = with(binding) {
        spinnerRole.setOnItemSelectedListener {
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
                customChip.setOnChipSelectedListener { data ->
                    tietSubjects.setReadOnly()
                    tietSubjects.setText(data.joinToString(separator = ",\n"))
                }
            }
        }

        tietAchievements.setOnClickListener {
            if (listOfCertificate.isEmpty()) listOfCertificate.add(binding.tietAchievements.text.toString())
            dialogCertificate.show()
        }

        tietPrice.addTextChangedListener(moneyTextWatcher())

        btnRegister.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    if (isSiswa) StudentHomeActivity::class.java else TeacherActivity::class.java
                ).apply {
                    this.putExtra(StudentHomeActivity.NAME, tietName.text.toString())
                    setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                })
        }
    }

    private fun moneyTextWatcher(): TextWatcher {
        return object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {/* no-op */
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {/* no-op */
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    binding.tietPrice.removeTextChangedListener(this)

                    val locale = Locale("in", "ID")
                    val numberFormat = NumberFormat.getCurrencyInstance(locale).apply {
                        maximumFractionDigits = 0
                        isGroupingUsed = true
                    }

                    try {
                        val cleanString = s.toString().replace("[Rp,.]".toRegex(), "")
                        if (cleanString.isNotEmpty()) {
                            val parsed = cleanString.toDouble()
                            val formatted = numberFormat.format(parsed)

                            current = formatted
                            binding.tietPrice.setText(formatted)
                            binding.tietPrice.setSelection(formatted.length)
                        } else {
                            current = ""
                            binding.tietPrice.text?.clear()
                            binding.tietPrice.setSelection(0)
                        }
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }

                    binding.tietPrice.addTextChangedListener(this)
                }
            }
        }
    }

    private fun String.isTextMatchingKeywords(keywords: String = "https://drive.google.com"): Boolean {
        val keywordList = this.split(",").map { it.trim() }
        return keywordList.all { keyword -> keyword.contains(keywords, ignoreCase = true) }
    }
}