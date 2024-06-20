package com.bangkit.tutordonk.component.historyrecyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bangkit.tutordonk.component.base.BaseRecyclerViewAdapter
import com.bangkit.tutordonk.component.base.GenericDiffCallback
import com.bangkit.tutordonk.databinding.CustomRecyclerviewBinding
import com.bangkit.tutordonk.databinding.ItemlistHistoryBinding
import com.bangkit.tutordonk.model.ListBookingItem
import com.bangkit.tutordonk.utils.toFormattedDateString

typealias OnItemClickListener = (ListBookingItem) -> Unit

class HistoryRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: CustomRecyclerviewBinding =
        CustomRecyclerviewBinding.inflate(LayoutInflater.from(context), this, true)
    private lateinit var historyAdapter: BaseRecyclerViewAdapter<ListBookingItem, ItemlistHistoryBinding>

    private val allItems = mutableListOf<ListBookingItem>()
    private var onItemClickListener: OnItemClickListener = {}

    init {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        historyAdapter = BaseRecyclerViewAdapter(
            itemClickListener = { item ->
                onItemClickListener.invoke(item)
            },
            inflateBinding = ItemlistHistoryBinding::inflate,
            bind = { binding, item ->
                with(binding) {
                    tvStatus.text = if (item.status == 0) "SUCCESS" else "FAILED"
                    tvTitle.text = item.course
                    tvSubtitle.text = item.namaTutor
                    tvTime.text = item.createdAt._seconds.toFormattedDateString("dd/mm/yyyy HH:mm:ss")
                }
            },
            diffCallback = GenericDiffCallback(
                areTheItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
                areTheContentsTheSame = { oldItem, newItem -> oldItem == newItem }
            )
        )
        binding.rvMain.adapter = historyAdapter
    }

    fun setInitialItems(items: List<ListBookingItem>) {
        allItems.clear()
        allItems.addAll(items)
        historyAdapter.submitList(allItems.toList())
    }

    fun setOnItemClickListener(callback: OnItemClickListener) {
        onItemClickListener = callback
        historyAdapter.setOnItemClickListener(callback)
    }

    fun getAllItems() = historyAdapter.currentList.toList()

}