package com.vam.coroutinesflowshomework

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vam.coroutinesflowshomework.homework2.MoneyTransferScreen
import com.vam.coroutinesflowshomework.homework2.MoneyTransferViewModel
import com.vam.coroutinesflowshomework.ui.theme.CoroutinesFlowsHomeworkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoroutinesFlowsHomeworkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    (applicationContext as? MyApplication)?.applicationScope?.let { applicationCoroutine ->
                        val viewModel: MoneyTransferViewModel = viewModel<MoneyTransferViewModel> {
                            MoneyTransferViewModel(applicationCoroutine)
                        }

                        MoneyTransferScreen(
                            state = viewModel.state,
                            onAction = viewModel::onAction
                        )
                    }
                }
            }
        }
    }
}

