package com.example.doevida.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R
import com.example.doevida.components.InfoRow
import com.example.doevida.components.MenuInferior
import com.example.doevida.service.SharedPreferencesUtils

@Composable
fun TelaPerfil(navController: NavController) {
    val userName = remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        userName.value = SharedPreferencesUtils.getUserName(context)
    }

    Scaffold(
        bottomBar = { MenuInferior(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF7F7F7)) // Fundo cinza claro
                .verticalScroll(rememberScrollState())
        ) {
            ProfileHeader(userName.value)
            Spacer(modifier = Modifier.height(24.dp))
            UserInfoCard()
            Spacer(modifier = Modifier.height(24.dp))
            OptionsMenu(navController = navController)
            Spacer(modifier = Modifier.height(24.dp))
            LogoutButton {
                navController.navigate("tela_inicial") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ProfileHeader(userName: String) {
    val primaryColor = Color(0xFF990410)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(primaryColor)
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logologin), // Usar um avatar do usuário aqui
            contentDescription = "Avatar do Usuário",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(4.dp, Color.White, CircleShape)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = userName,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "Doador de Sangue", // Subtítulo ou status
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun UserInfoCard() {
    var nome by rememberSaveable { mutableStateOf("Rafaella Toscano") }
    var email by rememberSaveable { mutableStateOf("rafaella@email.com") }
    var cpf by rememberSaveable { mutableStateOf("123.456.789-00") }
    var dataNascimento by rememberSaveable { mutableStateOf("01/01/2005") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Minhas Informações",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(16.dp))
            InfoRow(label = "Nome", value = nome)
            Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
            InfoRow(label = "E-mail", value = email)
            Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
            InfoRow(label = "CPF", value = cpf)
            Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
            InfoRow(label = "Data de Nascimento", value = dataNascimento)
        }
    }
}

@Composable
fun OptionsMenu(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Opções",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column {
                OptionItem("Certificados", painterResource(id = R.drawable.doarsangue), onClick = {
                    navController.navigate("tela_certificado")
                })
                Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
fun OptionItem(title: String, icon: Painter, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = icon, contentDescription = title, tint = Color(0xFF990410))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.DarkGray)
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
    }
}

@Composable
fun LogoutButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color(0xFF990410).copy(alpha = 0.7f)),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF990410))
    ) {
        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Sair")
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Sair", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TelaPerfilPreview() {
    val navController = rememberNavController()
    TelaPerfil(navController = navController)
}
