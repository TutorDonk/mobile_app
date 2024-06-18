package com.bangkit.tutordonk.view.teacher.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bangkit.tutordonk.component.base.BaseCustomDialog
import com.bangkit.tutordonk.databinding.FragmentTeacherEditProfileBinding
import com.bangkit.tutordonk.databinding.PopupAddCertificateBinding
import com.bangkit.tutordonk.model.CertificationData
import com.bangkit.tutordonk.model.TeacherProfileResponse
import com.bangkit.tutordonk.network.ApiServiceProvider
import com.bangkit.tutordonk.utils.SharedPreferencesHelper
import com.bangkit.tutordonk.utils.isTextMatchingKeywords
import com.bangkit.tutordonk.utils.setReadOnly
import org.koin.android.ext.android.inject

class TeacherEditProfileFragment : Fragment() {
    private var _binding: FragmentTeacherEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var dialogCertificate: BaseCustomDialog<PopupAddCertificateBinding>

    private val apiServiceProvider: ApiServiceProvider by inject()
    private val sharedPreferences: SharedPreferencesHelper by inject()

    private val certificateList: MutableList<CertificationData> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherEditProfileBinding.inflate(inflater, container, false)
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
                val cleanText = link.trim()
                certificateList.add(
                    certificateList.size, CertificationData(
                        cleanText.substringBefore(":"), cleanText.substringAfter(":")
                    )
                )
                binding.tietAchievements.setText(
                    if (certificateList.size > 2) certificateList.joinToString(
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

        tietAchievements.setOnClickListener {
            if (certificateList.isEmpty())
                certificateList.add(
                    0, CertificationData("", binding.tietAchievements.text.toString())
                )
            dialogCertificate.show()
        }
        btnUpdate.setOnClickListener { doUpdateData() }
    }

    private fun doUpdateData() {
        with(binding) {
            val reqBody = TeacherProfileResponse(
                tietName.text.toString(),
                tietEmail.text.toString(),
                tietPhoneNumber.text.toString(),
                spinnerGender.value,
                tietEducationLevel.text.toString().toIntOrNull() ?: 0,
                customChip.value,
                tietAddress.text.toString(),
                certificateList
            )

            val callback = apiServiceProvider.createCallback<TeacherProfileResponse> { response ->
                sharedPreferences.saveUsername(response.nama)
                navController.popBackStack()
            }

            apiServiceProvider.apiService.teacherUpdateProfile(reqBody).enqueue(callback)
        }
    }
}