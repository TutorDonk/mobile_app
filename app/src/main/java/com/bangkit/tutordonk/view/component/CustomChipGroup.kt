package com.bangkit.tutordonk.view.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bangkit.tutordonk.R
import com.bangkit.tutordonk.databinding.CustomChipGroupBinding
import com.google.android.material.chip.Chip

class CustomChipGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var binding: CustomChipGroupBinding
    private var onChipSelectedListener: ((List<String>) -> Unit)? = null

    init {
        orientation = VERTICAL
        binding = CustomChipGroupBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setOnChipSelectedListener(listener: (List<String>) -> Unit) {
        onChipSelectedListener = listener
    }

    fun setChips(labels: List<String>, checkedIndices: List<Int> = emptyList()) {
        binding.chipGroup.removeAllViews()
        labels.forEachIndexed { index, label ->
            addChip(label, index in checkedIndices)
        }
    }

    private fun addChip(label: String, isChecked: Boolean) {
        val chip = Chip(context).apply {
            text = label
            isCheckable = true
            this.isChecked = isChecked
            setChipBackgroundColorResource(R.color.soft_yellow)
            setTextColor(resources.getColor(R.color.black, null))
            setOnCheckedChangeListener { _, _ ->
                onChipSelectedListener?.invoke(getCheckedChips())
            }
        }
        binding.chipGroup.addView(chip)
    }

    private fun getCheckedChips(): List<String> {
        val checkedChips = mutableListOf<String>()
        for (i in 0 until binding.chipGroup.childCount) {
            val chip = binding.chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                checkedChips.add(chip.text.toString())
            }
        }
        return checkedChips
    }
}
