package com.firstbreadclient.model

import androidx.annotation.WorkerThread
import com.firstbreadclient.model.data.Auth
import com.firstbreadclient.model.data.Order
import com.firstbreadclient.model.data.Prod
import com.firstbreadclient.room.FirstDao
import kotlinx.coroutines.flow.Flow

class FirstRepository(private val firstDao: FirstDao) {
    val allAuths: Flow<List<Auth>> = firstDao.getAlphabetizedAuths()
    val allOrders: Flow<List<Order>> = firstDao.getAlphabetizedOrders()
    val allProds: Flow<List<Prod>> = firstDao.getAlphabetizedProds()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAuth(auth: Auth) {
        firstDao.insertAuth(auth)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertOrder(order: Order) {
        firstDao.insertOrder(order)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertProd(prod: Prod) {
        firstDao.insertProd(prod)
    }
}