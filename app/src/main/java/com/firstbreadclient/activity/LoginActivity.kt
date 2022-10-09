package com.firstbreadclient.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.firstbreadclient.R
import com.firstbreadclient.network.LoginRetrofitInstance
import com.firstbreadclient.network.OkHttpClientInstance
import com.firstbreadclient.network.listener.AuthenticationListener
import com.firstbreadclient.network.listener.InternetConnectionListener
import com.firstbreadclient.network.result.LoginResult
import com.firstbreadclient.network.result.UIEvent
import com.firstbreadclient.network.security.Authorization
import com.firstbreadclient.network.security.Registration
import com.firstbreadclient.service.LoginService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class LoginActivity : AppCompatActivity(), InternetConnectionListener, AuthenticationListener {
    private var mLoginLayout: TextInputLayout? = null
    private var mPasswordLayout: TextInputLayout? = null
    private var mCntkodEdit: TextInputEditText? = null
    private var mPasswordEdit: TextInputEditText? = null
    private var mLoginButton: FloatingActionButton? = null
    private var mLoginService: LoginService? = null
    private var mParentLayout: View? = null
    private var mAuthToolBar: Toolbar? = null

    private val mEditCompositeDisposable = CompositeDisposable()
    private val mLoginCompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)

        mParentLayout = findViewById(android.R.id.content)
        mLoginLayout = findViewById(R.id.loginLayout)
        mCntkodEdit = findViewById(R.id.cntkodEditText)
        mPasswordLayout = findViewById(R.id.passwordLayout)
        mPasswordEdit = findViewById(R.id.passwordEditText)
        mLoginButton = findViewById(R.id.loginButton)
        mAuthToolBar = findViewById(R.id.authToolbar)

        mAuthToolBar?.title = "Первый хлеб"
        mAuthToolBar?.subtitle = "Регистрация"

        setSupportActionBar(mAuthToolBar)

        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)

        mLoginService = LoginRetrofitInstance.retrofitInstance?.create(LoginService::class.java)

        OkHttpClientInstance.setInternetConnectionListener(this)
        OkHttpClientInstance.setAuthenticationListener(mLoginService, this)

        val userNameObservable = RxTextView.textChanges(mCntkodEdit!!)
            .map { obj: CharSequence -> obj.toString() }
            .map { obj: String -> obj.trim { it <= ' ' } }

        val passwordObservable = RxTextView.textChanges(mPasswordEdit!!)
            .map { obj: CharSequence -> obj.toString() }
            .map { obj: String -> obj.trim { it <= ' ' } }

        val uiResultObservable = Observable.combineLatest(
            userNameObservable,
            passwordObservable, { cntkod: String?, password: String? -> UIEvent(cntkod, password) })

        mEditCompositeDisposable.add(
            uiResultObservable.subscribe { ui: UIEvent ->
                if (ui.cntkod?.length in 1..3) {
                    mLoginLayout!!.error = "Код клиента не менее 4 символов"
                } else {
                    mLoginLayout!!.error = null
                }
                if (ui.password?.length!! < 4 && ui.cntkod?.length!! > 0) {
                    mPasswordLayout!!.error = "Пароль не менее 4 символов"
                } else {
                    mPasswordLayout!!.error = null
                }
                mLoginButton!!.isEnabled = ui.cntkod?.length!! >= 4 && ui.password?.length!! >= 4
            }
        )

        val loginObservable = RxView.clicks(mLoginButton!!)
            .flatMap {
                mLoginService!!.signin(
                    Registration(
                        mCntkodEdit!!.text.toString(), mPasswordEdit!!.text.toString()
                    )
                )!!
                    .subscribeOn(Schedulers.io())
                    .map { n: Authorization ->
                        if (n.tokenType == "Bearer") {
                            Log.i("token type ", n.tokenType.toString())
                            return@map LoginResult(false, n.accessToken)
                        } else {
                            Log.e("token type error ", n.tokenType.toString())
                            return@map LoginResult(true, "Неверный пароль!")
                        }
                    }
                    .onErrorReturnItem(LoginResult(true, "Сетевая ошибка"))
            }
            .observeOn(AndroidSchedulers.mainThread())

        mLoginCompositeDisposable.add(loginObservable.subscribe { be: LoginResult ->
            if (be.hasError) {
                Log.e("pass error ", be.hasError.toString())
                mPasswordLayout!!.error = "Неверный пароль!"
            } else {
                mLoginLayout!!.isEnabled = true
                mPasswordLayout!!.isEnabled = true

                showToast(resources.getString(R.string.auth_ok))

                with(OkHttpClientInstance.getSession()) {
                    this?.saveCntkod(mCntkodEdit!!.text.toString())
                    this?.savePassword(mPasswordEdit!!.text.toString())
                    this?.saveToken("Bearer " + be.message)
                }

                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
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

}