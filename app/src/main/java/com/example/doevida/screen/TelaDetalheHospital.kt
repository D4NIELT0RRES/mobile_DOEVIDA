package com.example.doevida.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.doevida.R
import com.example.doevida.model.HospitaisCards
import com.example.doevida.service.RetrofitFactory
import kotlinx.coroutines.launch

// >>> IMPORTA AS FUNÇÕES DE UTIL
import com.example.doevida.util.abrirNoMaps
import com.example.doevida.util.abrirNoMapsPorCEP
import com.example.doevida.util.ligarPara

@Composable
fun TelaDetalheHospital(
    navController: NavController,
    hospitalId: Int
) {
    var hospital by remember { mutableStateOf<HospitaisCards?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(hospitalId) {
        scope.launch {
            try {
                val response = RetrofitFactory(context).getHospitalService().getHospitalById(hospitalId)
                if (response.isSuccessful) {
                    response.body()?.let { hospitalResponse ->
                        hospital = hospitalResponse.hospital
                    }
                } else {
                    errorMessage = "Erro ao carregar hospital: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "Erro de conexão: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF990410))
                .padding(vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { navController.navigate("tela_hospitais") },
                    modifier = Modifier
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
                    text = "Hospital",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Box(modifier = Modifier.size(48.dp))
            }
        }

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF990410))
                }
            }
            errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = errorMessage!!,
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(
                            onClick = {
                                isLoading = true
                                errorMessage = null
                                scope.launch {
                                    try {
                                        val response = RetrofitFactory(context).getHospitalService().getHospitalById(hospitalId)
                                        if (response.isSuccessful) {
                                            response.body()?.let { hospitalResponse ->
                                                hospital = hospitalResponse.hospital
                                            }
                                        } else {
                                            errorMessage = "Erro ao carregar hospital: ${response.code()}"
                                        }
                                    } catch (e: Exception) {
                                        errorMessage = "Erro de conexão: ${e.message}"
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF990410))
                        ) { Text("Tentar Novamente") }
                    }
                }
            }
            hospital != null -> {
                val hospitalData = hospital!!

                Image(
                    painter = painterResource(id = R.drawable.hospital),
                    contentDescription = "Hospital",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(270.dp),
                    contentScale = ContentScale.Crop
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, bottom = 32.dp)
                        ) {
                            Spacer(modifier = Modifier.height(15.dp))

                            Text(
                                text = hospitalData.nomeHospital,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = hospitalData.endereco,
                                color = Color.Gray,
                                fontSize = 16.sp
                            )

                            Text(
                                text = hospitalData.telefone,
                                color = Color.Gray,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Spacer(modifier = Modifier.height(15.dp))

                            OutlinedButton(
                                onClick = { abrirNoMaps(context, hospitalData) },
                                modifier = Modifier.fillMaxWidth().height(56.dp).padding(vertical = 4.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                            ) {
                                Text(text = "Abrir no Google Maps", color = Color.Black)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedButton(
                                onClick = { ligarPara(context, hospitalData.telefone) },
                                modifier = Modifier.fillMaxWidth().height(56.dp).padding(vertical = 4.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                            ) {
                                Text(text = "Ligar Agora", color = Color.Black)
                            }

                            Spacer(modifier = Modifier.height(15.dp))

                            if (hospitalData.horario_abertura != null && hospitalData.horario_fechamento != null) {
                                // >>> FORMATA OS HORÁRIOS ANTES DE EXIBIR
                                val horarioAbertura = formatarHorario(hospitalData.horario_abertura)
                                val horarioFechamento = formatarHorario(hospitalData.horario_fechamento)

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = Color.Black
                                    )
                                    Column(modifier = Modifier.padding(start = 8.dp)) {
                                        Text(
                                            text = "Horário de funcionamento",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        // >>> USA AS VARIÁVEIS FORMATADAS
                                        Text(
                                            text = "$horarioAbertura - $horarioFechamento\nSegunda a sexta",
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(15.dp))
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color.Black
                                )
                                Column(modifier = Modifier.padding(start = 8.dp)) {
                                    Text(
                                        text = "Informações adicionais",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    hospitalData.convenios?.let { convenios ->
                                        Text(text = "• Convênios: $convenios", fontSize = 14.sp)
                                    }
                                    hospitalData.capacidade_maxima?.let { capacidade ->
                                        Text(text = "• Capacidade máxima: $capacidade pessoas", fontSize = 14.sp)
                                    }
                                    Text(text = "• Estacionamento disponível", fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Função auxiliar para formatar o horário
private fun formatarHorario(horarioISO: String?): String {
    if (horarioISO.isNullOrBlank() || !horarioISO.contains("T")) {
        return "" // Retorna vazio se o formato for inválido
    }
    return try {
        // Pega a string após o 'T' e extrai as 5 primeiras posições (HH:mm)
        horarioISO.substringAfter('T').take(5)
    } catch (e: Exception) {
        // Em caso de qualquer erro na manipulação da string, retorna vazio
        ""
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TelaDetalheHospitalPreview() {
    TelaDetalheHospital(
        navController = rememberNavController(),
        hospitalId = 1
    )
}
