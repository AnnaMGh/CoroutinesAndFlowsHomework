package com.vam.coroutinesflowshomework.homework

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vam.coroutinesflowshomework.ui.theme.CoroutinesFlowsHomeworkTheme
import kotlinx.coroutines.launch


@Composable
fun BirdSingRootScreen(modifier: Modifier = Modifier) {
    val viewModel = viewModel<BirdSingViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    BirdSingScreen(
        modifier = modifier,
        currentBird = state.currentBird,
        birds = state.birds,
        onAction = viewModel::onAction
    )
}

@Composable
fun BirdSingScreen(
    modifier: Modifier = Modifier,
    currentBird: BirdModel?,
    birds: List<BirdModel>,
    onAction: (BirdAction) -> Unit
) {

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (currentBird != null) {
            BirdComponent(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.5f),
                birdModel = currentBird
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items = birds, key = { it.id }) { bird ->
                Button(onClick = {
                    scope.launch { println(bird.song) }
                    onAction(BirdAction.Sing(bird.id))
                }) {
                    Icon(
                        painter = painterResource(bird.imgRes),
                        contentDescription = bird.song,
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Text(text = bird.song, color = MaterialTheme.colorScheme.onSecondary)
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun BirdSingScreenPreview() {
    CoroutinesFlowsHomeworkTheme {
        BirdSingScreen(
            currentBird = DummyData.birdsList[0],
            birds = DummyData.birdsList,
            onAction = {})
    }
}