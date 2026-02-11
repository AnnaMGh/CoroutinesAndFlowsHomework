package com.vam.coroutinesflowshomework


import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.vam.coroutinesflowshomework.homework.BiometricPromptManager
import com.vam.coroutinesflowshomework.homework.BiometricResult
import com.vam.coroutinesflowshomework.ui.theme.CoroutinesFlowsHomeworkTheme
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.cancellation.CancellationException

private const val AUTHENTICATION_TIMEOUT_MILLIS = 3000L

@RequiresApi(Build.VERSION_CODES.P)
class MainActivity : AppCompatActivity() {

    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        BiometricPromptManager(this)
        setContent {
            CoroutinesFlowsHomeworkTheme {
                val context = LocalContext.current
                var biometricsResult by remember {
                    mutableStateOf<BiometricResult?>(null)
                }

                LaunchedEffect(biometricsResult) {
                    if (biometricsResult != null) {
                        Toast.makeText(context, biometricsResult.toString(), Toast.LENGTH_LONG)
                            .show()
                        biometricsResult = null
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Button(onClick = {
                        lifecycleScope.launch {
                            launch {
                                val result = withTimeoutOrNull(AUTHENTICATION_TIMEOUT_MILLIS) {
                                    try {
                                        biometricsResult = promptManager.showBiometricPrompt(
                                            title = "Authentication",
                                            description = "Please authenticate to proceed"
                                        )
                                    } catch (e: Exception) {
                                        if (e is CancellationException) {
                                            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG)
                                                .show()
                                            throw e
                                        }
                                        e.printStackTrace()
                                    }
                                }
                                if (result == null) {
                                    println("Timeout reached, cancelling...")
                                    lifecycleScope.cancelChildren()
                                }
                            }
                        }
                    }) {
                        Text(text = "Authenticate")
                    }
                }
            }
        }
    }
}

fun LifecycleCoroutineScope.cancelChildren() {
    this.coroutineContext.cancelChildren()
}