package com.firstbreadclient.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.firstbreadclient.databinding.AuthRowBinding
import com.firstbreadclient.model.data.Auth

class AuthAdapter(private val clickListener: AuthListener) : ListAdapter<Auth, AuthAdapter.AuthViewHolder>(AuthDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthViewHolder {
        return AuthViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AuthViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class AuthViewHolder private constructor(private val binding: AuthRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Auth, clickListener: AuthListener) {
            binding.auth = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AuthViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AuthRowBinding.inflate(layoutInflater, parent, false)
                return AuthViewHolder(binding)
            }
        }
    }
}

class AuthDiffCallback : DiffUtil.ItemCallback<Auth>() {
    override fun areItemsTheSame(oldItem: Auth, newItem: Auth): Boolean {
        return oldItem.cntid == newItem.cntid
    }

    override fun areContentsTheSame(oldItem: Auth, newItem: Auth): Boolean {
        return oldItem == newItem
    }
}

class AuthListener(val clickListener: (auth: Auth) -> Unit) {
    fun onClickAuth(auth: Auth) = clickListener(auth)
}
