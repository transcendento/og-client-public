package com.firstbreadclient.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.firstbreadclient.model.data.Auth
import com.firstbreadclient.model.data.Order
import com.firstbreadclient.model.data.Prod
import kotlinx.coroutines.flow.Flow

/**
 * The Room Magic is in this file, where you map a Java method call to an SQL query.
 *
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */
@Dao
interface FirstDao {

    // The flow always holds/caches latest version of data. Notifies its observers when the
    // data has changed.
    @Query("SELECT * from auth_table ORDER BY cntkod ASC")
    fun getAlphabetizedAuths(): Flow<List<Auth>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAuth(auth: Auth)

    @Query("DELETE FROM auth_table")
    suspend fun deleteAllAuths()

    @Query("SELECT * from order_table ORDER BY daysorderdate ASC")
    fun getAlphabetizedOrders(): Flow<List<Order>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrder(order: Order)

    @Query("DELETE FROM order_table")
    suspend fun deleteAllOrders()

    @Query("SELECT * from prod_table ORDER BY prodlongname ASC")
    fun  getAlphabetizedProds(): Flow<List<Prod>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProd(prod: Prod)

    @Query("DELETE FROM prod_table")
    suspend fun deleteAllProds()
}