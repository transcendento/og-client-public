package com.firstbreadclient.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firstbreadclient.R
import com.firstbreadclient.adapter.OrderAdapter
import com.firstbreadclient.adapter.OrderListener
import com.firstbreadclient.databinding.OrderFragmentBinding
import com.firstbreadclient.room.FirstApplication
import com.firstbreadclient.room.FirstViewModel
import com.firstbreadclient.room.FirstViewModelFactory

class OrderFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val bindingOrderFragment: OrderFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.order_fragment, container, false)

        val application = requireNotNull(this.activity).application

        val viewModelFactory = FirstViewModelFactory((application as FirstApplication).repository)

        val firstViewModel = ViewModelProvider(
                this, viewModelFactory).get(FirstViewModel::class.java)

        bindingOrderFragment.firstViewModel = firstViewModel

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.activity)
        bindingOrderFragment.recyclerViewOrder.layoutManager = layoutManager

        val orderAdapter = OrderAdapter(OrderListener { Order ->  firstViewModel.selectOrder(Order) })
        bindingOrderFragment.recyclerViewOrder.adapter = orderAdapter

        bindingOrderFragment.lifecycleOwner = this

        firstViewModel.allOrders.observe(viewLifecycleOwner, {
            it.let { orderAdapter.submitList(it) }
        })

        return bindingOrderFragment.root
    }
}