package com.example.doevida.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
import com.example.doevida.R
import com.example.doevida.components.CalendarioView
import com.example.doevida.components.CartaoHospital
import com.example.doevida.components.ChipHorario
import com.example.doevida.model.HospitaisCards
import com.example.doevida.service.RetrofitFactory
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.imePadding
import com.example.doevida.util.gerarHorarios

@Composable
fun TelaAgendamento(navController: NavController) {
    var listaHospitais by remember { mutableStateOf<List<HospitaisCards>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    var selectedHospital by remember { mutableStateOf<HospitaisCards?>(null) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTime by remember { mutableStateOf<String?>(null) }

    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    val availableTimes = remember(selectedHospital) {
        runCatching {
            gerarHorarios(
                selectedHospital?.horario_abertura,
                selectedHospital?.horario_fechamento
            )
        }.getOrElse { emptyList() }
    }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitFactory(context).getHospitalService().getHospitais()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    listaHospitais = body.hospitais
                } else {
                    errorMessage = "Resposta vazia do servidor."
                }
            } else {
                errorMessage = "Erro ao carregar hospitais: ${response.code()}"
            }
        } catch (e: Exception) {
            errorMessage = "Erro de conexão: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF9F9F9))
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Voltar",
                    tint = Color(0xFF990410),
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { navController.popBackStack() }
                )
                Text(
                    text = "Agendar Doação",
                    color = Color(0xFF990410),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        },

        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = Color(0xFFF9F9F9),
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .navigationBarsPadding()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            val hospitalId = selectedHospital!!.id
                            val data = selectedDate!!.toString()
                            navController.navigate("tela_informacao/$hospitalId/$data")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .imePadding(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF990410)),
                        enabled = selectedHospital != null && selectedDate != null && selectedTime != null
                    ) {
                        Text(
                            text = "Confirmar Agendamento",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },

        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9F9F9))
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            item {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Text(text = "Local", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = "Selecione o local", color = Color.Gray, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            item {
                if (isLoading) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(listaHospitais) { hospital ->
                            CartaoHospital(
                                hospital = hospital,
                                isSelected = selectedHospital?.id == hospital.id,
                                onHospitalSelected = { h ->
                                    selectedHospital = h
                                    // opcional: ao trocar de hospital, limpe seleções
                                    // selectedDate = null; selectedTime = null
                                }
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(20.dp)) }

            item {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Text(text = "Data", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    CalendarioView(
                        currentMonth = currentMonth,
                        onMonthChange = { newMonth -> currentMonth = newMonth },
                        selectedDate = selectedDate,
                        onDateSelected = { date -> selectedDate = date }
                    )
                }
            }

            item { Spacer(Modifier.height(16.dp)) }

            if (availableTimes.isNotEmpty() && selectedHospital != null && selectedDate != null) {
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(availableTimes) { time ->
                            ChipHorario(
                                time = time,
                                isSelected = selectedTime == time,
                                onTimeSelected = { selectedTime = it }
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(16.dp)) }

            item {
                Image(
                    painter = painterResource(id = R.drawable.wallpapelagendamento),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TelaAgendamentoPreview() {
    TelaAgendamento(rememberNavController())
}
