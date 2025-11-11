package com.example.doevida.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.components.CalendarioView
import com.example.doevida.components.CartaoHospital
import com.example.doevida.components.ChipHorario
import com.example.doevida.model.HospitaisCards
import com.example.doevida.service.RetrofitFactory
import com.example.doevida.util.gerarHorarios
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
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
        runCatching { gerarHorarios(selectedHospital?.horario_abertura, selectedHospital?.horario_fechamento) }.getOrElse { emptyList() }
    }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitFactory(context).getHospitalService().getHospitais()
            if (response.isSuccessful) {
                listaHospitais = response.body()?.hospitais ?: emptyList()
            } else {
                errorMessage = "Erro ao carregar hospitais: ${response.code()}"
            }
        } catch (e: Exception) {
            errorMessage = "Erro de conexão: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    val progress = animateFloatAsState(
        targetValue = when {
            selectedHospital == null -> 0.33f
            selectedDate == null -> 0.66f
            else -> 1f
        },
        label = "progressAnimation"
    ).value

    Scaffold(
        topBar = { TopBarAgendamento(navController, progress) },
        bottomBar = { BottomBarAgendamento(navController, selectedHospital, selectedDate, selectedTime) },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(paddingValues)
        ) {
            item { Step1_SelectHospital(isLoading, errorMessage, listaHospitais, selectedHospital) { selectedHospital = it } }
            item { Step2_SelectDate(selectedHospital, currentMonth, selectedDate, { currentMonth = it }) { selectedDate = it } }
            item { Step3_SelectTime(selectedDate, availableTimes, selectedTime) { selectedTime = it } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarAgendamento(navController: NavController, progress: Float) {
    Column(modifier = Modifier.background(Color.White)) {
        TopAppBar(
            title = { Text("Agendar Doação", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color(0xFF990410))
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent, titleContentColor = Color(0xFF990410))
        )
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(6.dp).clip(RoundedCornerShape(4.dp)),
            color = Color(0xFF990410),
            trackColor = Color.LightGray.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun BottomBarAgendamento(navController: NavController, hospital: HospitaisCards?, date: LocalDate?, time: String?) {
    Surface(shadowElevation = 8.dp, color = Color.White) {
        Button(
            onClick = {
                navController.navigate("tela_informacao/${hospital!!.id}/$date/$time")
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp).height(52.dp).imePadding(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF990410)),
            enabled = hospital != null && date != null && time != null
        ) {
            Text("Confirmar Agendamento", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun StepContainer(stepNumber: Int, title: String, isVisible: Boolean = true, content: @Composable () -> Unit) {
    if (!isVisible) return
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Passo $stepNumber", color = Color(0xFF990410), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color.DarkGray)
        }
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

@Composable
fun Step1_SelectHospital(isLoading: Boolean, error: String?, hospitais: List<HospitaisCards>, selected: HospitaisCards?, onSelect: (HospitaisCards) -> Unit) {
    StepContainer(1, "Selecione o local") {
        when {
            isLoading -> Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            error != null -> Text(error, color = Color.Red, modifier = Modifier.padding(horizontal = 16.dp), textAlign = TextAlign.Center)
            else -> LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(hospitais) { hospital ->
                    CartaoHospital(hospital, selected?.id == hospital.id) { onSelect(it) }
                }
            }
        }
    }
}

@Composable
fun Step2_SelectDate(hospital: HospitaisCards?, currentMonth: YearMonth, selectedDate: LocalDate?, onMonthChange: (YearMonth) -> Unit, onDateSelect: (LocalDate) -> Unit) {
    StepContainer(2, "Escolha uma data", isVisible = hospital != null) {
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            CalendarioView(currentMonth, onMonthChange, selectedDate, onDateSelect)
        }
    }
}

@Composable
fun Step3_SelectTime(date: LocalDate?, times: List<String>, selectedTime: String?, onTimeSelect: (String) -> Unit) {
    StepContainer(3, "Defina um horário", isVisible = date != null) {
        if (times.isNotEmpty()) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(times) { time ->
                    ChipHorario(time, selectedTime == time) { onTimeSelect(it) }
                }
            }
        } else {
            Text("Nenhum horário disponível para este dia.", modifier = Modifier.padding(horizontal = 16.dp), color = Color.Gray)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaAgendamentoPreview() {
    TelaAgendamento(rememberNavController())
}
