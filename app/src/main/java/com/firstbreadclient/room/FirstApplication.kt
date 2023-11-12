package com.firstbreadclient.room

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.firstbreadclient.model.FirstRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

const val NOTIFICATION_CHANNEL_ID = "order_check"
class FirstApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { FirstRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { FirstRepository(database.firstDao()) }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "123"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
