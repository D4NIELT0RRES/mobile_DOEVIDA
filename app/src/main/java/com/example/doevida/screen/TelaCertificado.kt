package com.example.doevida.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCertificado(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Certificados", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF7F7F7),
                    titleContentColor = Color.DarkGray
                )
            )
        },
        containerColor = Color(0xFFF7F7F7)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                CertificadoCard(
                    nomeCompetencia = "Doador de Sangue Ouro",
                    organizacaoEmissora = "DoeVida App",
                    dataEmissao = "25/05/2024"
                )
            }
            item {
                CertificadoCard(
                    nomeCompetencia = "Doador de Sangue Prata",
                    organizacaoEmissora = "DoeVida App",
                    dataEmissao = "15/01/2024"
                )
            }
        }
    }
}

@Composable
fun CertificadoCard(nomeCompetencia: String, organizacaoEmissora: String, dataEmissao: String) {
    val primaryColor = Color(0xFF990410)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEDEDED)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = nomeCompetencia,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = primaryColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Organização emissora: $organizacaoEmissora",
                    fontSize = 14.sp,
                    color = primaryColor.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Data de emissão: $dataEmissao",
                    fontSize = 14.sp,
                    color = primaryColor.copy(alpha = 0.8f)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { /* TODO: Ação de exibir */ },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text("Exibir", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TelaCertificadoPreview() {
    TelaCertificado(rememberNavController())
}
