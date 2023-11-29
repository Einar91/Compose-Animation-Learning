package com.example.animationdemo_v2.animations

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.animationdemo_v2.HomeScreenDrawer
import com.example.animationdemo_v2.ScreenContents

/**
 * Animasjons demo versjon 2, her bruker jeg en ´transition´, den koordinerer state update som skjer
 * samtidig, basert på en tilstand/state. Benyttes altså for animasjoner som ikke er uavhengige, men
 * skjer samtidig.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationDemoV2(
    modifier: Modifier = Modifier
) {
    val width = LocalConfiguration.current.screenWidthDp
    val drawerWidth = width.toFloat() * 1.5f

    Box(modifier) {
        var drawerState by remember {
            mutableStateOf(DrawerValue.Closed)
        }
        val transition = updateTransition(targetState = drawerState, label = "transition")
        val spec = spring<Float>(
            dampingRatio = if (drawerState == DrawerValue.Open) Spring.DampingRatioMediumBouncy else Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessLow
        )

        val translation by transition.animateFloat(
            transitionSpec = { spec },
            label = "translation"
        ) { state ->
            when (state) {
                DrawerValue.Open -> drawerWidth
                DrawerValue.Closed -> 0f
            }

        }

        val scale by transition.animateFloat(
            transitionSpec = { spec },
            label = "scale"
        ) { state ->
            when (state) {
                DrawerValue.Open -> 0.8f
                DrawerValue.Closed -> 1f
            }

        }

        val cornerSize by transition.animateDp(
            label = "corner size"
        ) { state ->
            when (state) {
                DrawerValue.Open -> 32.dp
                DrawerValue.Closed -> 0.dp
            }
        }

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