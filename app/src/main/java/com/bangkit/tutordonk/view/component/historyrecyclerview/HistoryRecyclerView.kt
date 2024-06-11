package com.bangkit.tutordonk.view.component.historyrecyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.tutordonk.databinding.CustomRecyclerviewBinding
import com.bangkit.tutordonk.databinding.ItemlistHistoryBinding
import com.bangkit.tutordonk.view.base.BaseRecyclerViewAdapter
import com.bangkit.tutordonk.view.base.GenericDiffCallback
import com.bangkit.tutordonk.view.component.historyrecyclerview.model.HistoryItem

typealias OnItemClickListener = (HistoryItem) -> Unit

class HistoryRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: CustomRecyclerviewBinding =
        CustomRecyclerviewBinding.inflate(LayoutInflater.from(context), this, true)
    private lateinit var historyAdapter: BaseRecyclerViewAdapter<HistoryItem, ItemlistHistoryBinding>

    private var isLoading = false
    private var currentPage = 0
    private var maxPage = 5
    private var itemsPerPage = 10
    private val allItems = mutableListOf<HistoryItem>()

    private var onItemClickListener: OnItemClickListener = {}

    init {
        setupRecyclerView()
        setupPagination()
    }

    private fun setupRecyclerView() {
        historyAdapter = BaseRecyclerViewAdapter(
            itemClickListener = { item ->
                onItemClickListener.invoke(item)
            },
            inflateBinding = ItemlistHistoryBinding::inflate,
            bind = { binding, item ->
                with(binding) {
                    tvStatus.text = item.status
                    tvTitle.text = item.major
                    tvSubtitle.text = item.name
                    tvTime.text = item.date
                }
            },
            diffCallback = GenericDiffCallback(
                areTheItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
                areTheContentsTheSame = { oldItem, newItem -> oldItem == newItem }
            )
        )
        binding.rvMain.adapter = historyAdapter
    }

    private fun setupPagination() {
        binding.rvMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (recyclerView.canScrollVertically(1).not() && isLoading.not() && currentPage < maxPage) {
                    loadMoreItems()
                }
            }
        })
    }

    private fun loadMoreItems() {
        isLoading = true
        currentPage++
        val newItems = fetchData(currentPage)
        allItems.addAll(newItems)
        historyAdapter.submitList(allItems.toList())
        isLoading = false
    }

    fun setMaxPage(maxPage: Int) {
        this.maxPage = maxPage
    }

    fun setItemsPerPage(itemsPerPage: Int) {
        this.itemsPerPage = itemsPerPage
    }

    fun setInitialItems(items: List<HistoryItem>) {
        allItems.clear()
        allItems.addAll(items)
        historyAdapter.submitList(allItems.toList())
    }

    fun setOnItemClickListener(callback: OnItemClickListener) {
        onItemClickListener = callback
        historyAdapter.setOnItemClickListener(callback)
    }

    fun getAllItems() = historyAdapter.currentList.toMutableList()

    private fun fetchData(page: Int): List<HistoryItem> {
        return List(itemsPerPage) {
            HistoryItem(
                id = (page - 1) * itemsPerPage + it,
                status = "SUCCESS",
                name = "Prof. Hamka",
                major = "Kalkulus",
                subMajor = "Sub Kalkulus",
                date = "$it-04-2024",
            )
        }
    }

}