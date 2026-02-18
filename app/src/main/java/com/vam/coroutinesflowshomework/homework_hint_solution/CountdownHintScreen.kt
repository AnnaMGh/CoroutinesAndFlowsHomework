package com.vam.coroutinesflowshomework.homework_hint_solution

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vam.coroutinesflowshomework.ui.theme.CoroutinesFlowsHomeworkTheme


@Composable
fun CountdownHintScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = viewModel { CountdownHintViewModel() }
    val state = viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (!state.value.isStarted) {
            TextField(
                value = state.value.counterDisplay,
                onValueChange = { counter ->
                    viewModel.onAction(CountdownHintAction.OnActionSetCounter(counter))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(textAlign = TextAlign.Center)
            )

            Button(onClick = {
                viewModel.onAction(CountdownHintAction.OnActionStartCountdown)

            }) {
                Text(text = "Start Countdown")
            }
        } else {
            Text(text = state.value.countdown.toString())
        }

    }
}

@Preview
@Composable
fun CountdownHintScreenPreview() {
    CoroutinesFlowsHomeworkTheme {
        CountdownHintScreen()
    }
}