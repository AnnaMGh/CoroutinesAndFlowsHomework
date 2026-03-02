package com.vam.coroutinesflowshomework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vam.coroutinesflowshomework.homework1.StandardDispatchers
import com.vam.coroutinesflowshomework.homework2.SearchScreen
import com.vam.coroutinesflowshomework.homework2.SearchViewModel
import com.vam.coroutinesflowshomework.ui.theme.CoroutinesFlowsHomeworkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoroutinesFlowsHomeworkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: SearchViewModel = viewModel { SearchViewModel(StandardDispatchers)}
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    SearchScreen(
                        state = state,
                        onSearchTextChange = viewModel::onSearchQueryChange
                    )
                }
            }
        }
    }
}

