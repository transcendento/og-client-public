package com.firstbreadclient.compose.reg

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.firstbreadclient.room.FirstViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistancesConverter(
    viewModel: FirstViewModel
) {
    var cnt by rememberSaveable { mutableStateOf("") }

    Row(modifier = Modifier.padding(bottom = 16.dp)) {
        OutlinedTextField(value = cnt, onValueChange = { cnt = it })
    }
}