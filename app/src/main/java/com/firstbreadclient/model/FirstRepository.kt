package com.firstbreadclient.model

import androidx.annotation.WorkerThread
import com.firstbreadclient.model.data.Auth
import com.firstbreadclient.model.data.Order
import com.firstbreadclient.model.data.Prod
import com.firstbreadclient.room.FirstDao
import kotlinx.coroutines.flow.Flow

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */
class FirstRepository (private val firstDao: FirstDao) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allAuths: Flow<List<Auth>> = firstDao.getAlphabetizedAuths()
    val allOrders: Flow<List<Order>> = firstDao.getAlphabetizedOrders()
    val allProds: Flow<List<Prod>> = firstDao.getAlphabetizedProds()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
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

    //private val roomDao: RoomDao

    /*val allAuths: LiveData<List<Auth>>
    val allOrders: LiveData<List<Order>>
    val allProds: LiveData<List<Prod>>*/

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
/*
        fun insertAuth(auth: Auth?) {
        InsertAsyncTaskAuth(roomDao).execute(auth)
    }
*/
/*
    fun insertAuth(auth: Auth?) {
        Executors.newFixedThreadPool(2).submit {
            roomDao.insertAuth(auth)
        }
    }
*/
/*
    fun insertAuth(auth: Auth?) {
        Observable.fromCallable {
            roomDao.insertAuth(auth)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }
*/
/*
        fun insertOrder(order: Order?) {
        InsertAsyncTaskOrder(roomDao).execute(order)
    }
*/

/*
    fun insertOrder(order: Order?) {
        Observable.fromCallable {
            roomDao.insertOrder(order)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }
*/

/*
        fun insertProd(prod: Prod?) {
        InsertAsyncTaskProd(roomDao).execute(prod)
    }
*/

/*
    fun insertProd(prod: Prod?) {
        Observable.fromCallable {
            roomDao.insertProd(prod)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    private class InsertAsyncTaskAuth(private val asyncTaskDao: RoomDao) : AsyncTask<Auth?, Void?, Void?>() {
        override fun doInBackground(vararg params: Auth?): Void? {
            asyncTaskDao.insertAuth(params[0])
            return null
        }
    }

    private class InsertAsyncTaskOrder(private val asyncTaskDao: RoomDao) : AsyncTask<Order?, Void?, Void?>() {
        override fun doInBackground(vararg params: Order?): Void? {
            asyncTaskDao.insertOrder(params[0])
            return null
        }
    }

    private class InsertAsyncTaskProd constructor(private val asyncTaskDao: RoomDao) : AsyncTask<Prod?, Void?, Void?>() {
        override fun doInBackground(vararg params: Prod?): Void? {
            asyncTaskDao.insertProd(params[0])
            return null
        }
    }
*/

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    /*init {
        val db = application?.let { RoomDatabase.getDatabase(it) }
        roomDao = db!!.roomDao()
        allAuths = roomDao.alphabetizedAuths
        allOrders = roomDao.alphabetizedOrders
        allProds = roomDao.alphabetizedProds
    }*/
}