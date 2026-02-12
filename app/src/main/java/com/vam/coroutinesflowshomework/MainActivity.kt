package com.vam.coroutinesflowshomework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.vam.coroutinesflowshomework.homework.LeaderboardScreenRoot
import com.vam.coroutinesflowshomework.ui.theme.CoroutinesFlowsHomeworkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoroutinesFlowsHomeworkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LeaderboardScreenRoot(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

