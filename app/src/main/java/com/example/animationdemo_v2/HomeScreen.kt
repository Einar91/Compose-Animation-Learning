package com.example.animationdemo_v2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun HomeScreen(
    navToSecond: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)) {
        Text(text = "Hello")
        Button(onClick = navToSecond) {
            Text(text = "Nav")
        }
    }
    
}