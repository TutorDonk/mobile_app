package com.bangkit.tutordonk.view.onboarding.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.component.base.BaseCustomDialog
import com.bangkit.tutordonk.databinding.FragmentRegisterBinding
import com.bangkit.tutordonk.databinding.PopupAddCertificateBinding
import com.bangkit.tutordonk.model.UserResponse
import com.bangkit.tutordonk.network.ApiServiceProvider
import com.bangkit.tutordonk.utils.isTextMatchingKeywords
import com.bangkit.tutordonk.utils.navigateWithAnimation
import com.bangkit.tutordonk.utils.setReadOnly
import org.koin.android.ext.android.inject
import java.text.NumberFormat
import java.util.Locale

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var dialogCertificate: BaseCustomDialog<PopupAddCertificateBinding>

    private var isSiswa = false
    private val listOfCertificate: MutableList<String> = mutableListOf()
    private val apiServiceProvider: ApiServiceProvider by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setLayoutListener()
        setDialogAddCertificate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun setLayoutListener() = with(binding) {
        spinnerRole.setOnItemSelectedListener {
            isSiswa = it.lowercase() == "siswa"
            groupStudent.visibility = if (isSiswa) View.VISIBLE else View.GONE
            groupTeacher.visibility = if (isSiswa.not()) View.VISIBLE else View.GONE

            if (isSiswa.not()) {
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

        btnRegister.setOnClickListener { doRegister() }
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

    private fun doRegister() {
        with(binding) {
            val reqBody = mapOf(
                "nama" to tietName.text.toString(),
                "email" to tietEmail.text.toString(),
                "password" to tietPassword.text.toString(),
                "role" to isSiswa.roleString()
            )

            val callback = apiServiceProvider.createCallback<UserResponse> { _ ->
                navController.navigateWithAnimation(R.id.loginFragment, true)
            }

            apiServiceProvider.apiService.authRegister(reqBody).enqueue(callback)
        }
    }
}

private fun Boolean.roleString() = if (this) "siswa" else "pengajar"