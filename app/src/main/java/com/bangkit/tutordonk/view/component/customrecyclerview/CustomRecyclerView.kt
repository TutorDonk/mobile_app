package com.bangkit.tutordonk.view.component.customrecyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.tutordonk.databinding.CustomRecyclerviewBinding
import com.bangkit.tutordonk.databinding.ItemlistForumBinding
import com.bangkit.tutordonk.view.base.BaseRecyclerViewAdapter
import com.bangkit.tutordonk.view.base.GenericDiffCallback
import com.bangkit.tutordonk.view.component.customrecyclerview.model.ForumItem

typealias OnItemClickListener = (ForumItem) -> Unit

class CustomRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: CustomRecyclerviewBinding =
        CustomRecyclerviewBinding.inflate(LayoutInflater.from(context), this, true)
    private lateinit var customRecyclerViewAdapter: BaseRecyclerViewAdapter<ForumItem, ItemlistForumBinding>

    private var isLoading = false
    private var currentPage = 0
    private var maxPage = 5
    private var itemsPerPage = 10
    private val allItems = mutableListOf<ForumItem>()

    private var onItemClickListener: OnItemClickListener = {}

    init {
        setupRecyclerView()
        setupPagination()
    }

    private fun setupRecyclerView() {
        customRecyclerViewAdapter = BaseRecyclerViewAdapter(
            itemClickListener = { item ->
                onItemClickListener.invoke(item)
            },
            inflateBinding = ItemlistForumBinding::inflate,
            bind = { binding, item ->
                with(binding) {
                    tvTitle.text = item.title
                    tvSubtitle.text = item.subtitle
                    tvLike.text = buildString {
                        append("Likes : ")
                        append(item.like)
                    }
                    tvComments.text = buildString {
                        append("Comments")
                        append(item.comment)
                    }
                }
            },
            diffCallback = GenericDiffCallback(
                areTheItemsTheSame = { oldItem, newItem -> oldItem.id == newItem.id },
                areTheContentsTheSame = { oldItem, newItem -> oldItem == newItem }
            )
        )
        binding.rvMain.adapter = customRecyclerViewAdapter
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
        customRecyclerViewAdapter.submitList(allItems.toList())
        isLoading = false
    }

    fun setMaxPage(maxPage: Int) {
        this.maxPage = maxPage
    }

    fun setItemsPerPage(itemsPerPage: Int) {
        this.itemsPerPage = itemsPerPage
    }

    fun setInitialItems(items: List<ForumItem>) {
        allItems.clear()
        allItems.addAll(items)
        customRecyclerViewAdapter.submitList(allItems.toList())
    }

    fun setOnItemClickListener(callback: OnItemClickListener) {
        onItemClickListener = callback
        customRecyclerViewAdapter.setOnItemClickListener(callback)
    }

    fun getAllItems() = customRecyclerViewAdapter.currentList.toMutableList()

    private fun fetchData(page: Int): List<ForumItem> {
        return List(itemsPerPage) {
            ForumItem(
                id = (page - 1) * itemsPerPage + it,
                user = "User $it",
                title = "Title ${(page - 1) * itemsPerPage + it}",
                subtitle = "Subtitle ${(page - 1) * itemsPerPage + it}",
                like = (page - 1) * itemsPerPage + it,
                comment = (page - 1) * itemsPerPage + it,
                popularity = (page - 1) * itemsPerPage + it
            )
        }
    }
}
