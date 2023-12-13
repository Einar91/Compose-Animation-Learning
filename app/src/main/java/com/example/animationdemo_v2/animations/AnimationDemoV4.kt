package com.example.animationdemo_v2.animations

import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.animationdemo_v2.HomeScreen
import com.example.animationdemo_v2.HomeScreenDrawer
import com.example.animationdemo_v2.ScreenContents
import com.example.animationdemo_v2.SecondScreen
import kotlinx.coroutines.launch

/**
 * Animasjons demo versjon 4.
 *
 * I steden for å bruke .draggable som i v3, bruker vi her DraggableAchors, som tar automatisk av
 * mye av det vi måtte spesifisere manuelt i v3.
 *
 *
 */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AnimationDemoV4(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val width = LocalConfiguration.current.screenWidthDp
    val drawerWidth = width.toFloat() * 1.5f

    val corutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    Box(modifier) {
        var drawerState by remember {
            mutableStateOf(DrawerValue.Closed)
        }

        val anchors = DraggableAnchors {
            DrawerValue.Open at drawerWidth
            DrawerValue.Closed at 0f
        }
        val state = remember {
            AnchoredDraggableState(
                initialValue = DrawerValue.Closed,
                anchors = anchors,
                positionalThreshold = { distance: Float -> distance * 0.5f },
                animationSpec = spring(),
                velocityThreshold = { with(density) { 80.dp.toPx() } },
                confirmValueChange = { draw ->
                    drawerState = draw
                    true
                }

            )
        }

        val toggleDrawerState: () -> Unit = {
            corutineScope.launch {
                drawerState = if (drawerState == DrawerValue.Open) {
                    state.animateTo(DrawerValue.Closed)
                    DrawerValue.Closed
                } else {
                    state.animateTo(DrawerValue.Open)
                    DrawerValue.Open
                }
            }
        }

        HomeScreenDrawer(
            onDismiss = toggleDrawerState,
            navToSecond = {navController.navigate("second")}
        )
        ScreenContents(
            onMenuClick = toggleDrawerState,
            modifier = Modifier
                .graphicsLayer {
                    this.translationX = state.requireOffset()

                    val scale = lerp(1f, 0.8f, state.requireOffset() / drawerWidth)
                    this.scaleX = scale
                    this.scaleY = scale

                    val cornerSize = lerp(0.dp, 32.dp, state.requireOffset() / drawerWidth)
                    this.clip = true
                    this.shape = RoundedCornerShape(if (cornerSize >= 0.dp) cornerSize else 0.dp)
                }
                .anchoredDraggable(state, Orientation.Horizontal),
            content = {
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = modifier
                ) {

                    composable(route = "home") {
                        HomeScreen(
                            navToSecond = { navController.navigate("second") }
                        )
                    }
                    composable(route = "second") {
                        SecondScreen()
                    }

                }
            }
        )
    }
}