package com.firstbreadclient.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firstbreadclient.R
import com.firstbreadclient.adapter.OrderAdapter.OrderViewHolder
import com.firstbreadclient.model.data.Order

class OrderAdapter(context: Context?) : RecyclerView.Adapter<OrderViewHolder>() {
    private var clickListener: ItemClickListener? = null
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var mOrders: List<Order> = emptyList() // Cached copy of words

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = mLayoutInflater.inflate(R.layout.order_row, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val current = mOrders[position]
        //holder.cntKodText.setText(current.getCntkod());
        //holder.cntNameStrText.setText(current.getCntnamestr());
        holder.daysOrderDateText.text = current.daysorderdate
    }

    fun setOrders(orders: List<Order>) {
        mOrders = orders
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mOrders.size
    }

    fun setClickListener(itemClickListener: ItemClickListener?) {
        clickListener = itemClickListener
    }

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var cntKodText: TextView? = null
        var cntNameStrText: TextView? = null
        var daysOrderDateText: TextView = itemView.findViewById(R.id.text_daysorderdate)
        override fun onClick(view: View) {
            if (clickListener != null) clickListener!!.onClick(view, adapterPosition)
        }

        init {
            //cntKodText = itemView.findViewById(R.id.text_cntkod);
            //cntNameStrText = itemView.findViewById(R.id.text_cntnamestr);
            itemView.setOnClickListener(this)
        }
    }

}