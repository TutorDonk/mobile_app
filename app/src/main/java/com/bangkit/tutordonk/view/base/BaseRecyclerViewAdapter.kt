package com.bangkit.tutordonk.view.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseRecyclerViewAdapter<T, VB : ViewBinding>(
    private var itemClickListener: (T) -> Unit,
    private val inflateBinding: (LayoutInflater, ViewGroup, Boolean) -> VB,
    private val bind: (VB, T) -> Unit,
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BaseRecyclerViewAdapter<T, VB>.GenericViewHolder>(diffCallback) {

    fun setOnItemClickListener(callback: (T) -> Unit) {
        itemClickListener = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        val binding = inflateBinding(LayoutInflater.from(parent.context), parent, false)
        return GenericViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { itemClickListener.invoke(getItem(position)) }
    }

    inner class GenericViewHolder(private val binding: VB) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            bind(binding, item)
        }
    }
}

class GenericDiffCallback<T : Any>(
    private val areTheItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    private val areTheContentsTheSame: (oldItem: T, newItem: T) -> Boolean
) : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = areTheItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = areTheContentsTheSame(oldItem, newItem)
}