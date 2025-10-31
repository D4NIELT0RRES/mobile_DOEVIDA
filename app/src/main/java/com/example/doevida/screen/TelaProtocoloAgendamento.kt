package com.example.doevida.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.components.LembreteCard
import com.example.doevida.components.ProtocoloInfoSection
import com.example.doevida.model.AgendamentoRequest
import com.example.doevida.model.HospitaisCards
import com.example.doevida.service.RetrofitFactory
import com.example.doevida.util.UserDataManager
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TelaProtocoloAgendamento(
    navController: NavController,
    hospitalId: Int,
    dataSelecionada: String, // Formato YYYY-MM-DD
    horarioSelecionado: String // Formato HH:MM
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nomeUsuario by remember { mutableStateOf("Carregando...") }
    var cpfUsuario by remember { mutableStateOf("Carregando...") }
    var hospital by remember { mutableStateOf<HospitaisCards?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val dataFormatada = remember(dataSelecionada) {
        try {
            LocalDate.parse(dataSelecionada).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } catch (e: Exception) {
            dataSelecionada
        }
    }

    LaunchedEffect(Unit) {
        nomeUsuario = UserDataManager.getUserName(context)
        cpfUsuario = UserDataManager.getUserCpf(context)

        try {
            val response = RetrofitFactory(context).getHospitalService().getHospitalById(hospitalId)
            if (response.isSuccessful) {
                hospital = response.body()?.hospital
            }
        } catch (e: Exception) {
            // Tratar erro
        }
    }

    fun salvarAgendamento() {
        isLoading = true
        scope.launch {
            val request = AgendamentoRequest(
                id_hospital = hospitalId,
                data_agendamento = dataSelecionada,
                horario_agendamento = horarioSelecionado
            )
            try {
                val response = RetrofitFactory(context).getUserService().agendarDoacao(request)
                if (response.isSuccessful) {
                    Toast.makeText(context, "Agendamento realizado com sucesso!", Toast.LENGTH_SHORT).show()
                    navController.navigate("tela_home") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                } else {
                    Toast.makeText(context, "Erro ao agendar: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Erro de conexão: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                isLoading = false
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            ProtocoloInfoSection(
                nome = nomeUsuario,
                cpf = cpfUsuario,
                data = "$dataFormatada às $horarioSelecionado",
                local = hospital?.let { "${it.nomeHospital}, ${it.endereco}" } ?: "Carregando..."
            )

            LembreteCard(onConfirm = { salvarAgendamento() }, isLoading = isLoading)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TelaProtocoloAgendamentoPreview() {
    TelaProtocoloAgendamento(
        navController = rememberNavController(),
        hospitalId = 1,
        dataSelecionada = "2025-07-05",
        horarioSelecionado = "10:00"
    )
}
