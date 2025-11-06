package com.example.doevida.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.components.MenuInferior
import com.example.doevida.model.HospitaisCards
import com.example.doevida.service.SharedPreferencesUtils

@Composable
fun TelaCertificado(navController: NavController) {
    val userName = remember { mutableStateOf("") }

    val context = LocalContext.current

    // Usando LaunchedEffect para carregar os dados assim que a TelaHome for composta
    LaunchedEffect(Unit) {
        userName.value = SharedPreferencesUtils.getUserName(context)
        Log.d("TelaHome", "Nome de usuário carregado: ${userName.value}")
    }

    Scaffold(
        bottomBar = {
            MenuInferior(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF8B0000))
                    .padding(bottom = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
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
                                text = userName.value,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Nome de usuário",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Divider(
                        color = Color.White
                            .copy(alpha = 0.1f), // quase transparente
                        thickness = 1.dp
                    )


                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        "Total de Doações:",
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        "Doações este ano:",
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(
                                topStart = 50.dp,
                                topEnd = 50.dp
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun CardsCertificado(
    nome: String = "Nome da Competência:",
    organizacao: String = "Organização emissora:",
    data: String = "Data de emissão:",
    onExibirClick: () -> Unit = {}
) {

    val corTexto = Color(0xFF8B0000)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 12.dp,
                vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xC6FFFFFF)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = nome,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = corTexto
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = organizacao,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = corTexto
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = data,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = corTexto
                )
            }

            Button(
                onClick = { onExibirClick() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .height(36.dp)
                    .width(90.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000))
            ) {
                Text(
                    text = "Exibir",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun CardsCertificadoPreview() {
    CardsCertificado()
}

@Preview
@Composable
private fun TelaCertificadoPreview() {
    val navController = rememberNavController()
    TelaCertificado(navController = navController)
}

