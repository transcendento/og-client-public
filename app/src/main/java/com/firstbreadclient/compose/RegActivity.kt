package com.firstbreadclient.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.firstbreadclient.compose.reg.RegScreen
import com.firstbreadclient.compose.ui.theme.OgclientpublicTheme
import com.firstbreadclient.mvp.AppModuleReg
import com.firstbreadclient.mvp.DaggerAppComponentReg
import com.firstbreadclient.mvp.presenter.RegPresenter
import com.firstbreadclient.mvp.view.RegView
import com.firstbreadclient.network.listener.AuthenticationListener
import com.firstbreadclient.network.listener.InternetConnectionListener
import com.firstbreadclient.network.security.Registration
import com.firstbreadclient.room.FirstApplication
import com.firstbreadclient.room.FirstViewModel
import com.firstbreadclient.room.FirstViewModelFactory
import javax.inject.Inject

class RegActivity : ComponentActivity(), InternetConnectionListener, AuthenticationListener, RegView {
    @Inject
    lateinit var regPresenter: RegPresenter

    private val firstViewModel: FirstViewModel by viewModels {
        FirstViewModelFactory((application as FirstApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAppComponentReg.builder().appModuleReg(AppModuleReg(this)).build().inject(this)

        super.onCreate(savedInstanceState)
        val text = mutableStateOf("some text")
        setContent {
            OgclientpublicTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
Column {
    RegScreen (firstViewModel) {registration: Registration -> registration }
}
                }
            }
/*
            RegScreen(
                text
            ) { newText ->
                text.value = newText
            }
*/

            //RegScreen {registration: Registration -> regPresenter.signup(registration) }

        }
    }



    override fun showToast(message: String) {
        TODO("Not yet implemented")
    }

    override fun showSnackbar(message: String) {
        TODO("Not yet implemented")
    }

    override fun onUserLoggedOut() {
        TODO("Not yet implemented")
    }

    override fun onInternetUnavailable() {
        TODO("Not yet implemented")
    }


}

/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OgclientpublicTheme {
        Greeting("Android")
    }
}*/
