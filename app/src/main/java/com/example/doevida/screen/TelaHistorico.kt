package com.example.doevida.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.doevida.model.DoacaoManual
import com.example.doevida.model.HistoricoDoacaoCombinado
import com.example.doevida.service.RetrofitFactory
import com.example.doevida.service.SharedPreferencesUtils
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun navigateToDetail(navController: NavController, doacao: HistoricoDoacaoCombinado) {
    val doacaoJson = Gson().toJson(doacao)
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
    var selectedFilter by remember { mutableStateOf("Todos") }

    val primaryColor = Color(0xFF990410)

    LaunchedEffect(Unit) {
        val userId = SharedPreferencesUtils.getUserId(context)
        val combinedList = mutableListOf<HistoricoDoacaoCombinado>()

        if (userId > 0) {
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

    val filteredHistorico = historico.filter { item ->
        val matchesSearch = item.hospitalName.contains(searchQuery, ignoreCase = true) || 
                            item.status.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "Concluídas" -> item.status.equals("Concluido", ignoreCase = true) || 
                           item.status.equals("Concluído", ignoreCase = true) || 
                           item.status.equals("Manual", ignoreCase = true)
            "Agendadas" -> item.status.equals("Agendado", ignoreCase = true) || item.status.equals("Pendente", ignoreCase = true)
            else -> true
        }
        matchesSearch && matchesFilter
    }

    val totalDoacoes = historico.count { 
        val s = it.status.uppercase()
        s == "CONCLUIDO" || s == "CONCLUÍDO" || s == "MANUAL" 
    }
    val ultimaDoacao = historico.firstOrNull { 
        val s = it.status.uppercase()
        s == "CONCLUIDO" || s == "CONCLUÍDO" || s == "MANUAL" 
    }?.date ?: "--"

    Scaffold(
        topBar = { TopBarHistorico(navController) },
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            
            // Header com Resumo
            DonationSummaryCard(totalDoacoes, ultimaDoacao)

            // Barra de Pesquisa e Filtros
            Column(modifier = Modifier.background(Color.White).padding(bottom = 8.dp)) {
                SearchBar(searchQuery) { searchQuery = it }
                FilterChips(selectedFilter) { selectedFilter = it }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryColor)
                }
            } else if (filteredHistorico.isEmpty()) {
                EmptyState(searchQuery.isNotEmpty())
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredHistorico) { donation ->
                        DoacaoCardModern(donation) { navigateToDetail(navController, donation) }
                    }
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DonationSummaryCard(total: Int, lastDate: String) {
    val primaryColor = Color(0xFF990410)
    val gradient = Brush.horizontalGradient(
        colors = listOf(primaryColor, Color(0xFFCC2B36))
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.background(gradient).padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total de Doações",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "$total",
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Última: $lastDate",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp
                        )
                    }
                }
                
                Icon(
                    painter = painterResource(R.drawable.gota),
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(80.dp)
                )
            }
        }
    }
}

@Composable
fun FilterChips(selected: String, onSelect: (String) -> Unit) {
    val filters = listOf("Todos", "Concluídas", "Agendadas")
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { filter ->
            val isSelected = selected == filter
            FilterChip(
                selected = isSelected,
                onClick = { onSelect(filter) },
                label = { Text(filter) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF990410).copy(alpha = 0.1f),
                    selectedLabelColor = Color(0xFF990410)
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = Color.LightGray,
                    selectedBorderColor = Color(0xFF990410)
                )
            )
        }
    }
}

@Composable
fun DoacaoCardModern(donation: HistoricoDoacaoCombinado, onClick: () -> Unit) {
    val (statusColor, statusText, statusIcon) = when (donation.status.uppercase()) {
        "CONCLUIDO", "CONCLUÍDO", "MANUAL" -> Triple(Color(0xFF2E7D32), "Concluída", Icons.Default.CheckCircle)
        "AGENDADO" -> Triple(Color(0xFFF57C00), "Agendada", Icons.Default.Schedule)
        "CANCELADO" -> Triple(Color(0xFFD32F2F), "Cancelada", Icons.Default.Close)
        else -> Triple(Color.Gray, donation.status, Icons.Default.History)
    }

    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Data Badge
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5))
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            ) {
                val parts = donation.date.split("/")
                if (parts.size == 3) {
                    Text(text = parts[0], fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF333333))
                    Text(text = getMonthShortName(parts[1]), fontSize = 12.sp, color = Color(0xFF757575), fontWeight = FontWeight.Medium)
                } else {
                    Icon(Icons.Default.CalendarToday, null, tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Conteúdo
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = donation.hospitalName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF212121),
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(14.dp), tint = Color(0xFF757575))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Hemocentro / Hospital",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF757575)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Status Chip Personalizado
                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(statusIcon, null, modifier = Modifier.size(12.dp), tint = statusColor)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = statusText, color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

fun getMonthShortName(month: String): String {
    return when(month) {
        "01" -> "JAN"; "02" -> "FEV"; "03" -> "MAR"; "04" -> "ABR"
        "05" -> "MAI"; "06" -> "JUN"; "07" -> "JUL"; "08" -> "AGO"
        "09" -> "SET"; "10" -> "OUT"; "11" -> "NOV"; "12" -> "DEZ"
        else -> ""
    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHistorico(navController: NavController) {
    CenterAlignedTopAppBar(
        title = { 
            Text(
                "Histórico de Doações", 
                fontWeight = FontWeight.SemiBold, 
                fontSize = 18.sp
            ) 
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", tint = Color(0xFF212121))
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color(0xFF212121)
        )
    )
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query, 
        onValueChange = onQueryChange, 
        placeholder = { Text("Buscar hospital...", fontSize = 14.sp) },
        leadingIcon = { Icon(Icons.Default.Search, "Pesquisar", tint = Color.Gray) },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF990410),
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        ),
        singleLine = true
    )
}

@Composable
fun EmptyState(isSearch: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.gota),
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = Color(0xFF990410).copy(alpha = 0.4f)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = if (isSearch) "Nenhum resultado encontrado" else "Seu histórico está vazio",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF424242)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isSearch) "Tente buscar com outro termo." else "Agende sua doação e comece a salvar vidas!",
            fontSize = 14.sp,
            color = Color(0xFF757575),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TelaHistoricoPreview() {
    TelaHistorico(rememberNavController())
}
