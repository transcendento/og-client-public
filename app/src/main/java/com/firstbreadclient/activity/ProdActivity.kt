package com.firstbreadclient.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.firstbreadclient.R
import com.firstbreadclient.model.data.Prod
import com.firstbreadclient.mvp.AppModuleProd
import com.firstbreadclient.mvp.DaggerAppComponentProd
import com.firstbreadclient.mvp.presenter.ProdPresenter
import com.firstbreadclient.mvp.view.ProdView
import com.firstbreadclient.network.OkHttpClientInstance
import com.firstbreadclient.network.listener.InternetConnectionListener
import com.firstbreadclient.room.FirstApplication
import com.firstbreadclient.room.FirstViewModel
import com.firstbreadclient.room.FirstViewModelFactory
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.util.Objects
import javax.inject.Inject

class ProdActivity : AppCompatActivity(), InternetConnectionListener, ProdView {
    private var mDataBundle: Bundle? = null
    private var mProdList: ArrayList<Prod?>? = null
    //private var mService: GetDataService? = null

    private val firstViewModel: FirstViewModel by viewModels {
        FirstViewModelFactory((application as FirstApplication).repository)
    }

    //private var prodAdapter: ProdAdapter? = null
    private var mAppBarProd: AppBarLayout? = null
    private var parentLayout: View? = null

    @Inject
    lateinit var prodPresenter: ProdPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAppComponentProd.builder().appModuleProd(AppModuleProd(this)).build().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.prod_main)
        val toolbar = findViewById<Toolbar>(R.id.authToolbar)
        setSupportActionBar(toolbar)

        mDataBundle = intent.extras

        OkHttpClientInstance.setInternetConnectionListener(this)

        parentLayout = findViewById(android.R.id.content)

        mAppBarProd = findViewById(R.id.orderAppBarLayout)

        prodPresenter.bind(this)

        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)

        val mCntNameStrText = findViewById<TextView>(R.id.text_cntnamestr_prod)

        mCntNameStrText.text = intent.getStringExtra("cntnamestr")

/*
        val mRecyclerView = findViewById<RecyclerView>(R.id.recycler_view_prod_list)
        //prodAdapter = ProdAdapter(this)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@ProdActivity)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = prodAdapter
*/

        //mService = RetrofitInstance.retrofitInstance?.create(GetDataService::class.java)

        prodPresenter.prodData()
        //prodData

/*
        firstViewModel.allProds.observe(owner = this) { prods ->
            // Update the cached copy of the words in the adapter.
            prods.let { prodAdapter!!.setProds(it) }
        }
*/

        val fabOrder = findViewById<FloatingActionButton>(R.id.fab_prod)
        fabOrder.setOnClickListener { onClick() }

        val collapsingToolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutProd)
        val appBarLayout = findViewById<AppBarLayout>(R.id.appBarLayoutProd)
        appBarLayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.title = intent.getStringExtra("cntkod") + " на " + intent.getStringExtra("daysorderdate")
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

    override fun onDestroy() {
        prodPresenter.unbind()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.order_list_actions, menu)
        val mSearchItem = menu.findItem(R.id.search)
        val searchView = mSearchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //prodAdapter!!.filter.filter(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, OrderActivity::class.java)
                intent.putExtras(mDataBundle!!)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

/*
    private val prodData: Unit
        get() {
            val daysId = intent.getStringExtra("daysordermoveid")
            val jwt = OkHttpClientInstance.getSession()?.getToken()
            val call = mService!!.getProdData(jwt, daysId)
            Log.i("URL Called", call?.request()?.url.toString() + "")
            call?.enqueue(object : Callback<ArrayList<Prod?>?> {
                override fun onResponse(call: Call<ArrayList<Prod?>?>, response: Response<ArrayList<Prod?>?>) {
                    if (!response.isSuccessful) {
                        Toast.makeText(this@ProdActivity, "Ошибка соединения", Toast.LENGTH_SHORT).show()
                        return
                    }
                    mProdList = response.body()
                    for (prod in mProdList!!) {
                        if (prod != null) {
                            firstViewModel.insertProd(prod)
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<Prod?>?>, t: Throwable) {
                    t.message?.let { Log.e("Error message", it) }
                    Toast.makeText(this@ProdActivity, "Something went wrong...Error message: " + t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

    private val prodPost: Unit
        get(){
            val allProd = firstViewModel.allProds.value
            val call = allProd?.let { mService!!.postProd(it) }
            call?.enqueue(object: Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
*/

    override fun putIntent(): Intent? {
        return intent
    }

    override fun putViewModel(): FirstViewModel {
        return firstViewModel
    }

    private fun onClick() {
        prodPresenter.prodPost()
        mAppBarProd!!.setExpanded(false)
    }

    override fun onInternetUnavailable() {}
    override fun updateProdUi(prodList: ArrayList<Prod?>?) {
        this.mProdList = prodList
        for (prod in this.mProdList!!) {
            if (prod != null) {
                firstViewModel.insertProd(prod)
            }
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(this@ProdActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun showSnackbar(message: String) {
        Snackbar.make(parentLayout!!, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()

    }

}