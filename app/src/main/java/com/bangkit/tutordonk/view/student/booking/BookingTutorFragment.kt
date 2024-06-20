package com.bangkit.tutordonk.view.student.booking

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.component.base.BaseCustomDialog
import com.bangkit.tutordonk.databinding.FragmentStudentBookingTutorBinding
import com.bangkit.tutordonk.databinding.PopupRatingBinding
import com.bangkit.tutordonk.databinding.PopupReportBinding
import com.bangkit.tutordonk.model.ListBookingItem
import com.bangkit.tutordonk.model.TeacherListItem
import com.bangkit.tutordonk.network.ApiServiceProvider
import com.bangkit.tutordonk.utils.navigateWithAnimation
import com.bangkit.tutordonk.utils.setReadOnly
import com.bangkit.tutordonk.utils.toFormattedDateString
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BookingTutorFragment : Fragment() {
    private var _binding: FragmentStudentBookingTutorBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var dialogRating: BaseCustomDialog<PopupRatingBinding>
    private lateinit var dialogReport: BaseCustomDialog<PopupReportBinding>
    private lateinit var historyItem: ListBookingItem

    private val apiServiceProvider: ApiServiceProvider by inject()
    private var tutorList: List<TeacherListItem> = emptyList()
    private var selectedStudy = ""
    private var selectedTutor = ""
    private var selectedDateTime = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentBookingTutorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setLayoutListener()
        if (arguments?.getBoolean(ARG_FROM_HISTORY, false) == true) {
            setDialogListener()
            checkArgumentData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        val callback = apiServiceProvider.createCallback<List<String>>(
            onSuccess = { response -> binding.spinnerStudy.setDefaultEntries(response) }
        )

        apiServiceProvider.apiService.listStudy().enqueue(callback)
    }

    private fun setLayoutListener() = with(binding) {
        spinnerStudy.setOnItemSelectedListener { study ->
            selectedStudy = study
            changeItemSubStudy()
            getTeacherList()
        }
        spinnerSelectTutor.setOnItemSelectedListener {
            selectedTutor = it
        }
        mcbSelectSubStudy.setOnCheckedChangeListener { _, isChecked ->
            changeItemSubStudy()
            spinnerSubStudy.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        tilDate.setEndIconOnClickListener { showDatePickerDialog() }
        tietDate.addTextChangedListener(formatDateWatcher())

        btnRating.setOnClickListener { dialogRating.show() }
        btnReport.setOnClickListener { dialogReport.show() }
        btnBooking.setOnClickListener { goToTutorHome() }
    }

    private fun checkArgumentData() {
        historyItem = Gson().fromJson(arguments?.getString(ARG_HISTORY_ITEM), ListBookingItem::class.java)
        with(binding) {
            groupRateTutor.visibility = View.VISIBLE
            btnBooking.visibility = View.GONE
            mcbSelectSubStudy.isChecked = true

            spinnerStudy.setDefaultEntries(listOf(historyItem.course))
            spinnerStudy.setReadOnly()
            spinnerSelectTutor.setDefaultEntries(listOf(historyItem.namaTutor))
            spinnerSelectTutor.setReadOnly()
            tietDate.setText(historyItem.createdAt._seconds.toFormattedDateString("MM/dd/yy"))
            tietDate.setReadOnly()
        }
    }

    private fun setDialogListener() {
        dialogRating = BaseCustomDialog(
            context = requireContext(),
            bindingInflater = PopupRatingBinding::inflate,
            bind = { binding ->
                with(binding) {
                    tvName.text = historyItem.namaTutor
                    ivStars1.setOnClickListener { setRating(binding, 1) }
                    ivStars2.setOnClickListener { setRating(binding, 2) }
                    ivStars3.setOnClickListener { setRating(binding, 3) }
                    ivStars4.setOnClickListener { setRating(binding, 4) }
                    ivStars5.setOnClickListener { setRating(binding, 5) }
                    ivClose.setOnClickListener { dialogRating.cancel() }
                    btnSend.setOnClickListener { dialogRating.cancel() }
                }
            })

        dialogReport = BaseCustomDialog(
            context = requireContext(),
            bindingInflater = PopupReportBinding::inflate,
            bind = { binding ->
                with(binding) {
                    tvName.text = historyItem.namaTutor
                    ivClose.setOnClickListener { dialogReport.cancel() }
                    btnSend.setOnClickListener { dialogReport.cancel() }
                }
            })
    }

    private fun setRating(binding: PopupRatingBinding, rating: Int) {
        val filledStar = R.drawable.ic_stars_filled
        val emptyStar = R.drawable.ic_stars

        with(binding) {
            ivStars1.setImageResource(if (rating >= 1) filledStar else emptyStar)
            ivStars2.setImageResource(if (rating >= 2) filledStar else emptyStar)
            ivStars3.setImageResource(if (rating >= 3) filledStar else emptyStar)
            ivStars4.setImageResource(if (rating >= 4) filledStar else emptyStar)
            ivStars5.setImageResource(if (rating >= 5) filledStar else emptyStar)
        }
    }

    private fun changeItemSubStudy() = with(binding) {
        spinnerSubStudy.setEntries(
            when (selectedStudy) {
                B_ENG -> R.array.list_sub_study_b_english_array
                ALGORITHM -> R.array.list_sub_study_algorithm_array
                else -> R.array.list_sub_study_kalkulus_array
            }
        )
    }

    private fun goToTutorHome() {
        val getSelectedTutor = tutorList.find { it.nama == selectedTutor }.also {
            it?.selectedSubjects = selectedStudy
            it?.selectedDateTime = selectedDateTime
        }
        val bundle = bundleOf(
            BrowseTutorFragment.ARG_TUTOR_DATA to Gson().toJson(getSelectedTutor)
        )

        navController.navigateWithAnimation(R.id.tutorHomeFragment, args = bundle)
    }

    private fun formatDateWatcher(): TextWatcher {
        return object : TextWatcher {
            private var isUpdating = false
            private var oldText = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                oldText = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                /* no-op */
            }

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return

                isUpdating = true

                val input = s.toString().replace("/", "")
                val length = input.length
                val formatted = StringBuilder()

                for (i in input.indices) {
                    formatted.append(input[i])
                    if ((i == 1 || i == 3) && i < length - 1) {
                        formatted.append("/")
                    }
                }

                var cursorPosition = binding.tietDate.selectionStart
                val formattedLength = formatted.length

                if (oldText.length > input.length) {
                    if (cursorPosition > 0 && oldText.getOrNull(cursorPosition - 1) == '/') {
                        cursorPosition--
                    }
                } else {
                    if (cursorPosition in 2..formattedLength && formatted.getOrNull(cursorPosition - 1) == '/') {
                        cursorPosition++
                    }
                }

                binding.tietDate.setText(formatted.toString())
                binding.tietDate.setSelection(cursorPosition.coerceAtMost(formattedLength))

                isUpdating = false
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth)
                set(Calendar.DAY_OF_MONTH, selectedDay)
            }
            val dateFormat = SimpleDateFormat("MM/dd/yy", Locale.US)
            binding.tietDate.setText(dateFormat.format(selectedDate.time))
            showTimePicker()
        }, year, month, day)

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select Time")
            .build()

        picker.addOnPositiveButtonClickListener {
            val hour = picker.hour
            val minute = picker.minute
            val selectedTime = String.format(Locale.US, "%02d:%02d", hour, minute)
            selectedDateTime = buildString {
                append(selectedTime)
                append(" ")
                append(binding.tietDate.text.toString().formateDate())
            }
        }

        picker.show(parentFragmentManager, "TAG_TIME_PICKER")
    }

    private fun getTeacherList() {
        val callback = apiServiceProvider.createCallback<List<TeacherListItem>>(
            onSuccess = { response ->
                val allTutor = response.map { it.nama }
                tutorList = response
                binding.spinnerSelectTutor.setDefaultEntries(allTutor)
            }
        )

        apiServiceProvider.apiService.listTeacher(selectedStudy).enqueue(callback)
    }

    private fun String.formateDate(): String {
        val inputFormat = SimpleDateFormat("MM/dd/yy", Locale.US)
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)

        val date: Date = inputFormat.parse(this) ?: Date()
        return outputFormat.format(date)
    }

    companion object {
        const val B_ENG = "B Inggris"
        const val ALGORITHM = "Algoritma Dasar"
        const val ARG_HISTORY_ITEM = "HISTORY_ITEM"
        const val ARG_FROM_HISTORY = "FROM_HISTORY"
    }
}