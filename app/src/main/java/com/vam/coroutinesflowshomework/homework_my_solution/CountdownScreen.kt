package com.vam.coroutinesflowshomework.homework_my_solution

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vam.coroutinesflowshomework.ui.theme.CoroutinesFlowsHomeworkTheme


@Composable
fun CountdownScreenRoot(
    modifier: Modifier = Modifier,
) {
    val viewModel = viewModel { CountDownViewModel() }
    val state by viewModel.state.collectAsStateWithLifecycle()

    CountdownScreen(
        modifier = modifier,
        countdown = state.countdown,
        isStarted = state.isStarted,
        onAction = viewModel::onAction
    )
}

@Composable
fun CountdownScreen(
    modifier: Modifier = Modifier,
    countdown: Int,
    isStarted: Boolean,
    onAction: (CountdownAction) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = countdown.toString())

        if (!isStarted)
            Button(onClick = { onAction(CountdownAction.OnActionStartCountdown) }) {
                Text(text = "Start Countdown")
            }
    }
}

@Preview
@Composable
fun CountdownScreenPreview() {
    CoroutinesFlowsHomeworkTheme {
        CountdownScreenRoot()
    }
}