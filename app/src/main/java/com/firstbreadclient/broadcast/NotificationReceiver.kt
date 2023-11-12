package com.firstbreadclient.broadcast

import android.annotation.SuppressLint
import android.app.Activity.*
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.firstbreadclient.worker.GetDataWorker

private const val TAG = "NotificationReceiver"

class NotificationReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        //Log.i(TAG, "received broadcast: ${intent.action}")
        Log.i(TAG, "received result: $resultCode")

        if (resultCode != RESULT_OK) {

        }
        val requestCode = intent.getIntExtra(GetDataWorker.REQUEST_CODE, 0)
        val notification: Notification? = intent.getParcelableExtra(GetDataWorker.NOTIFICATION)
        val notificationManager = NotificationManagerCompat.from(context)

        if (notification != null) {
            notificationManager.notify(requestCode, notification)
        }
    }
}
