package com.firstbreadclient.compose.reg

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.ui.graphics.vector.ImageVector
import com.firstbreadclient.R

sealed class ComposeUnitConverterScreen(
    val route: String,
    @StringRes val label: Int,
    val icon: ImageVector
) {
    companion object {
        val screens = listOf(
            Temperature,
            Distances
        )

        const val route_temperature = "temperature"
        const val route_distances = "distances"
    }

    private object Temperature : ComposeUnitConverterScreen(
        route_temperature,
        R.string.clients,
        Icons.Default.AccountCircle
    )

    private object Distances : ComposeUnitConverterScreen(
        route_distances,
        R.string.action_refresh,
        Icons.Default.Call
    )
}