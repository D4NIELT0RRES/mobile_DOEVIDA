package com.example.doevida.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R
import com.example.doevida.components.MenuInferior
import com.example.doevida.service.SharedPreferencesUtils

@Composable
fun TelaHome(navController: NavController) {
    val userName = remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        userName.value = SharedPreferencesUtils.getUserName(context)
        Log.d("TelaHome", "Nome: ${userName.value}")
    }

    Scaffold(
        bottomBar = { MenuInferior(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF7F7F7)) // Fundo levemente cinza
                .verticalScroll(rememberScrollState())
        ) {
            HomeHeader(userName = userName.value) {
                navController.navigate("tela_perfil")
            }
            Spacer(modifier = Modifier.height(16.dp))
            DonationCountdownCard()
            Spacer(modifier = Modifier.height(24.dp))
            ActionsGrid(navController = navController)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HomeHeader(userName: String, onProfileClick: () -> Unit) {
    val primaryColor = Color(0xFF990410)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Olá,", fontSize = 18.sp, color = Color.Gray)
            Text(
                text = userName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Image(
            painter = painterResource(id = R.drawable.logologin), // Usar um avatar do usuário aqui
            contentDescription = "Perfil",
            modifier = Modifier
                .size(55.dp)
                .clip(CircleShape)
                .border(2.dp, primaryColor, CircleShape)
                .clickable(onClick = onProfileClick)
        )
    }
}

@Composable
fun DonationCountdownCard() {
    val primaryColor = Color(0xFF990410)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = primaryColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "13", // TODO: Substituir por valor dinâmico
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "dias para a\npróxima doação",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 18.sp,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun ActionsGrid(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Ações Rápidas",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ActionCard("Agendar Doação", R.drawable.doarsangue, "tela_agendamento", navController)
            ActionCard("Hospitais", R.drawable.hospitais, "tela_hospitais", navController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ActionCard("Banco de Sangue", R.drawable.bancodesangue, "tela_banco_sangue", navController)
            ActionCard("Histórico", R.drawable.historicos, "tela_historico", navController)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ActionCard("Registrar Doação", R.drawable.doarsangue, "tela_registrar_doacao", navController)
        }
    }
}

@Composable
fun ActionCard(text: String, iconRes: Int, route: String, navController: NavController) {
    val primaryColor = Color(0xFF990410)
    val iconBackgroundColor = primaryColor.copy(alpha = 0.1f)

    Card(
        modifier = Modifier.size(width = 150.dp, height = 120.dp),
        onClick = { navController.navigate(route) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = text,
                    modifier = Modifier.size(28.dp),
                    tint = primaryColor
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                color = Color.DarkGray,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TelaHomePreview() {
    val navController = rememberNavController()
    TelaHome(navController = navController)
}
