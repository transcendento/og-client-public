package com.firstbreadclient.activity.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firstbreadclient.R
import com.firstbreadclient.adapter.AuthAdapter
import com.firstbreadclient.adapter.AuthListener
import com.firstbreadclient.databinding.AuthFragmentBinding
import com.firstbreadclient.room.FirstApplication
import com.firstbreadclient.room.FirstViewModel
import com.firstbreadclient.room.FirstViewModelFactory

class AuthFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: AuthFragmentBinding = DataBindingUtil.inflate(inflater,
                R.layout.auth_fragment, container, false)

        val application = requireNotNull(this.activity).application

        val viewModelFactory = FirstViewModelFactory((application as FirstApplication).repository)

        val firstViewModel = ViewModelProvider(
                this, viewModelFactory).get(FirstViewModel::class.java)

        binding.firstViewModel = firstViewModel

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.activity)
        binding.recyclerViewAuth.layoutManager = layoutManager

        val authAdapter = AuthAdapter(AuthListener {  })
        binding.recyclerViewAuth.adapter = authAdapter

        binding.lifecycleOwner = this

        firstViewModel.allAuths.observe(viewLifecycleOwner, Observer {
            it.let { authAdapter.setAuths(it) }
        })




        return binding.root
    }
}