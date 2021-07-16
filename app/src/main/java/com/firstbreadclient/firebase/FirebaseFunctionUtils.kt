package com.firstbreadclient.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import java.text.SimpleDateFormat
import java.util.*

fun dateServer(timestamp: Long): String {
    val myFormat = "dd.MM.yyyy"
    val sdf = SimpleDateFormat(myFormat, Locale.US)

    val cal = Calendar.getInstance()
    cal.time = Date(timestamp)
    cal.add(Calendar.DATE, 1)

    return sdf.format(cal.time)
    //return "01.11.2018";
}

fun timeServer(timestamp: Long): String {
    val myFormat = "HH:mm"
    val sdf = SimpleDateFormat(myFormat, Locale.US)

    val cal = Calendar.getInstance()
    cal.time = Date(timestamp)

    return sdf.format(cal.time)
}

fun dateDevice(): String {
    val myFormat = "dd.MM.yyyy"
    val sdf = SimpleDateFormat(myFormat, Locale.US)

    val cal = Calendar.getInstance()
    cal.time = Date()
    cal.add(Calendar.DATE, 1)

    return sdf.format(cal.time)
    //return "01.11.2018";
}

fun timeDevice(): String {
    val myFormat = "HH:mm"
    val sdf = SimpleDateFormat(myFormat, Locale.US)

    val cal = Calendar.getInstance()
    cal.time = Date()

    return sdf.format(cal.time)
}

class FirebaseFunctionUtils {
    fun getServerTime(onComplete: OnGetServerTime?) {
        FirebaseFunctions.getInstance().getHttpsCallable("getTime")
            .call()
            .addOnCompleteListener { task: Task<HttpsCallableResult> ->
                when (task.isSuccessful) {
                    true -> {
                        val timestamp = task.result!!.data as Long
                        val dateServer = dateServer(timestamp)
                        val timeServer = timeServer(timestamp)
                        onComplete?.onSuccess(dateServer, timeServer)
                    }
                    else -> {
                        val dateDevice = dateDevice()
                        val timeDevice = timeDevice()
                        onComplete!!.onFailed(dateDevice, timeDevice)
                    }
                }
            }
    }
}