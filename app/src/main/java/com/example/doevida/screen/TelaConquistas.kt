package com.example.doevida.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.service.RetrofitFactory
import com.example.doevida.service.SharedPreferencesUtils
import com.example.doevida.util.UserDataManager

// Modelo de Nível/Ranking
data class RankingLevel(
    val name: String,
    val minDonations: Int,
    val color: Color,
    val icon: ImageVector
)

val rankings = listOf(
    RankingLevel("Iniciante", 0, Color(0xFF8D6E63), Icons.Default.Star), // Marrom
    RankingLevel("Bronze", 1, Color(0xFFCD7F32), Icons.Default.EmojiEvents), // Bronze
    RankingLevel("Prata", 5, Color(0xFFC0C0C0), Icons.Default.EmojiEvents), // Prata
    RankingLevel("Ouro", 10, Color(0xFFFFD700), Icons.Default.EmojiEvents), // Ouro
    RankingLevel("Diamante", 20, Color(0xFFB9F2FF), Icons.Default.WaterDrop), // Azul Diamante
    RankingLevel("Lenda da Vida", 50, Color(0xFFE91E63), Icons.Default.Favorite) // Rosa/Vermelho
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaConquistas(navController: NavController) {
    val context = LocalContext.current
    var donationCount by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val userId = UserDataManager.getUserId(context)
        if (userId != 0) {
            try {
                val manualCount = SharedPreferencesUtils.getManualDonations(context)
                    .count { it.userId == userId }

                val apiCount = try {
                    val response = RetrofitFactory(context).getUserService().getHistorico(userId)
                    if (response.isSuccessful) response.body()?.agendamentos?.size ?: 0 else 0
                } catch (e: Exception) { 0 }

                donationCount = manualCount + apiCount
            } catch (e: Exception) {
                donationCount = 0
            }
        }
        isLoading = false
    }

    // Determinar nível atual
    val currentLevel = rankings.lastOrNull { donationCount >= it.minDonations } ?: rankings.first()
    val nextLevel = rankings.firstOrNull { donationCount < it.minDonations }

    // Estatísticas
    val vidasSalvas = donationCount * 4
    val litrosDoados = String.format("%.1f", donationCount * 0.45)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Conquistas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF7F7F7),
                    titleContentColor = Color.DarkGray
                )
            )
        },
        containerColor = Color(0xFFF7F7F7)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Cartão Principal do Nível
            item {
                CurrentRankCard(currentLevel, nextLevel, donationCount)
            }

            // Estatísticas de Impacto
            item {
                ImpactStatsRow(vidasSalvas, litrosDoados)
            }

            // Título da Jornada
            item {
                Text(
                    text = "Sua Jornada de Ranking",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            }

            // Lista de Níveis (Linha do Tempo)
            itemsIndexed(rankings) { index, level ->
                RankItem(level, donationCount, isCurrent = level == currentLevel)
                if (index < rankings.size - 1) {
                    // Linha conectora
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .width(2.dp)
                            .background(if (donationCount >= rankings[index+1].minDonations) Color(0xFF990410) else Color.LightGray)
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun CurrentRankCard(currentLevel: RankingLevel, nextLevel: RankingLevel?, donationCount: Int) {
    val progressTarget = if (nextLevel != null) {
        val range = nextLevel.minDonations - currentLevel.minDonations
        val currentInLevel = donationCount - currentLevel.minDonations
        currentInLevel.toFloat() / range.toFloat()
    } else {
        1f // Máximo atingido
    }
    
    val animatedProgress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = tween(durationMillis = 1000), label = "progress"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ícone do Nível com Brilho
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Brush.radialGradient(listOf(currentLevel.color.copy(alpha = 0.3f), currentLevel.color.copy(alpha = 0.1f))))
                    .border(4.dp, currentLevel.color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = currentLevel.icon,
                    contentDescription = null,
                    tint = currentLevel.color,
                    modifier = Modifier.size(50.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = currentLevel.name,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = currentLevel.color
            )
            
            Text(
                text = "$donationCount doações realizadas",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Barra de Progresso
            if (nextLevel != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Nível Atual", fontSize = 12.sp, color = Color.Gray)
                    Text("Próximo: ${nextLevel.name}", fontSize = 12.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = currentLevel.color,
                    trackColor = Color.LightGray.copy(alpha = 0.3f),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Faltam ${nextLevel.minDonations - donationCount} doações para evoluir",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF990410)
                )
            } else {
                Text(
                    text = "Você atingiu o ranking máximo!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = currentLevel.color
                )
            }
        }
    }
}

@Composable
fun ImpactStatsRow(vidas: Int, litros: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            value = "$vidas",
            label = "Vidas Salvas",
            icon = Icons.Default.Favorite,
            color = Color(0xFFE53935)
        )
        StatCard(
            modifier = Modifier.weight(1f),
            value = "$litros L",
            label = "Sangue Doado",
            icon = Icons.Default.WaterDrop,
            color = Color(0xFF039BE5)
        )
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, value: String, label: String, icon: ImageVector, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun RankItem(level: RankingLevel, currentDonations: Int, isCurrent: Boolean) {
    val isUnlocked = currentDonations >= level.minDonations
    val backgroundColor = if (isUnlocked) Color.White else Color(0xFFEEEEEE)
    val contentColor = if (isUnlocked) Color.DarkGray else Color.Gray
    val iconTint = if (isUnlocked) level.color else Color.Gray

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 2.dp else 0.dp),
        border = if (isCurrent) BorderStroke(2.dp, level.color) else null
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (isUnlocked) level.color.copy(alpha = 0.1f) else Color.LightGray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Icon(imageVector = level.icon, contentDescription = null, tint = iconTint)
                } else {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = level.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (isCurrent) level.color else contentColor
                )
                Text(
                    text = "${level.minDonations} doações necessárias",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            if (isCurrent) {
                Text(
                    text = "ATUAL",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = level.color,
                    modifier = Modifier
                        .background(level.color.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaConquistasPreview() {
    TelaConquistas(rememberNavController())
}
