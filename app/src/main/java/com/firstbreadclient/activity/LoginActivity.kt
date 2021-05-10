package com.firstbreadclient.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.firstbreadclient.R
import com.firstbreadclient.network.LoginRetrofitInstance
import com.firstbreadclient.network.result.LoginResult
import com.firstbreadclient.network.result.UIEvent
import com.firstbreadclient.network.security.Authorization
import com.firstbreadclient.network.security.Registration
import com.firstbreadclient.service.LoginService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class LoginActivity : AppCompatActivity() {
    private var mLoginLayout: TextInputLayout? = null
    private var mPasswordLayout: TextInputLayout? = null
    private var mCntkodEdit: TextInputEditText? = null
    private var mPasswordEdit: TextInputEditText? = null
    private var mLoginButton: FloatingActionButton? = null
    private var mService: LoginService? = null
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

        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)

        mService = LoginRetrofitInstance.retrofitInstance?.create(LoginService::class.java)

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
                    mService!!.signin(Registration(mCntkodEdit!!.text.toString(), mPasswordEdit!!.text.toString()))!!
                            .subscribeOn(Schedulers.io())
                            .map { n: Authorization ->
                                if (n.tokenType == "Bearer")
                                    return@map LoginResult(false, "") else
                                    return@map LoginResult(true, "Неверный пароль!")
                            }
                            .onErrorReturnItem(LoginResult(true, "Сетевая ошибка"))
                }
                .observeOn(AndroidSchedulers.mainThread())

        mLoginCompositeDisposable.add(loginObservable.subscribe { be: LoginResult ->
            if (be.hasError) {
                mPasswordLayout!!.error = "Неверный пароль!"
            } else {
                mLoginLayout!!.isEnabled = true
                mPasswordLayout!!.isEnabled = true
            }
        })
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
}