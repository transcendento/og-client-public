package com.firstbreadclient.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.firstbreadclient.model.data.Auth
import com.firstbreadclient.model.data.Order
import com.firstbreadclient.model.data.Prod
import com.firstbreadclient.model.FirstRepository
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */
class FirstViewModel(private val repository: FirstRepository) : ViewModel() {
    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allAuths: LiveData<List<Auth>> = repository.allAuths.asLiveData()
    val allOrders: LiveData<List<Order>> = repository.allOrders.asLiveData()
    val allProds: LiveData<List<Prod>> = repository.allProds.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertAuth(auth: Auth) = viewModelScope.launch {
        repository.insertAuth(auth)
    }
    fun insertOrder(order: Order) = viewModelScope.launch {
        repository.insertOrder(order)
    }
    fun insertProd(prod: Prod) = viewModelScope.launch {
        repository.insertProd(prod)
    }
}

class FirstViewModelFactory(private val repository: FirstRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FirstViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FirstViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
