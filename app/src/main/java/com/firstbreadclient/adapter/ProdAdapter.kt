package com.firstbreadclient.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.firstbreadclient.databinding.ProdRowBinding
import com.firstbreadclient.model.data.Prod
import com.firstbreadclient.room.FirstViewModel

class ProdAdapter(private val clickListener: ProdListener, private val viewModel: FirstViewModel) : ListAdapter<Prod, ProdAdapter.ProdViewHolder>(ProdDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdViewHolder {
        return ProdViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ProdViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener, viewModel)
    }

    class ProdViewHolder private constructor(private val binding: ProdRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Prod, clickListener: ProdListener, viewModel: FirstViewModel) {
            binding.prod = item
            binding.clickListener = clickListener
            binding.executePendingBindings()

            binding.buttonAdd.setOnClickListener{
                val amountstr = (item.amountstr.toInt() + 12).toString()
                Log.d("buttonAddSub", "onClickAdd $amountstr")
                Log.d("prodCardView", "onClick " + item.daysordermovecontentid)
                val prod = Prod(item.daysordermovecontentid, item.daysordermoveidstr, item.prodlongname, amountstr, item.flagacceptstr)
                viewModel.updateProd(prod)
            }

            binding.buttonSub.setOnClickListener{
                val amountstr = (item.amountstr.toInt() - 12).toString()
                Log.d("buttonAddSub", "onClickSub $amountstr")
                Log.d("prodCardView", "onClick " + item.daysordermovecontentid)
                val prod = Prod(item.daysordermovecontentid, item.daysordermoveidstr, item.prodlongname, amountstr, item.flagacceptstr)
                viewModel.updateProd(prod)
            }

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