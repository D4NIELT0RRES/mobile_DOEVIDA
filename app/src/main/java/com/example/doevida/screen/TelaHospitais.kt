package com.example.doevida.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.example.doevida.model.HospitalResponse
import com.example.doevida.service.RetrofitFactory
import kotlinx.coroutines.launch

@Composable
fun TelaHospitais(navController: NavController) {
    var listaHospitais by remember { mutableStateOf<List<HospitaisCards>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Carregar dados da API quando a tela for criada
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response = RetrofitFactory().getHospitalService().getHospitais()
                if (response.isSuccessful) {
                    response.body()?.let { hospitalResponse ->
                        listaHospitais = hospitalResponse.hospitais
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
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Hospitais",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.size(48.dp))
            }
        }

        // Mapa
        Image(
            painter = painterResource(id = R.drawable.mapa),
            contentDescription = "Mapa de hospitais",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )

        // Seção "Mais próximos"
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Mais próximos",
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }

        // Conteúdo principal
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF990410)
                    )
                }
            }
            errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
                                        val response = RetrofitFactory().getHospitalService().getHospitais()
                                        if (response.isSuccessful) {
                                            response.body()?.let { hospitalResponse ->
                                                listaHospitais = hospitalResponse.hospitais
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
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF990410)
                            )
                        ) {
                            Text("Tentar Novamente")
                        }
                    }
                }
            }
            listaHospitais.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhum hospital encontrado",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
            else -> {
                // Lista de hospitais
                LazyColumn {
                    items(listaHospitais) { hospital ->
                        CardsHospitais(
                            hospital = hospital,
                            onInfoClick = {
                                navController.navigate("tela_detalhe_hospital/${hospital.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardsHospitais(
    hospital: HospitaisCards,
    onInfoClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = hospital.nomeHospital,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = hospital.endereco,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = hospital.telefone,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onInfoClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Mais informações",
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Informações",
                    fontSize = 13.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview
@Composable
private fun CardsHospitaisPreview() {
    CardsHospitais(
        HospitaisCards(
            id = 1,
            nomeHospital = "Hospital Regional",
            endereco = "Av. Principal, 711 - São Paulo",
            telefone = "(11) 3373-2050"
        )
    )
}

@Preview(showSystemUi = true)
@Composable
private fun TelaHospitaisPreview() {
    TelaHospitais(navController = rememberNavController())
}