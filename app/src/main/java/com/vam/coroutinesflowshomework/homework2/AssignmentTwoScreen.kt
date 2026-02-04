package com.vam.coroutinesflowshomework.homework2

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AssignmentTwoScreen(modifier: Modifier = Modifier) {
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var backgroundColor by remember {
        mutableStateOf(Color.White)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        photoUri = it
    }

    LaunchedEffect(photoUri) {

        val uri = photoUri ?: return@LaunchedEffect

        isLoading = true

        // Input Stream - Dispatchers.IO
        val bitmap = withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri)?.use { input ->
                BitmapFactory.decodeStream(input)
            }
        }

        if (bitmap != null) {
            // Heavy CPU work - Dispatchers.Default
            val dominantColor = withContext(Dispatchers.Default) {
                PhotoProcessor.findDominantColor(bitmap)
            }

            backgroundColor = Color(dominantColor)
        }

        isLoading = false
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
    ) {
        RotatingBoxScreen()
        Spacer(modifier = Modifier.height(64.dp))
        Button(onClick = {
            scope.launch {
                imagePicker.launch(
                    input = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
            }
        }) {
            Text(text = "Pick Image")
        }
        if (isLoading) {
            Text(text = "Finding dominant color...")
        }

        if (photoUri != null) {
            val painter = rememberAsyncImagePainter(
                ImageRequest
                    .Builder(context)
                    .data(data = photoUri)
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = null
            )
        }
    }
}