package com.example.doevida.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.model.AgendamentoRequest
import com.example.doevida.model.HospitaisCards
import com.example.doevida.service.RetrofitFactory
import com.example.doevida.util.UserDataManager
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaProtocoloAgendamento(navController: NavController, hospitalId: Int, dataSelecionada: String, horarioSelecionado: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var hospital by remember { mutableStateOf<HospitaisCards?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var agendamentoProtocolo by remember { mutableStateOf<String?>(null) }

    // Formatando a data para exibição
    val dataFormatada = remember(dataSelecionada) {
        try {
            LocalDate.parse(dataSelecionada).format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR")))
        } catch (e: Exception) { dataSelecionada }
    }

    fun salvarAgendamento() {
        if (agendamentoProtocolo != null) { // Evita agendar duas vezes
            navController.navigate("tela_home") { popUpTo(0) }
            return
        }

        isLoading = true
        scope.launch {
            val request = AgendamentoRequest(hospitalId, dataSelecionada, "$horarioSelecionado:00")
            try {
                val response = RetrofitFactory(context).getUserService().agendarDoacao(request)
                if (response.isSuccessful) {
                    agendamentoProtocolo = response.body()?.agendamento?.id.toString() // Simulação
                    Toast.makeText(context, "Agendamento confirmado com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Erro ao confirmar agendamento: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Erro de conexão: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(hospitalId) {
        scope.launch {
            try {
                val response = RetrofitFactory(context).getHospitalService().getHospitalById(hospitalId)
                if (response.isSuccessful) hospital = response.body()?.hospital
            } catch (e: Exception) { /* Tratar erro de busca do hospital */ }
        }
    }

    Scaffold(
        topBar = { TopBarProtocolo() },
        bottomBar = { BottomBarProtocolo(isLoading) { if(agendamentoProtocolo != null) { navController.navigate("tela_home") { popUpTo(0) } } else { salvarAgendamento() } } },
        containerColor = Color(0xFFF7F7F7)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it).verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            if (agendamentoProtocolo == null) {
                // Tela de confirmação antes de gerar o protocolo
                ReviewDetails(hospital, dataFormatada, horarioSelecionado)
            } else {
                // Tela de sucesso após gerar o protocolo
                SuccessDetails(hospital, dataFormatada, horarioSelecionado, agendamentoProtocolo!!)
            }
        }
    }
}

@Composable
fun ReviewDetails(hospital: HospitaisCards?, dataFormatada: String, horarioSelecionado: String) {
    val nomeUsuario = UserDataManager.getUserName(LocalContext.current)
    Column {
        Text("Revise seu agendamento", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Confirme os detalhes abaixo antes de finalizar.", color = Color.Gray, modifier = Modifier.padding(top = 4.dp, bottom = 16.dp))
        
        AppointmentSummaryCard(hospital, dataFormatada, horarioSelecionado, nomeUsuario)
    }
}

@Composable
fun SuccessDetails(hospital: HospitaisCards?, dataFormatada: String, horarioSelecionado: String, protocolo: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Icon(Icons.Filled.CheckCircle, contentDescription = "Sucesso", tint = Color(0xFF4CAF50), modifier = Modifier.size(80.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Agendamento Confirmado!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
        Text("Seu protocolo é:", color = Color.Gray, modifier = Modifier.padding(top = 16.dp))
        Text(protocolo, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = Color.DarkGray, modifier = Modifier.padding(vertical = 8.dp))
        
        Spacer(modifier = Modifier.height(24.dp))
        InstructionsCard()
    }
}

@Composable
fun AppointmentSummaryCard(hospital: HospitaisCards?, data: String, horario: String, nomeUsuario: String) {
    Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(Modifier.padding(16.dp)) {
            InfoRow(label = "Doador", value = nomeUsuario)
            Divider(Modifier.padding(vertical = 8.dp))
            InfoRow(label = "Local", value = hospital?.nomeHospital ?: "Carregando...")
            Divider(Modifier.padding(vertical = 8.dp))
            InfoRow(label = "Endereço", value = hospital?.endereco ?: "Carregando...")
            Divider(Modifier.padding(vertical = 8.dp))
            InfoRow(label = "Data", value = data)
            Divider(Modifier.padding(vertical = 8.dp))
            InfoRow(label = "Horário", value = horario)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(Modifier.padding(vertical = 4.dp)) {
        Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF990410))
        Text(value, fontSize = 16.sp, color = Color.DarkGray)
    }
}

@Composable
fun InstructionsCard() {
    InfoCard(
        icon = Icons.Default.Info,
        title = "Prepare-se para a doação",
        content = {
            Text("• Beba bastante água antes de doar.", fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
            Text("• Esteja bem alimentado, evitando comidas gordurosas.", fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
            Text("• Leve um documento oficial com foto.", fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
            Text("• Durma bem na noite anterior.", fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarProtocolo() {
    TopAppBar(
        title = { Text("Confirmação", fontWeight = FontWeight.Bold) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF7F7F7), titleContentColor = Color.DarkGray)
    )
}

@Composable
fun BottomBarProtocolo(isLoading: Boolean, onClick: () -> Unit) {
    Surface(shadowElevation = 8.dp, color = Color.White) {
        Button(
            onClick = onClick,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth().padding(16.dp).height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF990410)),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Confirmar e Agendar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TelaProtocoloAgendamentoPreview() {
    TelaProtocoloAgendamento(rememberNavController(), 1, "2025-07-05", "10:00")
}
