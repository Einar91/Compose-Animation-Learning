package com.example.animationdemo_v2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SecondScreen() {
    Column(Modifier.fillMaxSize().background(Color.Magenta)) {
        Text(text = "Ello")
    }
}