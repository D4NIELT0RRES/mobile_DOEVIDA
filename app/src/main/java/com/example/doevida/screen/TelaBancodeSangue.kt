package com.example.doevida.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R

@Composable
fun TelaBancodeSangue(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TopBar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF8B0000))
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.navigate("tela_home") },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.voltar),
                    contentDescription = "Voltar",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Banco de Sangue",
                color = Color.White,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White,
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Estoque de Sangue",
                    color = Color(0xFFD32F2F),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(30.dp))

            // lista dos tipos de sangue
            BloodStockRow("A+", 0.6f, Color(0xFFE5D252))
            BloodStockRow("A-", 0.9f, Color(0xFF43A047))
            BloodStockRow("B+", 0.5f, Color(0xFFFB8C00))
            BloodStockRow("B-", 0.45f, Color(0xFFFB8C00))
            BloodStockRow("AB+", 0.4f, Color(0xFFFB8C00))
            BloodStockRow("AB-", 0.6f, Color(0xFFE5D252))
            BloodStockRow("O+", 0.37f, Color(0xFFD32F2F))
            BloodStockRow("O-", 0.29f, Color(0xFFD32F2F))

            // Spacer pra garantir que o botão fique no final da tela
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate(route = "tela_agendamento")  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Alerta",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Urgente: Doe O-",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// composable para cada tipo sanguíneo
@Composable
fun BloodStockRow(tipo: String, nivel: Float, cor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

    Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tipo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFDADADA))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(nivel) // preenche conforme nível
                        .background(cor)
                        .clip(RoundedCornerShape(6.dp))
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "${(nivel * 100).toInt()}%",
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaBancodeSanguePreview() {
    val navController = rememberNavController()
    TelaBancodeSangue(navController = navController)
}
