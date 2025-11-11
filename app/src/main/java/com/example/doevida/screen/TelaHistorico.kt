package com.example.doevida.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import java.util.* 

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaHistorico(navController: NavController) {
    val context = LocalContext.current
    var historico by remember { mutableStateOf<List<AgendamentoItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val userId = UserDataManager.getUserId(context)
        if (userId != 0) {
            try {
                val response = RetrofitFactory(context).getUserService().getHistorico(userId)
                if (response.isSuccessful) {
                    historico = response.body()?.agendamentos ?: emptyList()
                }
            } catch (e: Exception) {
                // Tratar erro de conexão
            } finally {
                isLoading = false
            }
        } else {
            isLoading = false // Usuário não logado
        }
    }

    val filteredHistorico = if (searchQuery.isEmpty()) {
        historico
    } else {
        historico.filter {
            it.nomeHospital?.contains(searchQuery, ignoreCase = true) == true ||
            it.status.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = { TopBarHistorico(navController) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(Color(0xFFF7F7F7))
            ) {
                SearchBar(searchQuery) { newQuery -> searchQuery = newQuery }

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF990410))
                    }
                } else if (filteredHistorico.isEmpty()) {
                    EmptyState()
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredHistorico) { agendamento ->
                            DoacaoCard(agendamento)
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHistorico(navController: NavController) {
    TopAppBar(
        title = { Text("Meu Histórico", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color(0xFF990410)
        )
    )
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Pesquisar por hospital ou status...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Pesquisar") },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF990410),
            unfocusedBorderColor = Color.LightGray,
            cursorColor = Color(0xFF990410),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.doarsangue),
            contentDescription = "Histórico Vazio",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Nenhuma doação encontrada",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.DarkGray
        )
        Text(
            text = "Seu histórico de doações aparecerá aqui.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun DoacaoCard(agendamento: AgendamentoItem) {
    val dataFormatada = remember(agendamento.dataAgendamento) {
        try {
            LocalDate.parse(agendamento.dataAgendamento.substringBefore("T")).format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("pt", "BR")))
        } catch (e: Exception) {
            agendamento.dataAgendamento
        }
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.doarsangue),
                contentDescription = "Doação",
                tint = Color(0xFF990410),
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = agendamento.nomeHospital ?: "Local não informado", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Agendado para: $dataFormatada", fontSize = 14.sp, color = Color.Gray)
            }
            StatusTag(status = agendamento.status)
        }
    }
}

@Composable
fun StatusTag(status: String) {
    val (cor, texto) = when (status.uppercase()) {
        "CONCLUIDO" -> Pair(Color(0xFF4CAF50), "Concluído")
        "AGENDADO" -> Pair(Color(0xFFFF9800), "Agendado")
        else -> Pair(Color.Gray, status.replaceFirstChar { it.uppercase() })
    }

    Box(
        modifier = Modifier
            .background(cor.copy(alpha = 0.15f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = texto,
            color = cor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TelaHistoricoPreview() {
    val navController = rememberNavController()
    TelaHistorico(navController = navController)
}
