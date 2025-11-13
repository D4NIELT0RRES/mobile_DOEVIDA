package com.example.doevida.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* 
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.doevida.model.HistoricoDoacaoCombinado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalheDoacao(navController: NavController, doacao: HistoricoDoacaoCombinado) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes da Doação") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF7F7F7))
        ) {
            if (doacao.proofImageUrl != null) {
                AsyncImage(
                    model = doacao.proofImageUrl,
                    contentDescription = "Comprovante da Doação",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.LightGray)
                ) 
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = doacao.hospitalName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFF990410)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Realizada em: ${doacao.date}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text(
                    text = "Minhas Anotações",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = doacao.observacao ?: "Nenhuma anotação adicionada.",
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }
        }
    }
}
