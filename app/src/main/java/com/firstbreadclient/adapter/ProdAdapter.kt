package com.firstbreadclient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.firstbreadclient.databinding.ProdRowBinding
import com.firstbreadclient.model.data.Prod

class ProdAdapter(private val clickListener: ProdListener) : ListAdapter<Prod, ProdAdapter.ProdViewHolder>(ProdDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdViewHolder {
        return ProdViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ProdViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class ProdViewHolder private constructor(private val binding: ProdRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Prod, clickListener: ProdListener) {
            binding.prod = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ProdViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProdRowBinding.inflate(layoutInflater, parent, false)
                return ProdViewHolder(binding)
            }
        }
    }
}

class ProdDiffCallback : DiffUtil.ItemCallback<Prod>() {
    override fun areItemsTheSame(oldItem: Prod, newItem: Prod): Boolean {
        return oldItem.daysordermovecontentid == newItem.daysordermovecontentid
    }

    override fun areContentsTheSame(oldItem: Prod, newItem: Prod): Boolean {
        return oldItem == newItem
    }
}

class ProdListener(val clickListener: (prod: Prod) -> Unit) {
    fun onClickProd(prod: Prod) = clickListener(prod)
}