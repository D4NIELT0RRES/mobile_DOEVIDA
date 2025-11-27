package com.example.doevida.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R
import kotlinx.coroutines.delay

@Composable
fun TelaSplash(navController: NavController) {
    val scale = remember { Animatable(0.5f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = 0.5f,
                stiffness = 150f
            )
        )
        delay(1200)
        navController.navigate("tela_inicial") {
            popUpTo("splash") { inclusive = true } // Corrigido para bater com a rota no MainActivity
        }
    }

    TelaSplashContent(scale.value)
}

@Composable
private fun TelaSplashContent(scale: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logoo),
            contentDescription = "Logo DoeVida",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(220.dp)
                .scale(scale)
                // Tenta recortar para remover possíveis bordas quadradas brancas
                // .clip(CircleShape) 
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TelaSplashPreview() {
    TelaSplashContent(scale = 1f)
}
