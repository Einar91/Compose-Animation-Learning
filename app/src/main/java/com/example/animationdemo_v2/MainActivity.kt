package com.example.animationdemo_v2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
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
                    AnimationDemo(Modifier.fillMaxSize())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationDemo(
    modifier: Modifier = Modifier
) {
    val width = LocalConfiguration.current.screenWidthDp
    val drawerWidth = width.toFloat() * 0.8f

    Box(modifier) {
        var drawerState by remember {
            mutableStateOf(DrawerValue.Closed)
        }

        val spec = spring<Float>(
            dampingRatio = if (drawerState == DrawerValue.Open) Spring.DampingRatioMediumBouncy else Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        )

        val translation by animateFloatAsState(
            targetValue = if (drawerState == DrawerValue.Open) drawerWidth else 0f,
            animationSpec = spec,
            label = ""
        )

        val scale by animateFloatAsState(
            targetValue = if (drawerState == DrawerValue.Open) 0.8f else 1f,
            animationSpec = spec,
            label = ""
        )

        val cornerSize by animateDpAsState(
            targetValue = if (drawerState == DrawerValue.Open) 32.dp else 0.dp,
            label = ""
        )

        HomeScreenDrawer()
        ScreenContents(
            onMenuClick = { drawerState = if (drawerState == DrawerValue.Closed) DrawerValue.Open else DrawerValue.Closed },
            modifier = Modifier
                .graphicsLayer {
                    this.translationX = translation
                    this.scaleX = scale
                    this.scaleY = scale

                    this.clip = true
                    this.shape = RoundedCornerShape(cornerSize)
                }

        )
    }
}


@Composable
fun HomeScreenDrawer(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Surface(
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = Color.Gray.copy(alpha = 0.8f)
            ) {
                Text(
                    text = "Home",
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                modifier = modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = Color.Gray.copy(alpha = 0.8f)
            ) {
                Text(
                    text = "Calendar",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ScreenContents(
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Gray),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.Black
                ),
            ) {
                Icon(Icons.Outlined.Menu, contentDescription = "menu")
            }
        }
    }
}