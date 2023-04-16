package com.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ui.R

//トップ画面
@Composable
fun Top(onNavigateToExplanation: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.blackjack),
            contentDescription = "Blackjack",
            modifier = Modifier.padding(top = 100.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = stringResource(R.string.top_overview),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 23.dp, end = 20.dp)
        )
        Button(
            onClick = { onNavigateToExplanation() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 50.dp, top = 64.dp, end = 50.dp),
        ) {
            Text(
                text = stringResource(R.string.top_btn_explanation),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
        }
    }
}

//ルール説明画面
@Composable
fun Explanation(onNavigateToPlayer: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.explanation_rule),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp, top = 30.dp, end = 10.dp)
        )
        Button(
            onClick = { onNavigateToPlayer() },
            modifier = Modifier
                .padding(start = 50.dp, top = 53.dp, end = 50.dp)
                .fillMaxWidth(),
            content = {
                Text(
                    text = stringResource(id = R.string.explanation_btn_fight),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopScreenPreview() {
    Top {}
}

@Preview(showBackground = true)
@Composable
fun ExplanationScreenPreview() {
    Explanation {}
}