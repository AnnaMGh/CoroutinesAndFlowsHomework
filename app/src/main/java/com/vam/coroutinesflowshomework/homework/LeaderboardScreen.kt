package com.vam.coroutinesflowshomework.homework

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vam.coroutinesflowshomework.ui.theme.CoroutinesFlowsHomeworkTheme


@Composable
fun LeaderboardScreenRoot(modifier: Modifier = Modifier) {

    val viewModel = viewModel { LeaderboardViewModel() }
    val state by viewModel.state.collectAsStateWithLifecycle()

    LeaderboardScreen(
        modifier = modifier,
        allScores = state.topThree,
        isFinished = state.isFinished,
        isRegistered = state.isRegistered,
        onAction = viewModel::onAction
    )
}

@Composable
fun LeaderboardScreen(
    modifier: Modifier = Modifier,
    allScores: List<Pair<String, Int>>,
    isFinished: Boolean,
    isRegistered: Boolean,
    onAction: (LeaderboardAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = "LEADERBOARD",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )


        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .weight(1f)
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            items(allScores) { player ->
                LeaderboardCell(
                    place = allScores.indexOf(player) + 1,
                    name = player.first,
                    score = player.second
                )
            }
        }

        if (isFinished) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Game finished",
                color = MaterialTheme.colorScheme.onSecondary
            )
        } else {
            Button(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    if (isRegistered) onAction(LeaderboardAction.OnUnregister)
                    else onAction(LeaderboardAction.OnRegister)
                }) {
                Text(
                    text = if (isRegistered) "Unregister" else "Register",
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}

@Composable
fun LeaderboardCell(modifier: Modifier = Modifier, place: Int, name: String, score: Int) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "$place. ",
            color = MaterialTheme.colorScheme.onSecondary,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = name,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center

        )
        Text(
            modifier = Modifier.weight(1f),
            text = score.toString(),
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.End
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LeaderboardScreenPreview() {
    CoroutinesFlowsHomeworkTheme {
        LeaderboardScreenRoot()
    }
}