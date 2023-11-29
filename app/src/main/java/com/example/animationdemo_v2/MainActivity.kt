package com.example.animationdemo_v2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.animationdemo_v2.animations.AnimationDemoV3
import com.example.animationdemo_v2.animations.AnimationDemoV4
import com.example.animationdemo_v2.ui.theme.AnimationDemo_v2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationDemo_v2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //AnimationDemoV1(Modifier.fillMaxSize())
                    //AnimationDemoV2(Modifier.fillMaxSize())
                    //AnimationDemoV3(Modifier.fillMaxSize())
                    AnimationDemoV4(Modifier.fillMaxSize())
                }
            }
        }
    }
}