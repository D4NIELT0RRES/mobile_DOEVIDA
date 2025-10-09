package com.example.doevida.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R

@Composable
fun TelaDetalheHospital(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFB71C1C))
                .padding(vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Botão de voltar (lado esquerdo)
                IconButton(onClick = { navController.navigate("tela_inicial") }) {
                    Icon(
                        painter = painterResource(id = R.drawable.voltar),
                        contentDescription = "Voltar",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Texto centralizado
                Text(
                    text = "Hospital",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                // Espaço à direita (para equilibrar o layout)
                Box(modifier = Modifier.size(48.dp)) {}
            }
        }


        // Imagem principal
        Image(
            painter = painterResource(id = R.drawable.hospital),
            contentDescription = "Hospital",
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp),
            contentScale = ContentScale.Crop
        )

        // Conteúdo rolável
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 32.dp)
                ) {

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = "Banco de Sangue de São Paulo",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "R. Dr. Tomás Carvalhal, 711 - São Paulo",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "(11) 3373-2050",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(15.dp))


                    OutlinedButton(
                        onClick = { /* abrir Google Maps */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                    ) {
                        Text(text = "Abrir no Google Maps", color = Color.Black)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { /* ligar agora */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                    ) {
                        Text(text = "Ligar Agora", color = Color.Black)
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = Color.Black

                        )
                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Text(
                                text = "Horário de funcionamento",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "08:00 - 17:00\nSegunda a sexta",
                                fontSize = 14.sp

                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Text(
                                text = "Informações adicionais",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "• Estacionamento disponível",
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TelaDetalheHospitalPreview() {
    TelaDetalheHospital(navController = rememberNavController())
}