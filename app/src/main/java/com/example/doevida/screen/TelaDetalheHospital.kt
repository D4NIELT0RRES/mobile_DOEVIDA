package com.example.doevida.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.doevida.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Composable
fun TelaDetalheHospital(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item {
            // Header com botão de voltar e título
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFB71C1C))
                    .padding(vertical = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Hospital",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 100.dp)
                    )
                }
            }

            // Imagem principal
            Image(
                painter = painterResource(id = R.drawable.hospital),
                contentDescription = "Hospital",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            // Informações do hospital
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Banco de Sangue de São Paulo",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "R. Dr. Tomás Carvalhal, 711 - São Paulo",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = "(11) 3373-2050",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Button(
                    onClick = { /* abrir Google Maps */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    border = ButtonDefaults.outlinedButtonBorder
                ) {
                    Text(text = "Abrir no Google Maps", color = Color.Black)
                }

                Button(
                    onClick = { /* ação de ligar */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    border = ButtonDefaults.outlinedButtonBorder
                ) {
                    Text(text = "Ligar Agora", color = Color.Black)
                }

                // Horário de funcionamento
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Horário",
                        tint = Color.Black
                    )
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = "Horário de funcionamento",
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "08:00 - 17:00\nSegunda a sexta", fontSize = 14.sp)
                    }
                }

                // Informações adicionais
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Informações adicionais",
                        tint = Color.Black
                    )
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = "Informações adicionais",
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "• Estacionamento disponível", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun TelaDetalheHospitalPreview() {
    val navController = rememberNavController()
    TelaDetalheHospital(navController = navController)
}