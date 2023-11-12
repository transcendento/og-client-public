package com.firstbreadclient.activity.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firstbreadclient.R
import com.firstbreadclient.adapter.ProdAdapter
import com.firstbreadclient.adapter.ProdListener
import com.firstbreadclient.databinding.ProdFragmentBinding
import com.firstbreadclient.room.FirstApplication
import com.firstbreadclient.room.FirstViewModel
import com.firstbreadclient.room.FirstViewModelFactory

//private const val TAG_OUTPUT = "OUTPUT"

class ProdFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingProdFragment: ProdFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.prod_fragment, container, false
        )

        val application = requireNotNull(this.activity).application

        val viewModelFactory = FirstViewModelFactory((application as FirstApplication).repository)

        val firstViewModel = ViewModelProvider(
            this, viewModelFactory
        )[FirstViewModel::class.java]

        bindingProdFragment.firstViewModel = firstViewModel

        val prodAdapter =
            ProdAdapter(ProdListener { prod -> firstViewModel.selectProd(prod) }, firstViewModel)
        bindingProdFragment.recyclerViewProd.adapter = prodAdapter

        bindingProdFragment.lifecycleOwner = this

        firstViewModel.allProds.observe(viewLifecycleOwner) {
            it.let {
                prodAdapter.submitList(it)
            }
        }

        firstViewModel.selectedProd.observe(viewLifecycleOwner) { prod ->
            prod.let {
                if (prod != null) {
                    firstViewModel.doneSelectProd()
                    Log.d("prodCardView", "onClick " + prod.daysordermovecontentid)
                }
            }
        }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.activity)
        bindingProdFragment.recyclerViewProd.layoutManager = layoutManager

        /*
                val daysId = savedInstanceState?.getString("daysordermoveid")

                val data: Data = Data.Builder()
                    .putString("daysordermoveid", daysId)
                    .build()

                val workRequest = OneTimeWorkRequest
                    .Builder(GetDataWorker::class.java)
                    .addTag(TAG_OUTPUT)
                    .setInputData(data)
                    .build()
                WorkManager.getInstance(application).enqueue(workRequest)
        */

        return bindingProdFragment.root
    }
}