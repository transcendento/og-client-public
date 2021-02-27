package com.firstbreadclient.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.firstbreadclient.R
import com.firstbreadclient.adapter.AuthAdapter.AuthViewHolder
import com.firstbreadclient.databinding.AuthRowBinding
import com.firstbreadclient.model.data.Auth

class AuthAdapter(val clickListener: AuthListener) : ListAdapter<Auth, AuthAdapter.AuthViewHolder>(AuthDiffCallback()) {
    private var clickListener: ItemClickListener? = null
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var mAuths: List<Auth> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthViewHolder {
        return AuthViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AuthViewHolder, position: Int) {
        val current = mAuths[position]
        holder.cntKodText.text = current.cntkod
        holder.cntNameText.text = current.cntname
        holder.cntAdresText.text = current.cntadres
        holder.bind(getItem(position)!!, clickListener)
    }

    fun setAuths(auths: List<Auth>) {
        mAuths = auths
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mAuths.size
    }

/*
    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }

    inner class AuthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var cntKodText: TextView = itemView.findViewById(R.id.TextViewAuthKod)
        var cntNameText: TextView = itemView.findViewById(R.id.TextViewAuthName)
        var cntAdresText: TextView = itemView.findViewById(R.id.TextViewAuthAdres)
        override fun onClick(view: View) {
            if (clickListener != null) clickListener!!.onClick(view, adapterPosition)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }
*/

    class AuthViewHolder private constructor(val binding: AuthRowBinding) : RecyclerView.ViewHolder(binding.root){
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
    fun onClick(auth: Auth) = clickListener(auth)
}
