package com.firstbreadclient.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.firstbreadclient.R
import com.firstbreadclient.adapter.OrderAdapter
import com.firstbreadclient.adapter.OrderListener
import com.firstbreadclient.databinding.OrderFragmentBinding
import com.firstbreadclient.room.FirstApplication
import com.firstbreadclient.room.FirstViewModel
import com.firstbreadclient.room.FirstViewModelFactory
import com.firstbreadclient.worker.GetDataWorker
import java.util.concurrent.TimeUnit

private const val POLL_WORK = "WORK"
class OrderFragment : VisibleFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingOrderFragment: OrderFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.order_fragment, container, false
        )
        val application = requireNotNull(this.activity).application

        val viewModelFactory = FirstViewModelFactory((application as FirstApplication).repository)

        val firstViewModel = ViewModelProvider(
            this, viewModelFactory
        )[FirstViewModel::class.java]

        bindingOrderFragment.firstViewModel = firstViewModel

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.activity)
        bindingOrderFragment.recyclerViewOrder.layoutManager = layoutManager

        val orderAdapter =
            OrderAdapter(OrderListener { order -> firstViewModel.selectOrder(order) })
        bindingOrderFragment.recyclerViewOrder.adapter = orderAdapter

        bindingOrderFragment.lifecycleOwner = this

        firstViewModel.allOrders.observe(viewLifecycleOwner) {
            it.let { orderAdapter.submitList(it) }
        }
        firstViewModel.selectedOrder.observe(viewLifecycleOwner) { order ->
            order.let {
                if (order != null) {
                    this.findNavController()
                        .navigate(OrderFragmentDirections.actionOrderFragmentToProdFragment(order.daysordermoveid))

                    firstViewModel.doneSelectOrder()
                }
            }
        }

        val periodicRequest = PeriodicWorkRequest
            .Builder(GetDataWorker::class.java, 15, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(application).enqueueUniquePeriodicWork(
            POLL_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicRequest
        )

        return bindingOrderFragment.root
    }
}