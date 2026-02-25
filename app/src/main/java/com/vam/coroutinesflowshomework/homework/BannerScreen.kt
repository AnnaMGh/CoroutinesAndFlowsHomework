package com.vam.coroutinesflowshomework.homework

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vam.coroutinesflowshomework.R
import com.vam.coroutinesflowshomework.ui.theme.CoroutinesFlowsHomeworkTheme

@Composable
fun BannerScreenRoot(modifier: Modifier = Modifier) {

    val appContext = LocalContext.current.applicationContext
    val viewmodel = viewModel { BannerViewModel(appContext = appContext) }
    val isInternet = viewmodel.isInternet.collectAsStateWithLifecycle()

    BannerScreen(modifier, isInternet.value)
}

@Composable
fun BannerScreen(modifier: Modifier = Modifier, isInternet: Boolean) {
    Box(modifier = modifier.fillMaxSize()) {
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
            .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(5.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(painter = painterResource(R.drawable.warning_24dp), contentDescription = null)
        Text(text = "No internet connection!")
    }
}

@Preview
@Composable
fun BannerScreenPreview() {

    CoroutinesFlowsHomeworkTheme {
        BannerScreen(isInternet = true)
    }

}
