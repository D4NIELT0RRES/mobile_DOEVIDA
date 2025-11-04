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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R
import com.example.doevida.model.AgendamentoItem
import com.example.doevida.service.RetrofitFactory
import com.example.doevida.util.UserDataManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TelaHistorico(navController: NavController) {
    val context = LocalContext.current
    var historico by remember { mutableStateOf<List<AgendamentoItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val userId = UserDataManager.getUserId(context)
        if (userId != 0) {
            try {
                val response = RetrofitFactory(context).getUserService().getHistorico(userId)
                if (response.isSuccessful) {
                    historico = response.body()?.agendamentos ?: emptyList()
                }
            } catch (e: Exception) {
            } finally {
                isLoading = false
            }
        } else {
            isLoading = false // Usuário não logado
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF990410))
    ) {
        TopBar(navController)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (historico.isEmpty()) {
                Text(
                    text = "Nenhum agendamento encontrado.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(historico) { agendamento ->
                        DoacaoCard(agendamento)
                    }
                }
            }
        }
    }
}

//-------------------------------------
@Composable
fun TopBar(navController: NavController) {
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
            IconButton(
                onClick = { navController.navigate("tela_home") },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 16.dp, start = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.voltar),
                    contentDescription = "Voltar",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

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
            onValueChange = { },
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
fun DoacaoCard(agendamento: AgendamentoItem) {
    val dataFormatada = remember(agendamento.dataAgendamento) {
        try {
            LocalDate.parse(agendamento.dataAgendamento.substringBefore("T")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } catch (e: Exception) {
            agendamento.dataAgendamento
        }
    }
    val horaFormatada = remember(agendamento.horarioAgendamento) {
        try {
            // Extrai a parte da hora (HH:mm) de uma string de tempo, que pode ser "HH:mm:ss" ou "YYYY-MM-DDTHH:mm:ss"
            val timePart = agendamento.horarioAgendamento.substringAfterLast('T') // Pega o que vem depois do 'T', ou a string inteira se não houver 'T'
            timePart.substring(0, 5) // Pega os 5 primeiros caracteres (HH:mm)
        } catch (e: Exception) {
            agendamento.horarioAgendamento // Em caso de erro, mostra a string original
        }
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
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
                    text = "Doação ${agendamento.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                val corStatus = when (agendamento.status) {
                    "CONCLUIDO" -> Color(0xFF549073)
                    "AGENDADO" -> Color(0xFFB72C2C)
                    else -> Color.Gray
                }
                val textoStatus = when (agendamento.status) {
                    "CONCLUIDO" -> "Concluído"
                    "AGENDADO" -> "Em espera"
                    else -> agendamento.status
                }


                Box(
                    modifier = Modifier
                        .background(corStatus, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = textoStatus,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Local: ${agendamento.nomeHospital ?: "Não informado"}", fontWeight = FontWeight.SemiBold)
            Text(text = "Dia: $dataFormatada")
            Text(text = "Horário: $horaFormatada")
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
