package com.firstbreadclient.activity.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingAuthFragment: AuthFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.auth_fragment, container, false
        )

        val application = requireNotNull(this.activity).application

        val viewModelFactory = FirstViewModelFactory((application as FirstApplication).repository)

        val firstViewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(FirstViewModel::class.java)

        bindingAuthFragment.firstViewModel = firstViewModel

        val authAdapter = AuthAdapter(AuthListener { auth -> firstViewModel.selectAuth(auth) })
        bindingAuthFragment.recyclerViewAuth.adapter = authAdapter

        bindingAuthFragment.lifecycleOwner = this

        firstViewModel.allAuths.observe(viewLifecycleOwner, {
            it.let {
                authAdapter.submitList(it)
            }
        })
        firstViewModel.selectedAuth.observe(viewLifecycleOwner, { auth ->
            auth.let {
                if (auth != null) {
                    this.findNavController()
                        .navigate(AuthFragmentDirections.actionAuthFragmentToOrderFragment(auth.cntkod))
                    firstViewModel.doneSelectAuth()
                }
            }
        })
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this.activity)
        bindingAuthFragment.recyclerViewAuth.layoutManager = layoutManager

        return bindingAuthFragment.root
    }
}