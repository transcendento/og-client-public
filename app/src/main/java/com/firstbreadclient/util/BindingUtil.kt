package com.firstbreadclient.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.firstbreadclient.model.data.Auth
import com.firstbreadclient.model.data.Order
import com.firstbreadclient.model.data.Prod

@BindingAdapter("authKodText")
fun TextView.setAuthKodText(item: Auth?) {
    item?.let {
        text = item.cntkod
    }
}

@BindingAdapter("authNameText")
fun TextView.setAuthNameText(item: Auth?) {
    item?.let {
        text = item.cntname
    }
}

@BindingAdapter("authAdresText")
fun TextView.setAuthAdresText(item: Auth?) {
    item?.let {
        text = item.cntadres
    }
}

@BindingAdapter("orderDateText")
fun TextView.setOrderDateText(item: Order?) {
    item?.let {
        text = item.daysorderdate
    }
}

@BindingAdapter("orderFlagNameText")
fun TextView.setOrderFlagNameText(item: Order?) {
    item?.let {
        text = convertMoveFlagToString(1, context.resources)
    }
}

@BindingAdapter("prodLongNameText")
fun TextView.setProdLongNameText(item: Prod?) {
    item?.let {
        text = item.prodlongname
    }
}

@BindingAdapter("amountStr")
fun TextView.setAmountStrText(item: Prod?) {
    item?.let {
        text = item.amountstr
    }
}


