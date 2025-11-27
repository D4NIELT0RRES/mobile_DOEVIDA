package com.example.doevida.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class PerguntaTriagem(
    val id: Int,
    val texto: String,
    val isImpeditivo: Boolean, // Se sim, impede a doação
    val mensagemErro: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaPreTriagem(navController: NavController) {
    val primaryColor = Color(0xFF990410)
    
    // Lista de perguntas baseadas nas regras da ANVISA/Hemocentros
    val perguntas = listOf(
        PerguntaTriagem(1, "Você pesa menos de 50kg?", true, "O peso mínimo para doar é 50kg."),
        PerguntaTriagem(2, "Fez tatuagem ou piercing nos últimos 12 meses?", true, "É necessário aguardar 12 meses após o procedimento."),
        PerguntaTriagem(3, "Teve gripe, resfriado ou febre nos últimos 7 dias?", true, "Aguarde 7 dias após o desaparecimento dos sintomas."),
        PerguntaTriagem(4, "Ingeriu bebida alcoólica nas últimas 12 horas?", true, "É necessário estar sóbrio e aguardar 12 horas."),
        PerguntaTriagem(5, "Está grávida ou amamentando?", true, "Não é permitido doar durante a gravidez ou amamentação (até 12 meses)."),
        PerguntaTriagem(6, "Realizou alguma cirurgia de grande porte nos últimos 6 meses?", true, "Dependendo da cirurgia, o prazo varia de 3 a 12 meses.")
    )

    // Estado para armazenar as respostas (Set de IDs das perguntas marcadas como SIM)
    val respostasPositivas = remember { mutableStateListOf<Int>() }
    var resultadoCalculado by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Autoavaliação", fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Card Informativo
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Importante",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE65100)
                    )
                    Text(
                        text = "Esta é uma pré-avaliação anônima para poupar seu tempo. A triagem oficial e confidencial será feita presencialmente no hospital.",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Responda com sinceridade:",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Perguntas (Switches)
            perguntas.forEach { pergunta ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = pergunta.texto,
                        modifier = Modifier.weight(1f),
                        fontSize = 15.sp,
                        color = Color.DarkGray
                    )
                    
                    Switch(
                        checked = respostasPositivas.contains(pergunta.id),
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                respostasPositivas.add(pergunta.id)
                            } else {
                                respostasPositivas.remove(pergunta.id)
                            }
                            resultadoCalculado = false // Reseta o resultado se mudar algo
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = primaryColor
                        )
                    )
                }
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { resultadoCalculado = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text("Verificar Elegibilidade", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Exibição do Resultado
            if (resultadoCalculado) {
                val impedimentos = perguntas.filter { respostasPositivas.contains(it.id) }
                
                if (impedimentos.isEmpty()) {
                    ResultadoCard(
                        titulo = "Você parece apto a doar!",
                        mensagem = "Com base nas suas respostas, você atende aos requisitos básicos. Agende sua doação!",
                        color = Color(0xFF4CAF50),
                        icon = Icons.Default.CheckCircle
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate("tela_agendamento") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Agendar Agora", color = Color.White)
                    }
                } else {
                    ResultadoCard(
                        titulo = "Atenção a alguns pontos",
                        mensagem = "Encontramos alguns fatores que podem impedir sua doação temporariamente:",
                        color = Color(0xFFF44336),
                        icon = Icons.Default.Warning
                    )
                    
                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        impedimentos.forEach { imp ->
                            Text(
                                text = "• ${imp.mensagemErro}",
                                color = Color(0xFFD32F2F),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ResultadoCard(titulo: String, mensagem: String, color: Color, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = titulo, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = mensagem, fontSize = 15.sp, color = Color.DarkGray, textAlign = TextAlign.Center)
        }
    }
}
