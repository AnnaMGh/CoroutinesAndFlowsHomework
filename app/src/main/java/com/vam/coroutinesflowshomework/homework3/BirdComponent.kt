package com.vam.coroutinesflowshomework.homework3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vam.coroutinesflowshomework.ui.theme.CoroutinesFlowsHomeworkTheme

@Composable
fun BirdComponent(modifier: Modifier = Modifier, birdModel: BirdModel) {

    LaunchedEffect(birdModel.id) {
        println(birdModel.song)
    }

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(5.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            modifier = Modifier.scale(1.5f),
            painter = painterResource(birdModel.imgRes),
            contentDescription = birdModel.song,
            tint = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            text = birdModel.song,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Preview
@Composable
fun BirdPreview() {
    CoroutinesFlowsHomeworkTheme {
        BirdComponent(birdModel = DummyData.birdsList[0])
    }
}