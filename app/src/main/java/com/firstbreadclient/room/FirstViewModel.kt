package com.firstbreadclient.room

import android.util.Log
import androidx.lifecycle.*
import com.firstbreadclient.model.data.Auth
import com.firstbreadclient.model.data.Order
import com.firstbreadclient.model.data.Prod
import com.firstbreadclient.model.FirstRepository
import com.firstbreadclient.mvp.view.AuthView
import com.firstbreadclient.network.OkHttpClientInstance
import com.firstbreadclient.network.RetrofitInstance
import com.firstbreadclient.service.GetDataService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class FirstViewModel(private val repository: FirstRepository) : ViewModel() {
    var mService: GetDataService? = null

    init {
        mService = RetrofitInstance.retrofitInstance?.create(GetDataService::class.java)
    }

    private var orderList: ArrayList<Order?>? = null
    private var prodList: ArrayList<Prod?>? = null

    val allAuths: LiveData<List<Auth>> = repository.allAuths.asLiveData()
    val allOrders: LiveData<List<Order>> = repository.allOrders.asLiveData()
    val allProds: LiveData<List<Prod>> = repository.allProds.asLiveData()

    private val mutableSelectedAuth = MutableLiveData<Auth?>()
    val selectedAuth: LiveData<Auth?> get() = mutableSelectedAuth

    private val mutableSelectedOrder = MutableLiveData<Order?>()
    val selectedOrder: LiveData<Order?> get() = mutableSelectedOrder

    private val mutableSelectedProd = MutableLiveData<Prod?>()
    val selectedProd: LiveData<Prod?> get() = mutableSelectedProd

    fun selectAuth(auth: Auth) {
        orderData(auth)
        mutableSelectedAuth.value = auth
    }

    fun doneSelectAuth() {
        mutableSelectedAuth.value = null
    }

    fun selectOrder(order: Order) {
        prodData(order)
        mutableSelectedOrder.value = order
    }

    fun doneSelectOrder() {
        mutableSelectedOrder.value = null
    }

    fun selectProd(prod: Prod) {
        mutableSelectedProd.value = prod
    }

    fun doneSelectProd() {
        mutableSelectedProd.value = null
    }

    fun insertAuth(auth: Auth) = viewModelScope.launch {
        repository.insertAuth(auth)
    }

    fun insertOrder(order: Order) = viewModelScope.launch {
        repository.insertOrder(order)
    }

    fun insertProd(prod: Prod) = viewModelScope.launch {
        repository.insertProd(prod)
    }

    private fun orderData(auth: Auth) {
        val jwt = OkHttpClientInstance.getSession()?.getToken()
        jwt ?: return

        val call = mService?.getOrderData(jwt, auth.cntkod)

        Log.i("URL Called", call?.request()?.url.toString() + "")

        call?.enqueue(object : Callback<ArrayList<Order?>?> {
            override fun onResponse(
                call: Call<ArrayList<Order?>?>,
                response: Response<ArrayList<Order?>?>
            ) {
                if (!response.isSuccessful) {
                    return
                }
                updateOrderUi(response.body())
            }

            override fun onFailure(call: Call<ArrayList<Order?>?>, t: Throwable) {
                t.message?.let { Log.e("Error message", it) }
            }
        })
    }

    fun updateOrderUi(orderList: ArrayList<Order?>?) {
        this.orderList = orderList
        for (order in this.orderList!!) {
            if (order != null) {
                insertOrder(order)
            }
        }
    }

    private fun prodData(order: Order) {
        val jwt = OkHttpClientInstance.getSession()?.getToken()
        jwt ?: return

        val call = mService?.getProdData(jwt, order.daysordermoveid)

        Log.i("URL Called", call?.request()?.url.toString() + "")

        call?.enqueue(object : Callback<ArrayList<Prod?>?> {
            override fun onResponse(
                call: Call<ArrayList<Prod?>?>,
                response: Response<ArrayList<Prod?>?>
            ) {
                if (!response.isSuccessful) {
                    return
                }
                updateProdUi(response.body())
            }

            override fun onFailure(call: Call<ArrayList<Prod?>?>, t: Throwable) {
                t.message?.let { Log.e("Error message", it) }
            }
        })
    }

    fun updateProdUi(prodList: ArrayList<Prod?>?) {
        this.prodList = prodList
        for (prod in this.prodList!!) {
            if (prod != null) {
                insertProd(prod)
            }
        }
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
