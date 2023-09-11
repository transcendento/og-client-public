package com.firstbreadclient.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.firstbreadclient.model.data.Auth
import com.firstbreadclient.model.data.Order
import com.firstbreadclient.model.data.Prod
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Auth::class, Order::class, Prod::class], version = 3, exportSchema = false)
abstract class FirstRoomDatabase : RoomDatabase() {
    abstract fun firstDao(): FirstDao

    companion object {
        @Volatile
        private var INSTANCE: FirstRoomDatabase? = null

        fun getDatabase(
                context: Context,
                scope: CoroutineScope
        ): FirstRoomDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        FirstRoomDatabase::class.java,
                        "first_database"
                )

                        .fallbackToDestructiveMigration()
                        .addCallback(FirstDatabaseCallback(scope))
                        .build()
                INSTANCE = instance

                instance
            }
        }

        private class FirstDatabaseCallback(
                private val scope: CoroutineScope
        ) : Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.firstDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(firstDao: FirstDao) {
            firstDao.deleteAllOrders()
            firstDao.deleteAllProds()
        }
    }
}