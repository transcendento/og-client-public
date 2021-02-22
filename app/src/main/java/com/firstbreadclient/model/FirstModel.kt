package com.firstbreadclient.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */
class FirstModel(application: Application?) : AndroidViewModel(application!!) {
    /*private val repository: RoomRepository = RoomRepository(application)

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allAuths: LiveData<List<Auth>>
    val allOrders: LiveData<List<Order>>
    val allProds: LiveData<List<Prod>>
    fun insertAuth(auth: Auth?) {
        repository.insertAuth(auth)
    }

    fun insertOrder(order: Order?) {
        repository.insertOrder(order)
    }

    fun insertProd(prod: Prod?) {
        repository.insertProd(prod)
    }

    init {
        allAuths = repository.allAuths
        allOrders = repository.allOrders
        allProds = repository.allProds
    }*/
}