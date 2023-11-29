package com.example.animationdemo_v2

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.launch

/**
 * Animasjons demo versjon 3.
 *
 * Her bruker vi Animatable (translationX) som vi oppdaterer med en gesture.
 *
 * Lerp (Linear interpolation) er en teknikk som "eases" overgangen mellom to verdier over tid.
 * - Altså vil den her kalkulere hvor overgangen skal ligge mellom 1f og 0.8f basert på
 *   (translationX.value / drawerWidth).
 *
 * I rememberDraggableState bruker vi .snapTo siden vi ikke ønsker noen animasjon når bruker
 * interagerer med en gesture. Vi bruker så .draggable modifieren med en restriksjon på kun
 * horisontal bevegelse.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimationDemoV3(
    modifier: Modifier = Modifier
) {
    val width = LocalConfiguration.current.screenWidthDp
    val drawerWidth = width.toFloat() * 1.5f

    val corutineScope = rememberCoroutineScope()

    Box(modifier) {
        var drawerState by remember {
            mutableStateOf(DrawerValue.Closed)
        }
        val translationX = remember {
            Animatable(0f)
        }

        val draggableState = rememberDraggableState(onDelta = { dragAmount ->
            corutineScope.launch {
                translationX.snapTo(translationX.value + dragAmount)
            }
        })


        HomeScreenDrawer()
        ScreenContents(
            onMenuClick = { drawerState = if (drawerState == DrawerValue.Closed) DrawerValue.Open else DrawerValue.Closed },
            modifier = Modifier
                .graphicsLayer {
                    this.translationX = translationX.value

                    val scale = lerp(1f, 0.8f, translationX.value / drawerWidth)
                    this.scaleX = scale
                    this.scaleY = scale

                    val cornerSize = lerp(0.dp, 32.dp, translationX.value / drawerWidth)
                    this.clip = true
                    this.shape = RoundedCornerShape(if (cornerSize >= 0.dp) cornerSize else 0.dp)

                }
                .draggable(draggableState, Orientation.Horizontal)

        )
    }
}