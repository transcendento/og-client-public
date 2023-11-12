package com.firstbreadclient.worker

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.firstbreadclient.R
import com.firstbreadclient.activity.OrderActivity
import com.firstbreadclient.model.data.Order
import com.firstbreadclient.network.OkHttpClientInstance
import com.firstbreadclient.network.RetrofitInstance
import com.firstbreadclient.room.NOTIFICATION_CHANNEL_ID
import com.firstbreadclient.service.GetDataService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "GetDataWorker"

class GetDataWorker(val context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    private var mService: GetDataService? = null
    private var jwt: String = ""
    private var cntKod: String = ""
    private var mOrderList: ArrayList<Order?>? = null

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        mService = RetrofitInstance.retrofitInstance?.create(GetDataService::class.java)
        jwt = OkHttpClientInstance.getSession()?.getToken().toString()
        cntKod = OkHttpClientInstance.getSession()?.getCntkod().toString()

        Log.i(TAG, "Work request triggered")

        orderData()

        val outputData = workDataOf("task" to "task details")

        val intent = OrderActivity.newIntent(context)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_MUTABLE
        )
        val resources = context.resources
        val notification = NotificationCompat
            .Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bakery)
            .setTicker(resources.getString(R.string.msg_update))
            .setSmallIcon(android.R.drawable.ic_menu_report_image)
            .setContentTitle(resources.getString(R.string.msg_update))
            .setContentText(resources.getString(R.string.msg_update))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        /*
                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.notify(0, notification)

                context.sendBroadcast(Intent(ACTION_SHOW_NOTIFICATION), PERM_PRIVATE)
        */
        showBackgroundNotification(0, notification)

        return Result.success()
    }

    private fun showBackgroundNotification(
        requestCode: Int,
        notification: Notification
    ) {
        val intent = Intent(ACTION_SHOW_NOTIFICATION).apply {
            putExtra(REQUEST_CODE, requestCode)
            putExtra(NOTIFICATION, notification)
        }
        context.sendOrderedBroadcast(intent, PERM_PRIVATE)
    }

    private fun orderData() {
        val call = mService?.getOrderData(jwt, cntKod)
        Log.i("URL Called", call?.request()?.url.toString() + "")
        call?.enqueue(object : Callback<ArrayList<Order?>?> {
            override fun onResponse(
                call: Call<ArrayList<Order?>?>,
                response: Response<ArrayList<Order?>?>
            ) {
                if (!response.isSuccessful) {
                    return
                }
                mOrderList = response.body()
            }

            override fun onFailure(call: Call<ArrayList<Order?>?>, t: Throwable) {
                t.message?.let { Log.e("Error message", it) }
            }
        })
    }

    companion object {
        const val ACTION_SHOW_NOTIFICATION = "com.firstbreadclient.SHOW_NOTIFICATION"
        const val PERM_PRIVATE = "com.firstbreadclient.PRIVATE"
        const val REQUEST_CODE = "REQUEST_CODE"
        const val NOTIFICATION = "NOTIFICATION"
    }

}