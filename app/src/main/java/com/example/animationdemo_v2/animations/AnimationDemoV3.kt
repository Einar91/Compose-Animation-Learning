package com.example.animationdemo_v2.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.rememberSplineBasedDecay
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
import com.example.animationdemo_v2.HomeScreenDrawer
import com.example.animationdemo_v2.ScreenContents
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
 *
 * Vi bruker translationX.updateBounds() for å sette en øvre og nedre grense slik at vi ikke kan
 * dra elementet ut av skjermen.
 * - Vi kunne også ha ignorert drag amounts som får oss over eller under thresholdet som vil tillater
 *
 * For en fling gesture så må vi kalkulere den nåværedne velocity av en gesture og bruke den til å
 * avgjøre hva som skjer når dra gesturen slutter.
 * - rememberSplineBasedDecay() bruker vi til å kalkulere current velocity av en gesture
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
        translationX.updateBounds(0f, drawerWidth)

        val draggableState = rememberDraggableState(onDelta = { dragAmount ->
            corutineScope.launch {
                translationX.snapTo(translationX.value + dragAmount)
            }
        })

        val decay = rememberSplineBasedDecay<Float>()

        val toggleDrawerState: () -> Unit = {
            corutineScope.launch {
                drawerState = if (drawerState == DrawerValue.Open) {
                    translationX.animateTo(0f)
                    DrawerValue.Closed
                } else {
                    translationX.animateTo(drawerWidth)
                    DrawerValue.Open
                }
            }
        }

        HomeScreenDrawer()
        ScreenContents(
            onMenuClick = toggleDrawerState,
            content = {

            },
            modifier = Modifier
                .graphicsLayer {
                    this.translationX = translationX.value

                    val scale = lerp(1f, 0.8f, translationX.value / drawerWidth)
                    this.scaleX = scale
                    this.scaleY = scale

                    val cornerSize = lerp(0.dp, 32.dp, translationX.value / drawerWidth)
                    this.clip = true
                    this.shape = RoundedCornerShape(cornerSize)

                }
                .draggable(draggableState, Orientation.Horizontal,
                    onDragStopped = { velocity ->
                        // Kalkuler current velocity
                        val decayX = decay.calculateTargetValue(
                            translationX.value,
                            velocity
                        )
                        // Avgjør hvilken tilstand den går til
                        corutineScope.launch {
                            val targetX = if (decayX > drawerWidth * 0.5) drawerWidth else 0f
                            val canReachTargetWithDecay = (decayX > targetX && targetX == drawerWidth)

                            /**
                             * Om decayX er forbi midtpunktet, så vet vi at endepunktet/drawerWidth
                             * er der den vil ende opp.
                             * - Om decayX går forbi drawerWidth, så kan vi naturlig nå målet med
                             *   decay. Vi trenger ikke videre animasjon.
                             * - Om kommer rett forbi midtpunktet, så bruker vi animateTo som vil
                             *   ta seg av å øke eller minke velocity for å komme til ønsket mål
                             *   (start eller sluttpunkt)
                             */

                            /**
                             * Om decayX er forbi midtpunktet, så vet vi at endepunktet/drawerWidth
                             * er der den vil ende opp.
                             * - Om decayX går forbi drawerWidth, så kan vi naturlig nå målet med
                             *   decay. Vi trenger ikke videre animasjon.
                             * - Om kommer rett forbi midtpunktet, så bruker vi animateTo som vil
                             *   ta seg av å øke eller minke velocity for å komme til ønsket mål
                             *   (start eller sluttpunkt)
                             */
                            if (canReachTargetWithDecay) {
                                translationX.animateDecay(
                                    initialVelocity = velocity,
                                    animationSpec = decay
                                )
                            } else {
                                translationX.animateTo(targetX, initialVelocity = velocity)
                            }

                            // Oppdater drawerState
                            drawerState = if (targetX == drawerWidth) {
                                DrawerValue.Open
                            } else {
                                DrawerValue.Closed
                            }
                        }
                    })
        )
    }
}