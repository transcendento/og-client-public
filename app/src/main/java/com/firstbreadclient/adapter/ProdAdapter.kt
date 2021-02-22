package com.firstbreadclient.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firstbreadclient.R
import com.firstbreadclient.adapter.ProdAdapter.ProdViewHolder
import com.firstbreadclient.model.data.Prod
import java.util.*

class ProdAdapter(context: Context?) : RecyclerView.Adapter<ProdViewHolder>(), Filterable {
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var mProds: List<Prod> = emptyList() // Cached copy of words
    private var mProdsFiltered: List<Prod> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdViewHolder {
        val view = mLayoutInflater.inflate(R.layout.prod_row, parent, false)
        return ProdViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdViewHolder, position: Int) {
        val current = mProdsFiltered[position]
        holder.prodLongNameText.text = current.prodlongname
        holder.amountStrText.text = current.amountstr
    }

    fun setProds(prods: List<Prod>) {
        mProds = prods
        mProdsFiltered = prods
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mProdsFiltered.size
    }

    inner class ProdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var prodLongNameText: TextView
        var amountStrText: TextView

        init {
            prodLongNameText = itemView.findViewById(R.id.text_prodlongname)
            amountStrText = itemView.findViewById(R.id.text_amountstr)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                mProdsFiltered = if (charString.isEmpty()) {
                    mProds
                } else {
                    val filteredList: MutableList<Prod> = ArrayList()
                    for (row in mProds) {
                        if (row.prodlongname.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }

                    filteredList

                }
                val filterResults = FilterResults()
                filterResults.values = mProdsFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                mProdsFiltered = filterResults.values as List<Prod>
                notifyDataSetChanged()
            }
        }
    }

}