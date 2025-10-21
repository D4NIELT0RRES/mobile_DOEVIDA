package com.example.doevida.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun TelaPerfil(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top vermelho
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF8B0000))
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "Nome",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text("Nome de usuário", color = Color.White, fontSize = 14.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Total de Doações:", color = Color.White)
                Text("Doações este ano:", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Dados Pessoais
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        }
    }
}

@Composable
fun MenuInferior(navController: NavController) {
    var selectedItem by remember { mutableStateOf("home") }

    NavigationBar(
        containerColor = Color.White
    ) {
        val items = listOf(
            "home" to Icons.Default.Home,
            "noticias" to Icons.Default.Newspaper,
            "perfil" to Icons.Default.Person
        )

        items.forEach { (route, icon) ->
            NavigationBarItem(
                selected = selectedItem == route,
                onClick = {
                    selectedItem = route
                    navController.navigate(route)
                },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = route,
                        tint = if (selectedItem == route) Color(0xFF8B0000) else Color.Gray
                    )
                },
                alwaysShowLabel = true,
                label = {
                    Text(
                        text = route.replaceFirstChar { it.uppercase() },
                        color = if (selectedItem == route) Color(0xFF8B0000) else Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFF8B0000)
                )
            )
        }
    }
}

@Preview
@Composable
private fun TelaPerfilPreview() {
    val navController = rememberNavController()
    TelaPerfil(navController = navController)
}