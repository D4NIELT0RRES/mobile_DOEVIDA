package com.example.doevida.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R
import kotlinx.coroutines.delay

@Composable
fun TelaSplash(navController: NavController) {
    TelaSplashContent(navController = navController)
}

@Composable
private fun TelaSplashContent(navController: NavController) {
    val scale = remember { Animatable(0.6f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600)
        )

        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = tween(
                durationMillis = 1200,
                easing = { overshootInterpolator(it) }
            )
        )

        delay(800)

        scale.animateTo(
            targetValue = 6f,
            animationSpec = tween(durationMillis = 600)
        )

        alpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 400)
        )

        navController.navigate("tela_inicial") {
            popUpTo("tela_splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logoo),
            contentDescription = "Logo DoeVida",
            modifier = Modifier
                .size(200.dp)
                .scale(scale.value)
                .alpha(alpha.value)
        )
    }
}

fun overshootInterpolator(fraction: Float): Float {
    val tension = 2f
    return (fraction - 1f) * (fraction - 1f) *
            ((tension + 1f) * (fraction - 1f) + tension) + 1f
}

@Preview(showSystemUi = true)
@Composable
fun TelaSplashPreview() {
    TelaSplashContent(navController = rememberNavController())
}