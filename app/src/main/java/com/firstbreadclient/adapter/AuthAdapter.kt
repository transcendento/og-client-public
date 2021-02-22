package com.firstbreadclient.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firstbreadclient.R
import com.firstbreadclient.adapter.AuthAdapter.AuthViewHolder
import com.firstbreadclient.model.data.Auth

class AuthAdapter(context: Context?) : RecyclerView.Adapter<AuthViewHolder>() {
    private var clickListener: ItemClickListener? = null
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var mAuths: List<Auth> = emptyList() // Cached copy of words
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthViewHolder {
        val view = mLayoutInflater.inflate(R.layout.auth_row, parent, false)
        return AuthViewHolder(view)
    }

    override fun onBindViewHolder(holder: AuthViewHolder, position: Int) {
        val current = mAuths[position]
        holder.cntKodText.text = current.cntkod
        holder.cntNameText.text = current.cntname
        holder.cntAdresText.text = current.cntadres
    }

    fun setAuths(auths: List<Auth>) {
        mAuths = auths
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mAuths.size
    }

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

}