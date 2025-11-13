package com.example.doevida.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.doevida.R
import com.example.doevida.model.AgendamentoItem
import com.example.doevida.model.DoacaoManual
import com.example.doevida.model.HistoricoDoacaoCombinado
import com.example.doevida.service.RetrofitFactory
import com.example.doevida.service.SharedPreferencesUtils
import com.example.doevida.util.UserDataManager
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun navigateToDetail(navController: NavController, doacao: HistoricoDoacaoCombinado) {
    val doacaoJson = Gson().toJson(doacao)
    // Codifica o JSON para que possa ser passado como um único argumento na rota
    val encodedJson = URLEncoder.encode(doacaoJson, StandardCharsets.UTF_8.name())
    navController.navigate("tela_detalhe_doacao/$encodedJson")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaHistorico(navController: NavController) {
    val context = LocalContext.current
    var historico by remember { mutableStateOf<List<HistoricoDoacaoCombinado>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val userId = UserDataManager.getUserId(context)
        val combinedList = mutableListOf<HistoricoDoacaoCombinado>()

        if (userId != 0) {
            try {
                val response = RetrofitFactory(context).getUserService().getHistorico(userId)
                if (response.isSuccessful) {
                    val apiDonations = response.body()?.agendamentos ?: emptyList()
                    combinedList.addAll(apiDonations.map { it.toCombinedHistory() })
                }
            } catch (e: Exception) { /* Tratar erro */ }
        }

        val manualDonations = SharedPreferencesUtils.getManualDonations(context)
            .filter { it.userId == userId }
        combinedList.addAll(manualDonations.map { it.toCombinedHistory() })

        combinedList.sortByDescending { it.timestamp }
        historico = combinedList
        isLoading = false
    }

    val filteredHistorico = if (searchQuery.isEmpty()) {
        historico
    } else {
        historico.filter {
            it.hospitalName.contains(searchQuery, ignoreCase = true) || it.status.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = { TopBarHistorico(navController) },
        content = { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFFF7F7F7))) {
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
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredHistorico) { donation ->
                            DoacaoCard(donation) { navigateToDetail(navController, donation) }
                        }
                    }
                }
            }
        }
    )
}

fun AgendamentoItem.toCombinedHistory(): HistoricoDoacaoCombinado {
    val parsedDate = try { LocalDate.parse(this.dataAgendamento.substringBefore("T")) } catch (e: Exception) { LocalDate.now() }
    return HistoricoDoacaoCombinado(
        hospitalName = this.nomeHospital ?: "Local não informado",
        date = parsedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
        status = this.status,
        proofImageUrl = null,
        observacao = null,
        timestamp = parsedDate.toEpochDay()
    )
}

fun DoacaoManual.toCombinedHistory(): HistoricoDoacaoCombinado {
    return HistoricoDoacaoCombinado(
        hospitalName = this.hospitalName,
        date = this.donationDate,
        status = "Manual",
        proofImageUrl = this.proofImageUrl,
        observacao = this.observacao,
        timestamp = this.timestamp
    )
}

@Composable
fun DoacaoCard(donation: HistoricoDoacaoCombinado, onClick: () -> Unit) {
    val (statusColor, statusText) = when (donation.status.uppercase()) {
        "CONCLUIDO" -> Pair(Color(0xFF4CAF50), "Concluído")
        "AGENDADO" -> Pair(Color(0xFFFF9800), "Agendado")
        "MANUAL" -> Pair(Color(0xFF607D8B), "Manual")
        else -> Pair(Color.Gray, donation.status.replaceFirstChar { it.uppercase() })
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            if (donation.proofImageUrl != null) {
                AsyncImage(
                    model = donation.proofImageUrl,
                    contentDescription = "Comprovante",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.LightGray, CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(statusColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.gota),
                        contentDescription = "Doação",
                        tint = statusColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = donation.hospitalName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Data",
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = donation.date,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
            StatusTag(statusText, statusColor)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHistorico(navController: NavController) {
    TopAppBar(
        title = { Text("Meu Histórico", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
        navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = Color(0xFFB03940))
    )
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query, onValueChange = onQueryChange, placeholder = { Text("Pesquisar...") },
        leadingIcon = { Icon(Icons.Default.Search, "Pesquisar") }, shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF990410), unfocusedBorderColor = Color.LightGray)
    )
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(id = R.drawable.doarsangue), "Histórico Vazio", modifier = Modifier.size(150.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Nenhuma doação encontrada", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
        Text("Seu histórico de doações aparecerá aqui.", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center)
    }
}

@Composable
fun StatusTag(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, color = color, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Preview(showBackground = true)
@Composable
private fun TelaHistoricoPreview() {
    TelaHistorico(rememberNavController())
}
