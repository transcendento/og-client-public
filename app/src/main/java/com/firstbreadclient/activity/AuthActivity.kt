package com.firstbreadclient.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import com.firstbreadclient.R
import com.firstbreadclient.activity.fragment.ProdFragment
import com.firstbreadclient.compose.RegActivity
import com.firstbreadclient.eventbus.NetworkEvent
import com.firstbreadclient.firebase.FirebaseFunctionUtils
import com.firstbreadclient.firebase.OnGetServerTime
import com.firstbreadclient.model.data.Auth
import com.firstbreadclient.model.data.Prod
import com.firstbreadclient.mvp.AppModule
import com.firstbreadclient.mvp.DaggerAppComponent
import com.firstbreadclient.mvp.presenter.AuthPresenter
import com.firstbreadclient.mvp.view.AuthView
import com.firstbreadclient.network.OkHttpClientInstance
import com.firstbreadclient.network.listener.AuthenticationListener
import com.firstbreadclient.network.listener.InternetConnectionListener
import com.firstbreadclient.room.FirstApplication
import com.firstbreadclient.room.FirstViewModel
import com.firstbreadclient.room.FirstViewModelFactory
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class AuthActivity : AppCompatActivity(), InternetConnectionListener, AuthenticationListener,
    AuthView {
    private var mDataBundle: Bundle? = null
    private var authList: ArrayList<Auth?>? = null

    private val firstViewModel: FirstViewModel by viewModels {
        FirstViewModelFactory((application as FirstApplication).repository)
    }

    private var mDateOrder: String? = null
    private var mParentLayout: View? = null

    private var listProds: List<Prod>? = null
    @Inject
    lateinit var authPresenter: AuthPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAppComponent.builder().appModule(AppModule(this)).build().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_main)

        mDataBundle = Bundle()

        authPresenter.bind(this)

        val toolbar = findViewById<Toolbar>(R.id.authToolbar)
        mParentLayout = findViewById(android.R.id.content)

        with(toolbar) {
            title = resources.getString(R.string.app_name)
            subtitle = resources.getString(R.string.clients)
        }

        setSupportActionBar(toolbar)

        val mActionBar = supportActionBar
        mActionBar!!.setLogo(R.drawable.ic_bakery)

        val firebaseFunctionUtils = FirebaseFunctionUtils()
        firebaseFunctionUtils.getServerTime(object : OnGetServerTime {
            override fun onSuccess(dateServer: String?, timeServer: String?) {
                mDateOrder = dateServer
            }

            override fun onFailed(dateDevice: String?, timeDevice: String?) {
                mDateOrder = dateDevice
            }
        })

        authPresenter.authToken()

        val fabAuth = findViewById<FloatingActionButton>(R.id.FloatingActionButtonAuth)
        fabAuth.setOnClickListener {
            val fragmentInstance = supportFragmentManager.findFragmentById(R.id.navHostFragment)

            if (fragmentInstance is ProdFragment) {
                authPresenter.prodPut()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent, mDataBundle)
            }
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.prodFragment -> {
                    fabAuth.setImageResource(android.R.drawable.ic_dialog_email)
                    fabAuth.visibility = View.VISIBLE
                }

                R.id.orderFragment -> {
                    fabAuth.visibility = View.GONE
                }

                R.id.authFragment -> {
                    fabAuth.setImageResource(android.R.drawable.ic_lock_idle_lock)
                    fabAuth.visibility = View.VISIBLE
                }
            }
        }

        fabAuth.setOnClickListener {
            if (navController.currentDestination?.id == R.id.prodFragment) {
                //authPresenter.prodDel()
                authPresenter.prodPut()
            } else {
/*
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent, mDataBundle)
*/

                val intent = Intent(this, RegActivity::class.java)
                startActivity(intent, mDataBundle)

            }
        }

        val collapsingToolbarLayout =
            findViewById<CollapsingToolbarLayout>(R.id.collapsingToolbarLayoutAuth)
        val appBarLayout = findViewById<AppBarLayout>(R.id.appBarLayoutAuth)
        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.title =
                        intent.getStringExtra("cntkod") + " на " + intent.getStringExtra("daysorderdate")
                    isShow = true
                } else if (isShow) {
                    collapsingToolbarLayout.title =
                        " " //careful there should a space between double quote otherwise it wont work
                    isShow = false
                }
            }
        })

        firstViewModel.allProds.observe(this) {
            it.let {
                listProds = it
            }
        }
    }

    override fun onResume() {
        super.onResume()
        authPresenter.authData()
    }

    public override fun onPause() {
        super.onPause()
        OkHttpClientInstance.removeInternetConnectionListener()
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        authPresenter.unbind()
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: NetworkEvent) {
        Snackbar.make(mParentLayout!!, event.message, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    override fun onInternetUnavailable() {
        Snackbar.make(
            mParentLayout!!,
            resources.getString(R.string.conn_fail),
            Snackbar.LENGTH_LONG
        )
            .setAction("Action", null).show()
    }

    override fun onUserLoggedOut() {
        Snackbar.make(
            mParentLayout!!,
            resources.getString(R.string.auth_fail),
            Snackbar.LENGTH_LONG
        )
            .setAction("Action", null).show()
    }

    override fun updateAuthUi(authList: ArrayList<Auth?>?) {
        this.authList = authList
        for (auth in this.authList!!) {
            if (auth != null) {
                firstViewModel.insertAuth(auth)
            }
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(this@AuthActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun showSnackbar(message: String) {
        Snackbar.make(mParentLayout!!, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    override fun getListProds(): List<Prod>? {
        return listProds
    }
}