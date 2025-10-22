package com.example.doevida.screen

import android.content.Context
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.components.LembreteCard
import com.example.doevida.components.ProtocoloInfoSection
import com.example.doevida.model.HospitaisCards
import com.example.doevida.service.RetrofitFactory
import com.example.doevida.util.UserDataManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TelaProtocoloAgendamento(
    navController: NavController,
    hospitalId: Int,
    dataSelecionada: String // Formato YYYY-MM-DD
) {
    val context = LocalContext.current

    // Dados do usuário (carregados via UserDataManager)
    var nomeUsuario by remember { mutableStateOf("Carregando...") }
    var cpfUsuario by remember { mutableStateOf("Carregando...") }

    // Dados do Hospital (carregados da API)
    var hospital by remember { mutableStateOf<HospitaisCards?>(null) }

    // Formata a data para exibição (DD/MM/YYYY)
    val dataFormatada = remember(dataSelecionada) {
        try {
            LocalDate.parse(dataSelecionada).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } catch (e: Exception) {
            dataSelecionada // Em caso de erro, mostra a data original
        }
    }

    // Busca os dados do usuário e do hospital
    LaunchedEffect(Unit) {
        // Carrega dados do usuário usando o UserDataManager
        nomeUsuario = UserDataManager.getUserName(context)
        cpfUsuario = UserDataManager.getUserCpf(context)

        // Busca dados do hospital pela API
        try {
            val response = RetrofitFactory(context).getHospitalService().getHospitalById(hospitalId)
            if (response.isSuccessful) {
                hospital = response.body()?.hospital
            }
        } catch (e: Exception) {
            // Tratar erro de conexão
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            ProtocoloInfoSection(
                nome = nomeUsuario,
                cpf = cpfUsuario,
                data = dataFormatada,
                local = hospital?.let { "${it.nomeHospital}, ${it.endereco}" } ?: "Carregando..."
            )

            LembreteCard {
                // Ação do botão confirmar - navegar para a tela inicial
                navController.navigate("tela_home") {
                    // Limpa a pilha de navegação para que o usuário não volte para as telas de agendamento
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TelaProtocoloAgendamentoPreview() {
    TelaProtocoloAgendamento(
        navController = rememberNavController(),
        hospitalId = 1,
        dataSelecionada = "2025-07-05"
    )
}
