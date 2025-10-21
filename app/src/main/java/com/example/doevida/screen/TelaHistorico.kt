package com.example.doevida.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R
import com.example.doevida.model.Doacao

@Composable
fun TelaHistorico(navController: NavController) {

    //tirar dps
    val doacoes = listOf(
        Doacao("Doação 04", "Hospital centra", "05/07/2025", "09:00", "Em espera"),
        Doacao("Doação 03", "Hospital Regional", "06/05/2025", "11:00", "Concluído"),
        Doacao("Doação 02", "Hospital do Servidor Público Municipal – Aclimação", "07/01/2025", "12:00", "Concluído"),
        Doacao("Doação 01", "Centro médico", "09/10/2024", "10:00", "Concluído")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF990410))
    ) {
        // topo
        TopBar()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(doacoes) { doacao ->
                    DoacaoCard(doacao)
                }
            }
        }
    }
}
//-------------------------------------
@Composable
fun TopBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.voltar),
                contentDescription = "Voltar",
                tint = Color.White,
                modifier = Modifier
                    .size(22.dp)
                    .align(Alignment.CenterStart)
            )

            Text(
                text = "Histórico de Doação",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Find Seekers", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .height(52.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                cursorColor = Color(0xFFB72C2C)
            )
        )
    }
}
//-------------------------------------
@Composable
fun DoacaoCard(doacao: Doacao) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp, // espessura da borda
                color = Color(0xFFBDBDBD),
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = doacao.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                // Status
                val corStatus = when (doacao.status) {
                    "Concluído" -> Color(0xFF549073)
                    else -> Color(0xFFB72C2C)
                }

                Box(
                    modifier = Modifier
                        .background(corStatus, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = doacao.status,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Local: ${doacao.local}", fontWeight = FontWeight.SemiBold)
            Text(text = "Dia: ${doacao.data}")
            Text(text = "Horário: ${doacao.horario}")
        }
    }
}
//-------------------------------------
@Preview
@Composable
private fun TelaHistoricoPreview() {
    val navController = rememberNavController()
    TelaHistorico(navController = navController)
}