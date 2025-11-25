package com.example.doevida.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
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
import com.example.doevida.service.RetrofitFactory
import com.example.doevida.service.SharedPreferencesUtils
import com.example.doevida.util.UserDataManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCertificado(navController: NavController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var donationCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        val userId = UserDataManager.getUserId(context)
        if (userId != 0) {
            try {
                // Busca doações manuais
                val manualCount = SharedPreferencesUtils.getManualDonations(context)
                    .count { it.userId == userId }

                // Busca doações da API
                val apiCount = try {
                    val response = RetrofitFactory(context).getUserService().getHistorico(userId)
                    if (response.isSuccessful) {
                        response.body()?.agendamentos?.size ?: 0
                    } else 0
                } catch (e: Exception) { 0 }

                donationCount = manualCount + apiCount
            } catch (e: Exception) {
                donationCount = 0 // Fallback
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Certificados e Conquistas", fontWeight = FontWeight.Bold) },
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
                GamificationCard(donationCount)
            }
            
            item {
                Text(
                    text = "Meus Certificados",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            if (donationCount >= 10) {
                item {
                    CertificadoCard(
                        nomeCompetencia = "Doador de Sangue Ouro",
                        organizacaoEmissora = "DoeVida App",
                        dataEmissao = "Conquista Desbloqueada!"
                    )
                }
            }
            if (donationCount >= 5) {
                item {
                    CertificadoCard(
                        nomeCompetencia = "Doador de Sangue Prata",
                        organizacaoEmissora = "DoeVida App",
                        dataEmissao = "Conquista Desbloqueada!"
                    )
                }
            }
            if (donationCount >= 1) {
                item {
                    CertificadoCard(
                        nomeCompetencia = "Primeira Doação",
                        organizacaoEmissora = "DoeVida App",
                        dataEmissao = "Conquista Desbloqueada!"
                    )
                }
            } else {
                item {
                    Text("Faça sua primeira doação para desbloquear certificados!", color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun GamificationCard(donationCount: Int) {
    val primaryColor = Color(0xFF990410)
    val nextGoal = if (donationCount < 5) 5 else if (donationCount < 10) 10 else donationCount + 5
    val progress = donationCount.toFloat() / nextGoal.toFloat()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFD700).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Star, contentDescription = "Estrela", tint = Color(0xFFFFD700))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Sua Jornada de Doador",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = "$donationCount doações realizadas",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            
            Text(
                text = "Próxima conquista: $nextGoal doações",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = primaryColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = primaryColor,
                trackColor = primaryColor.copy(alpha = 0.1f),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Faltam ${nextGoal - donationCount} doações para o próximo nível!",
                fontSize = 12.sp,
                color = Color.Gray
            )
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
                    text = "Data: $dataEmissao",
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
