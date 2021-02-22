package com.firstbreadclient.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firstbreadclient.R
import com.firstbreadclient.adapter.ItemClickListener
import com.firstbreadclient.adapter.OrderAdapter
import com.firstbreadclient.model.data.Order
import com.firstbreadclient.network.OkHttpClientInstance
import com.firstbreadclient.network.RetrofitInstance
import com.firstbreadclient.network.listener.InternetConnectionListener
import com.firstbreadclient.room.FirstApplication
import com.firstbreadclient.room.FirstViewModel
import com.firstbreadclient.room.FirstViewModelFactory
import com.firstbreadclient.service.GetDataService
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class OrderActivity : AppCompatActivity(), ItemClickListener, InternetConnectionListener {
    private var mDataBundle: Bundle? = null
    private var mOrderList: ArrayList<Order?>? = null
    //private var mOrderViewModel: RoomModel? = null

    private val firstViewModel: FirstViewModel by viewModels {
        FirstViewModelFactory((application as FirstApplication).repository)
    }

    private var mService: GetDataService? = null
    private var mAppBarOrder: AppBarLayout? = null
    private var mParentLayout: View? = null

    @SuppressLint("CutPasteId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_main)
        val toolbar = findViewById<Toolbar>(R.id.authToolbar)
        setSupportActionBar(toolbar)

        mDataBundle = intent.extras
        mParentLayout = findViewById(android.R.id.content)

        OkHttpClientInstance.setInternetConnectionListener(this)

        mAppBarOrder = findViewById(R.id.orderAppBarLayout)

        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)

        toolbar.setOnClickListener { onClick() }

        val orderDate = mDataBundle?.getString("orderdate")
        val cntKod = mDataBundle?.getString("cntkod")
        val mOrderDate = findViewById<TextView>(R.id.TextViewDateOrder)
        mOrderDate.text = "$orderDate $cntKod"

        val mRecyclerView = findViewById<RecyclerView>(R.id.RecyclerViewOrder)
        val orderAdapter = OrderAdapter(this)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@OrderActivity)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = orderAdapter

        orderAdapter.setClickListener(this)

        mService = RetrofitInstance.retrofitInstance?.create(GetDataService::class.java)

        orderData

        // Get a new or existing ViewModel from the ViewModelProvider.
        //mOrderViewModel = ViewModelProviders.of(this).get(RoomModel::class.java)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        // Update the cached copy of the words in the adapter.
        //mOrderViewModel!!.allOrders.observe(this, Observer { orders: List<Order> -> orderAdapter.setOrders(orders) })
        firstViewModel.allOrders.observe(owner = this) { orders ->
            // Update the cached copy of the words in the adapter.
            orders.let { orderAdapter.setOrders(it) }
        }

        val fabOrder = findViewById<FloatingActionButton>(R.id.fab_order)
        fabOrder.setOnClickListener { orderData }

        val collapsingToolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.CollapsingToolbarLayoutOrder)
        val appBarLayout = findViewById<AppBarLayout>(R.id.orderAppBarLayout)
        appBarLayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset < 120) {
                    collapsingToolbarLayout.title = "$orderDate $cntKod"
                    isShow = true
                } else if (isShow) {
                    collapsingToolbarLayout.title = " " //careful there should a space between double quote otherwise it wont work
                    isShow = false
                }
            }
        })
    }

    public override fun onPause() {
        super.onPause()
        OkHttpClientInstance.removeInternetConnectionListener()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val orderData: Unit
        get() {
            val cntKod = intent.getStringExtra("cntkod")
            val jwt = OkHttpClientInstance.getSession()?.getToken()
            val call = mService!!.getOrderData(jwt, cntKod)
            Log.i("URL Called", call?.request()?.url.toString() + "")
            call?.enqueue(object : Callback<ArrayList<Order?>?> {
                override fun onResponse(call: Call<ArrayList<Order?>?>, response: Response<ArrayList<Order?>?>) {
                    if (!response.isSuccessful) {
                        Toast.makeText(this@OrderActivity, "Ошибка соединения", Toast.LENGTH_SHORT).show()
                        return
                    }
                    mOrderList = response.body()
                    for (order in mOrderList!!) {
                        if (order != null) {
                            firstViewModel.insertOrder(order)
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<Order?>?>, t: Throwable) {
                    t.message?.let { Log.e("Error message", it) }
                    Toast.makeText(this@OrderActivity, "Something went wrong...Error message: " + t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

    override fun onClick(view: View?, position: Int) {
        val order = mOrderList!![position]
        val intent = Intent(this, ProdActivity::class.java)
        mDataBundle!!.putString("cntkod", order?.cntkod)
        intent.putExtras(mDataBundle!!)
        intent.putExtra("daysordermoveid", order?.daysordermoveid)
        intent.putExtra("cntkod", order?.cntkod)
        intent.putExtra("cntnamestr", order?.cntnamestr)
        intent.putExtra("daysorderdate", order?.daysorderdate)
        Log.i("cntnamestr", order!!.cntnamestr)
        startActivity(intent)
    }

    private fun onClick() {
        mAppBarOrder!!.setExpanded(true)
    }

    override fun onInternetUnavailable() {
        Snackbar.make(mParentLayout!!, resources.getString(R.string.conn_fail), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
    }
}