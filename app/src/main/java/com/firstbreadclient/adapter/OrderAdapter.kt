package com.firstbreadclient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.firstbreadclient.databinding.OrderRowBinding
import com.firstbreadclient.model.data.Order

class OrderAdapter(private val clickListener: OrderListener) : ListAdapter<Order, OrderAdapter.OrderViewHolder>(OrderDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class OrderViewHolder private constructor(private val binding: OrderRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Order, clickListener: OrderListener) {
            binding.order = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): OrderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = OrderRowBinding.inflate(layoutInflater, parent, false)
                return OrderViewHolder(binding)
            }
        }
    }
}

class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem.daysordermoveid == newItem.daysordermoveid
    }

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem == newItem
    }
}

class OrderListener(val clickListener: (Order: Order) -> Unit) {
    fun onClickOrder(Order: Order) = clickListener(Order)
}
