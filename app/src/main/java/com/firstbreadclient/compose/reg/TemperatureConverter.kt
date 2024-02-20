package com.firstbreadclient.compose.reg

//import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.firstbreadclient.R
import com.firstbreadclient.network.security.Registration
import com.firstbreadclient.room.FirstViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureConverter(
    viewModel: FirstViewModel,
    onClickRegButton: (registration: Registration) -> Unit
) {
    var cnt by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    Row(modifier = Modifier.padding(bottom = 16.dp)) {
        OutlinedTextField(value = cnt, onValueChange = { cnt = it })
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.reg_screen))

        OutlinedTextField(value = cnt, onValueChange = { cnt = it })
        OutlinedTextField(value = pass, onValueChange = { pass = it })

        Button(onClick = {
            coroutineScope.launch {
                onClickRegButton
                val snackBarResult = snackBarHostState.showSnackbar(
                    message = "Snackbar is here",
                    actionLabel = "Undo",
                    duration = SnackbarDuration.Short
                )
                when (snackBarResult) {
                    SnackbarResult.ActionPerformed -> {
                        Log.d("Snackbar", "Action Performed")
                    }

                    else -> {
                        Log.d("Snackbar", "Snackbar dismissed")
                    }
                }
            }

        }) {
            Text(text = "Show Snack Bar", color = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureTextField(

    modifier: Modifier = Modifier,

    viewModel: FirstViewModel
) {

}

@Composable
fun TemperatureScaleButtonGroup(

    modifier: Modifier = Modifier,

    ) {
    Row(modifier = modifier) {
        TemperatureRadioButton(

            resId = R.string.action_search,

            )
        TemperatureRadioButton(

            resId = R.string.action_send,

            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun TemperatureRadioButton(

    resId: Int,

    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = stringResource(resId),
            modifier = Modifier
                .padding(start = 8.dp)
        )
    }
}