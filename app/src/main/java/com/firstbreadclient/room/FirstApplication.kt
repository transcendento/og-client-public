package com.firstbreadclient.room

import android.app.Application
import com.firstbreadclient.model.FirstRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class FirstApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { FirstRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { FirstRepository(database.firstDao()) }
}
