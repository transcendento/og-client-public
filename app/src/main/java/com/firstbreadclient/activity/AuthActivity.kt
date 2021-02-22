package com.firstbreadclient.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firstbreadclient.R
import com.firstbreadclient.adapter.AuthAdapter
import com.firstbreadclient.adapter.ItemClickListener
import com.firstbreadclient.eventbus.NetworkEvent
import com.firstbreadclient.firebase.FirebaseFunctionUtils
import com.firstbreadclient.firebase.OnGetServerTime
import com.firstbreadclient.model.data.Auth
import com.firstbreadclient.model.FirstModel
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

class AuthActivity : AppCompatActivity(), ItemClickListener, InternetConnectionListener, AuthenticationListener, AuthView {
    private var mDataBundle: Bundle? = null
    private var mAuthList: ArrayList<Auth?>? = null
    private var mAuthViewModel: FirstModel? = null

    private val firstViewModel: FirstViewModel by viewModels {
        FirstViewModelFactory((application as FirstApplication).repository)
    }

    //private var mService: GetDataService? = null
    //private var mLoginService: LoginService? = null
    private var mDateOrder: String? = null
    private var mParentLayout: View? = null
    //private val loginCompositeDisposable = CompositeDisposable()

    @Inject
    lateinit var authPresenter: AuthPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        //(applicationContext as App).appComponent.inject(this)
        DaggerAppComponent.builder().appModule(AppModule(this)).build().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_main)

        mDataBundle = Bundle()

        //val authInteractor = AuthInteractorImpl(this,this)
        //authPresenter = AuthPresenter(authInteractor)
        authPresenter.bind(this)
/*
        mService = RetrofitInstance.retrofitInstance?.create(GetDataService::class.java)
        mLoginService = LoginRetrofitInstance.retrofitInstance?.create(LoginService::class.java)

        OkHttpClientInstance.setInternetConnectionListener(this)
        OkHttpClientInstance.setAuthenticationListener(mLoginService, this)
*/
        val toolbar = findViewById<Toolbar>(R.id.authToolbar)
        mParentLayout = findViewById(android.R.id.content)

        toolbar.title = "Первый хлеб"
        toolbar.subtitle = "Клиенты"

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

/*
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAuth)
        val authAdapter = AuthAdapter(this)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@AuthActivity)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = authAdapter

        authAdapter.setClickListener(this)
*/



        //OkHttpClientInstance.getSession()?.saveCntkod("10171")
        //OkHttpClientInstance.getSession()?.savePassword("1017")

        OkHttpClientInstance.getSession()?.saveCntkod("1158")
        OkHttpClientInstance.getSession()?.savePassword("1111")

        authPresenter.authToken()

        // Get a new or existing ViewModel from the ViewModelProvider.
        //mAuthViewModel = ViewModelProviders.of(this).get(RoomModel::class.java)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        // Update the cached copy of the words in the adapter.
        //mAuthViewModel!!.allAuths.observe(this, { auths: List<Auth> -> authAdapter.setAuths(auths) })
        /*firstViewModel.allAuths.observe(owner = this) { auths ->
            // Update the cached copy of the words in the adapter.
            auths.let { authAdapter.setAuths(it) }
        }
*/


        val fabAuth = findViewById<FloatingActionButton>(R.id.FloatingActionButtonAuth)
        fabAuth.setOnClickListener {
/*
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
*/
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

/*
    private val authToken: Unit
        get() {
            val userNameObservable = Observable.just(OkHttpClientInstance.getSession()?.getCntkod())
            val passwordObservable = Observable.just(OkHttpClientInstance.getSession()?.getPassword())

            val uiResultObservable = Observable.combineLatest(
                    userNameObservable,
                    passwordObservable, { cntkod: String?, password: String? -> UIEvent(cntkod, password) })

            val loginObservable = uiResultObservable
                    .flatMap { log: UIEvent ->
                        mLoginService!!.signin(Registration(log.cntkod, log.password))!!

                                .subscribeOn(Schedulers.io())
                                .map { n: Authorization ->
                                    if (n.tokenType == "Bearer")
                                        return@map LoginResult(false, n.accessToken) else
                                        return@map LoginResult(true, "Ошибка авторизации")
                                }
                                .onErrorReturnItem(LoginResult(true, "Сетевая ошибка"))
                    }
                    .observeOn(AndroidSchedulers.mainThread())

            loginCompositeDisposable.add(loginObservable.subscribe { be: LoginResult ->
                if (be.hasError) {
                    be.message?.let {
                        Snackbar.make(mParentLayout!!, it, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
                    }
                } else {
                    OkHttpClientInstance.getSession()?.saveToken("Bearer " + be.message)
                    authData
                }
            })
        }

    private val authData: Unit
        get() {
            val tokenAll = "c6ab9fc691cd7261deb541394e47bea4"

            val jwt = OkHttpClientInstance.getSession()?.getToken()

            val call = mService!!.getAuthData(jwt, tokenAll)

            Log.i("URL Called", call?.request()?.url.toString() + "")

            call?.enqueue(object : Callback<ArrayList<Auth?>?> {
                override fun onResponse(call: Call<ArrayList<Auth?>?>, response: Response<ArrayList<Auth?>?>) {
                    if (!response.isSuccessful) {
                        Toast.makeText(this@AuthActivity, "Ошибка соединения", Toast.LENGTH_SHORT).show()
                        return
                    }
                    mAuthList = response.body()
                    for (auth in Objects.requireNonNull(mAuthList)!!) {
                        mAuthViewModel!!.insertAuth(auth)
                    }
                }

                override fun onFailure(call: Call<ArrayList<Auth?>?>, t: Throwable) {
                    t.message?.let { Log.e("Error message", it) }
                    Toast.makeText(this@AuthActivity, "Something went wrong...Error message: " + t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
*/

    override fun onClick(view: View?, position: Int) {
        val auth = mAuthList!![position]
        val intent = Intent(this, OrderActivity::class.java)
        mDataBundle!!.putString("cntkod", auth?.cntkod)
        mDataBundle!!.putString("orderdate", mDateOrder)

        intent.putExtra("cntid", auth?.cntid)
        intent.putExtra("cntkod", auth?.cntkod)
        intent.putExtra("cntname", auth?.cntname)
        intent.putExtra("cntadres", auth?.cntadres)
        intent.putExtra("orderdate", mDateOrder)
        auth?.cntkod?.let { Log.i("cntkod", it) }
        startActivity(intent, mDataBundle)
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
        mAuthList = authList
        for (auth in mAuthList!!) {
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