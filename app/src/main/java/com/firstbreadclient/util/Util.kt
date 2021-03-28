package com.firstbreadclient.util

import android.content.res.Resources
import com.firstbreadclient.R

fun convertMoveFlagToString(moveFlag: Int, resources: Resources): String {
    var moveFlagString = resources.getString(R.string.save_fail)
    when (moveFlag) {
        1 -> moveFlagString = resources.getString(R.string.send_ok)
    }
    return moveFlagString
}
