package com.example.doevida.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

data class BloodTypeStock(val type: String, val level: Float, val status: BloodStatus)
enum class BloodStatus(val color: Color, val label: String) {
    STABLE(Color(0xFF4CAF50), "Estável"),
    LOW(Color(0xFFFFC107), "Baixo"),
    CRITICAL(Color(0xFFF44336), "Crítico")
}

val bloodStockLevels = listOf(
    BloodTypeStock("A+", 0.6f, BloodStatus.LOW),
    BloodTypeStock("A-", 0.9f, BloodStatus.STABLE),
    BloodTypeStock("B+", 0.5f, BloodStatus.LOW),
    BloodTypeStock("B-", 0.45f, BloodStatus.CRITICAL),
    BloodTypeStock("AB+", 0.4f, BloodStatus.CRITICAL),
    BloodTypeStock("AB-", 0.6f, BloodStatus.LOW),
    BloodTypeStock("O+", 0.37f, BloodStatus.CRITICAL),
    BloodTypeStock("O-", 0.29f, BloodStatus.CRITICAL)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaBancodeSangue(navController: NavController) {
    Scaffold(
        topBar = { TopBarBancoSangue(navController) },
        bottomBar = { BottomBarBancoSangue(navController) },
        containerColor = Color(0xFFF7F7F7)
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(paddingValues).padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            content = {
                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) { StatusLegend() }
                items(bloodStockLevels) { stock ->
                    BloodStockCard(stock = stock)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarBancoSangue(navController: NavController) {
    TopAppBar(
        title = { Text("Banco de Sangue", fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = Color(0xFF990410))
    )
}

@Composable
fun StatusLegend() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end=16.dp, top=8.dp, bottom=16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BloodStatus.values().forEach { status ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).background(status.color, CircleShape))
                Spacer(modifier = Modifier.width(6.dp))
                Text(status.label, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun BloodStockCard(stock: BloodTypeStock) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stock.type, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(12.dp))
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = 1f,
                    modifier = Modifier.size(70.dp),
                    color = stock.status.color.copy(alpha = 0.2f),
                    strokeWidth = 8.dp
                )
                CircularProgressIndicator(
                    progress = stock.level,
                    modifier = Modifier.size(70.dp),
                    color = stock.status.color,
                    strokeWidth = 8.dp
                )
                Text("${(stock.level * 100).toInt()}%", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                stock.status.label,
                color = stock.status.color,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.background(stock.status.color.copy(alpha = 0.1f), RoundedCornerShape(50)).padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun BottomBarBancoSangue(navController: NavController) {
    val criticalType = bloodStockLevels.filter { it.status == BloodStatus.CRITICAL }.maxByOrNull { it.level }

    Surface(shadowElevation = 8.dp, color = Color.White) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (criticalType != null) {
                Text(
                    text = "Atenção! Nível de sangue ${criticalType.type} está crítico.",
                    color = BloodStatus.CRITICAL.color,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }
            Button(
                onClick = { navController.navigate("tela_agendamento") },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF990410)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Agendar Doação", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaBancodeSanguePreview() {
    TelaBancodeSangue(navController = rememberNavController())
}
