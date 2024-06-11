package com.bangkit.tutordonk.view.student.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.databinding.FragmentStudentBookingTutorBinding
import com.bangkit.tutordonk.databinding.PopupRatingBinding
import com.bangkit.tutordonk.databinding.PopupReportBinding
import com.bangkit.tutordonk.view.base.BaseCustomDialog
import com.bangkit.tutordonk.view.component.historyrecyclerview.model.HistoryItem
import com.bangkit.tutordonk.view.setReadOnly
import com.google.gson.Gson

class BookingTutorFragment : Fragment() {
    private var _binding: FragmentStudentBookingTutorBinding? = null
    private val binding get() = _binding!!
    private var selectedStudy = ""

    private lateinit var dialogRating: BaseCustomDialog<PopupRatingBinding>
    private lateinit var dialogReport: BaseCustomDialog<PopupReportBinding>
    private lateinit var historyItem: HistoryItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentBookingTutorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    private fun setLayoutListener() = with(binding) {
        spinnerStudy.setOnItemSelectedListener { study ->
            selectedStudy = study
            changeItemSubStudy()
        }
        mcbSelectSubStudy.setOnCheckedChangeListener { _, isChecked ->
            changeItemSubStudy()
            spinnerSubStudy.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        btnRating.setOnClickListener { dialogRating.show() }
        btnReport.setOnClickListener { dialogReport.show() }
    }

    private fun checkArgumentData() {
        historyItem = Gson().fromJson(arguments?.getString(ARG_HISTORY_ITEM), HistoryItem::class.java)
        with(binding) {
            groupRateTutor.visibility = View.VISIBLE
            btnBooking.visibility = View.GONE
            mcbSelectSubStudy.isChecked = true

            spinnerStudy.setDefaultEntries(listOf(historyItem.major))
            spinnerStudy.setReadOnly()
            spinnerSubStudy.setDefaultEntries(listOf(historyItem.subMajor))
            spinnerSubStudy.setReadOnly()
            spinnerSelectTutor.setDefaultEntries(listOf(historyItem.name))
            spinnerSelectTutor.setReadOnly()
        }
    }

    private fun setDialogListener() {
        dialogRating = BaseCustomDialog(
            context = requireContext(),
            bindingInflater = PopupRatingBinding::inflate,
            bind = { binding ->
                with(binding) {
                    tvName.text = historyItem.name
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
                    tvName.text = historyItem.name
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

    companion object {
        const val B_ENG = "B Inggris"
        const val ALGORITHM = "Algoritma Dasar"
        const val ARG_HISTORY_ITEM = "HISTORY_ITEM"
        const val ARG_FROM_HISTORY = "FROM_HISTORY"
    }
}