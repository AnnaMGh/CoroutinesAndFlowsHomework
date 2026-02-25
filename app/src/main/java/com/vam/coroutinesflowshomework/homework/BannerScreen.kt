package com.vam.coroutinesflowshomework.homework

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vam.coroutinesflowshomework.R
import com.vam.coroutinesflowshomework.ui.theme.CoroutinesFlowsHomeworkTheme
import java.time.ZonedDateTime

@Composable
fun BannerScreenRoot(modifier: Modifier = Modifier) {

    val appContext = LocalContext.current.applicationContext
    val viewmodel = viewModel { BannerViewModel(appContext = appContext) }
    val isInternet = viewmodel.isInternet.collectAsStateWithLifecycle()
    val isLocationPermissionGranted =
        viewmodel.isLocationPermissionGranted.collectAsStateWithLifecycle()
    val logs = viewmodel.logs.collectAsStateWithLifecycle()

    BannerScreen(
        modifier,
        isInternet.value,
        isLocationPermissionGranted.value,
        logs.value,
        viewmodel::onAction
    )
}

@Composable
fun BannerScreen(
    modifier: Modifier = Modifier,
    isInternet: Boolean,
    isLocationPermissionGranted: Boolean,
    logs: List<NetworkLog>,
    onAction: (BannerAction) -> Unit
) {

    if (!isLocationPermissionGranted) {
        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result: Map<String, Boolean> ->

                val coarseGranted = result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                val fineGranted = result[Manifest.permission.ACCESS_FINE_LOCATION] == true

                onAction(BannerAction.OnLocationPermissionResponse(coarseGranted && fineGranted))
            }

        LaunchedEffect(Unit) {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(items = logs, key = { it.timestamp.value }) { log ->
                NetworkLogCell(log)
            }
        }

        if (!isInternet) {
            Banner()
        }
    }
}

@Composable
fun Banner(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(color = Color.Red, shape = RoundedCornerShape(5.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.warning_24dp),
            contentDescription = null,
            tint = Color.White
        )
        Text(text = "No internet connection!", color = Color.White)
    }
}

@Composable
fun NetworkLogCell(log: NetworkLog) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp)
    ) {
        Text(text = log.timestamp.formattedValue, color = MaterialTheme.colorScheme.onSecondary)
        Text(
            text = "Internet ${if (log.isNetworkAvailable) "on" else "off"}",
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            text = "Location: ${log.latitude} latitude, ${log.longitude} longitude",
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Preview
@Composable
fun BannerScreenPreview() {

    CoroutinesFlowsHomeworkTheme {
        val time = ZonedDateTime.now()
        val log = NetworkLog(
            DisplayableTimestamp(time, time.toDisplayableValue()),
            true,
            44.00,
            32.99
        )

        BannerScreen(
            isInternet = true,
            isLocationPermissionGranted = true,
            logs = listOf(log),
            onAction = {})
    }

}
