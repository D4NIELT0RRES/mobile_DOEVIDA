package com.example.doevida.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.doevida.util.abrirNoMaps
import com.example.doevida.util.ligarPara
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaHospitais(navController: NavController) {
    var listaHospitais by remember { mutableStateOf<List<HospitaisCards>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    fun fetchHospitais() {
        isLoading = true
        errorMessage = null
        scope.launch {
            try {
                val response = RetrofitFactory(context).getHospitalService().getHospitais()
                if (response.isSuccessful) {
                    listaHospitais = response.body()?.hospitais ?: emptyList()
                } else {
                    errorMessage = "Erro ao carregar hospitais: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage = "Erro de conexão. Verifique sua internet."
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        fetchHospitais()
    }

    Scaffold(
        topBar = { TopBarHospitais(navController) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color(0xFFF7F7F7))
        ) {
            HeaderMapa()

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF990410))
                    }
                }
                errorMessage != null -> {
                    ErrorState(errorMessage!!) { fetchHospitais() }
                }
                listaHospitais.isEmpty() -> {
                    EmptyStateHospitais()
                }
                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(listaHospitais) { hospital ->
                            HospitalCard(
                                hospital = hospital,
                                onInfoClick = { navController.navigate("tela_detalhe_hospital/${hospital.id}") },
                                onOpenMaps = { abrirNoMaps(context, hospital) },
                                onCall = { ligarPara(context, hospital.telefone) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHospitais(navController: NavController) {
    TopAppBar(
        title = { Text("Hospitais Próximos", fontWeight = FontWeight.Bold, fontSize = 22.sp) },
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
fun HeaderMapa() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)) {
        Image(
            painter = painterResource(id = R.drawable.mapa),
            contentDescription = "Mapa de hospitais",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent),
                        startY = 0f, endY = 200f
                    )
                )
        )
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message, color = Color.Gray, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF990410))) {
            Text("Tentar Novamente")
        }
    }
}

@Composable
fun EmptyStateHospitais() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(60.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Nenhum hospital encontrado", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("Não foi possível localizar hospitais no momento.", color = Color.Gray, textAlign = TextAlign.Center)
    }
}


@Composable
fun HospitalCard(
    hospital: HospitaisCards,
    onInfoClick: () -> Unit,
    onOpenMaps: () -> Unit,
    onCall: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onInfoClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    painter = painterResource(id = R.drawable.hospitais),
                    contentDescription = null,
                    tint = Color(0xFF990410),
                    modifier = Modifier.size(32.dp).padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = hospital.nomeHospital.takeIf { !it.isNullOrBlank() } ?: "Hospital não informado",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = hospital.endereco ?: "Endereço não disponível",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 2
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onOpenMaps,
                    border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Ver Rota", modifier = Modifier.size(18.dp), tint = Color(0xFF990410))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ver Rota", color = Color.DarkGray, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onCall,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF990410)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = "Ligar", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ligar", fontSize = 12.sp)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TelaHospitaisPreview() {
    TelaHospitais(navController = rememberNavController())
}
