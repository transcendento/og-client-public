package com.firstbreadclient.activity.fragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firstbreadclient.R
import com.firstbreadclient.adapter.AuthAdapter
import com.firstbreadclient.adapter.AuthListener
import com.firstbreadclient.adapter.ProdAdapter
import com.firstbreadclient.adapter.ProdListener
import com.firstbreadclient.databinding.AuthFragmentBinding
import com.firstbreadclient.databinding.ProdFragmentBinding
import com.firstbreadclient.room.FirstApplication
import com.firstbreadclient.room.FirstViewModel
import com.firstbreadclient.room.FirstViewModelFactory

class ProdFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
            val bindingProdFragment: ProdFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.prod_fragment, container, false)

            val application = requireNotNull(this.activity).application

            val viewModelFactory = FirstViewModelFactory((application as FirstApplication).repository)

            val firstViewModel = ViewModelProvider(
                this, viewModelFactory).get(FirstViewModel::class.java)

            bindingProdFragment.firstViewModel = firstViewModel

            val prodAdapter = ProdAdapter(ProdListener { prod -> firstViewModel.selectProd(prod) })
            bindingProdFragment.recyclerViewProd.adapter = prodAdapter

            bindingProdFragment.lifecycleOwner = this

            firstViewModel.allProds.observe(viewLifecycleOwner, {
                it.let {
                    prodAdapter.submitList(it)
                }
            })

            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.activity)
            bindingProdFragment.recyclerViewProd.layoutManager = layoutManager

            return bindingProdFragment.root
    }
}