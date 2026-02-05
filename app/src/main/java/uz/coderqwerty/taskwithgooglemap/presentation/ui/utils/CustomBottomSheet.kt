package uz.coderqwerty.taskwithgooglemap.presentation.ui.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun CustomBottomSheet(
    sheetContent: @Composable ColumnScope.() -> Unit
) {
    val transitionState = remember { MutableTransitionState(false) }
    LaunchedEffect(Unit) { transitionState.targetState = true }

    // Scrim opacity animation (FADE ONLY)
    val scrimAlpha by animateFloatAsState(
        targetValue = if (transitionState.targetState) 0.5f else 0f,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )

    // SCRIM (always in composition)
    if (scrimAlpha > 0f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = scrimAlpha))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {}
        )
    }

    // BOTTOM SHEET (AnimatedVisibility only for sheet)
    AnimatedVisibility(
        visibleState = transitionState,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight },
            animationSpec = spring(
//                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(tween(150)),

        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> fullHeight },
            animationSpec = tween(250, easing = FastOutSlowInEasing)
        ) + fadeOut(tween(200))
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.BottomCenter)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .pointerInput(Unit) {
                        // Disable swipe
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                event.changes.forEach { it.consume() }
                            }
                        }
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                sheetContent()
            }
        }
    }
}