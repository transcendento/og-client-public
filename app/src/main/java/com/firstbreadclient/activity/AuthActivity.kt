package com.firstbreadclient.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.firstbreadclient.R
import com.firstbreadclient.eventbus.NetworkEvent
import com.firstbreadclient.firebase.FirebaseFunctionUtils
import com.firstbreadclient.firebase.OnGetServerTime
import com.firstbreadclient.model.data.Auth
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import javax.inject.Inject
import androidx.lifecycle.Observer

class AuthActivity : AppCompatActivity(), InternetConnectionListener, AuthenticationListener, AuthView {
    private var mDataBundle: Bundle? = null
    private var authList: ArrayList<Auth?>? = null

    private val firstViewModel: FirstViewModel by viewModels {
        FirstViewModelFactory((application as FirstApplication).repository)
    }

    private var mDateOrder: String? = null
    private var mParentLayout: View? = null

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

        with(OkHttpClientInstance.getSession()) {
            this?.saveCntkod("1158")
            this?.savePassword("11111")
        }

        authPresenter.authToken()

        val fabAuth = findViewById<FloatingActionButton>(R.id.FloatingActionButtonAuth)
        fabAuth.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent, mDataBundle)
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
        Snackbar.make(mParentLayout!!, resources.getString(R.string.conn_fail), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
    }

    override fun onUserLoggedOut() {
        Snackbar.make(mParentLayout!!, resources.getString(R.string.auth_fail), Snackbar.LENGTH_LONG)
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
}