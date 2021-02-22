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

/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */
@Database(entities = [Auth::class, Order::class, Prod::class], version = 3, exportSchema = false)
abstract class FirstRoomDatabase : RoomDatabase() {
    abstract fun firstDao(): FirstDao

    /**
     * Populate the database in the background.
     * If you want to start with more words, just add them.
     */
    /*private class PopulateDbAsync(db: RoomDatabase?) : AsyncTask<Void?, Void?, Void?>() {
        private val mDao: RoomDao = db!!.roomDao()

        override fun doInBackground(vararg params: Void?): Void? {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            mDao.deleteAllOrder()
            mDao.deleteAllProd()
            return null
        }
    }*/

    companion object {
        @Volatile
        private var INSTANCE: FirstRoomDatabase? = null

        fun getDatabase(
                context: Context,
                scope: CoroutineScope
        ): FirstRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        FirstRoomDatabase::class.java,
                        "first_database"
                )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this codelab.
                        .fallbackToDestructiveMigration()
                        .addCallback(FirstDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class FirstDatabaseCallback(
                private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.firstDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(firstDao: FirstDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            firstDao.deleteAllOrders()
            firstDao.deleteAllProds()
        }
    }
/*
    companion object {
        // marking the instance as volatile to ensure atomic access to the variable
        @Volatile
        private var INSTANCE: RoomDatabase? = null
        fun getDatabase(context: Context): RoomDatabase? {
            if (INSTANCE == null) {
                synchronized(RoomDatabase::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                RoomDatabase::class.java, "order_database")
                                // Wipes and rebuilds instead of migrating if no Migration object.
                                // Migration is not part of this codelab.
                                .fallbackToDestructiveMigration()
                                .addCallback(sRoomDatabaseCallback)
                                .build()
                    }
                }
            }
            return INSTANCE
        }

        private fun populateDb (db: RoomDatabase?) {
            Executors.newFixedThreadPool(2).submit{
                val mDao: RoomDao = db!!.roomDao()
                mDao.deleteAllOrders()
                mDao.deleteAllProds()
            }
        }

        */
/**
         * Override the onOpen method to populate the database.
         * For this sample, we clear the database every time it is created or opened.
         *
         *
         * If you want to populate the database only when the database is created for the 1st time,
         * override RoomDatabase.Callback()#onCreate
         *//*

        private val sRoomDatabaseCallback: Callback = object : Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                // PopulateDbAsync(INSTANCE).execute()
                populateDb(INSTANCE)
            }
        }
    }
*/


}