package com.example.doevida.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.doevida.util.abrirNoMaps
import com.example.doevida.util.ligarPara
import kotlinx.coroutines.launch

@Composable
fun TelaDetalheHospital(navController: NavController, hospitalId: Int) {
    var hospital by remember { mutableStateOf<HospitaisCards?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    fun fetchHospitalDetails() {
        isLoading = true
        errorMessage = null
        scope.launch {
            try {
                val response = RetrofitFactory(context).getHospitalService().getHospitalById(hospitalId)
                if (response.isSuccessful) {
                    hospital = response.body()?.hospital
                } else {
                    errorMessage = "Erro ao carregar detalhes: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "Erro de conexão. Verifique sua internet."
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(hospitalId) {
        fetchHospitalDetails()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF7F7F7))
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF990410))
            }
            errorMessage != null -> {
                ErrorState(message = errorMessage!!, onRetry = { fetchHospitalDetails() })
            }
            hospital != null -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item { HospitalDetailHeader(hospital = hospital!!) }
                    item { ActionButtons(hospital = hospital!!, context = LocalContext.current) }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    
                    val horarioAbertura = formatarHorario(hospital!!.horario_abertura)
                    val horarioFechamento = formatarHorario(hospital!!.horario_fechamento)

                    if (horarioAbertura.isNotBlank() && horarioFechamento.isNotBlank()) {
                        item {
                            InfoCard(
                                icon = Icons.Default.Schedule,
                                title = "Horário de Funcionamento",
                                content = {
                                    Text("$horarioAbertura - $horarioFechamento", fontSize = 14.sp)
                                    Text("Segunda a Sexta", fontSize = 14.sp, color = Color.Gray)
                                }
                            )
                        }
                    }

                    item {
                        InfoCard(
                            icon = Icons.Default.Info,
                            title = "Informações Adicionais",
                            content = {
                                hospital!!.convenios?.let { Text("• Convênios: $it", fontSize = 14.sp) }
                                hospital!!.capacidade_maxima?.let { Text("• Capacidade: $it pessoas", fontSize = 14.sp) }
                                Text("• Estacionamento disponível", fontSize = 14.sp)
                            }
                        )
                    }
                }
            }
        }
        FloatingBackButton(onClick = { navController.popBackStack() })
    }
}

@Composable
private fun HospitalDetailHeader(hospital: HospitaisCards) {
    Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
        Image(
            painter = painterResource(id = R.drawable.hospital),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)), startY = 300f)))
        Column(
            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
        ) {
            Text(text = hospital.nomeHospital, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 26.sp, lineHeight = 30.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = hospital.endereco, color = Color.White.copy(alpha = 0.9f), fontSize = 16.sp)
        }
    }
}

@Composable
private fun ActionButtons(hospital: HospitaisCards, context: android.content.Context) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = { abrirNoMaps(context, hospital) },
            modifier = Modifier.weight(1f).height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF990410))
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Rota", fontWeight = FontWeight.Bold)
        }
        OutlinedButton(
            onClick = { ligarPara(context, hospital.telefone) },
            modifier = Modifier.weight(1f).height(50.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f))
        ) {
            Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color(0xFF990410))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ligar", fontWeight = FontWeight.Bold, color = Color.DarkGray)
        }
    }
}

@Composable
fun InfoCard(icon: ImageVector, title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF990410), modifier = Modifier.padding(top = 4.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                content()
            }
        }
    }
}

@Composable
private fun FloatingBackButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.padding(16.dp).background(Color.Black.copy(alpha = 0.4f), CircleShape).size(40.dp)
    ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
    }
}

private fun formatarHorario(horario: String?): String {
    if (horario.isNullOrBlank()) return ""
    return try {
        // Se tiver 'T', pega o que vem depois. Senão, pega a própria string.
        val timePart = if (horario.contains("T")) horario.substringAfter('T') else horario
        // Tenta pegar os primeiros 5 caracteres (HH:mm)
        timePart.take(5)
    } catch (e: Exception) {
        ""
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TelaDetalheHospitalPreview() {
    TelaDetalheHospital(navController = rememberNavController(), hospitalId = 1)
}
