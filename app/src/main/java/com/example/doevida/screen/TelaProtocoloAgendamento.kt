package com.example.doevida.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaProtocoloAgendamento() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF990410))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Protocolo de agendamento",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Informações do usuário
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Nome: Nome do User",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "CPF: xxx.xxx.xxx-xx",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Data da doação: 05/07/2025",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Local da doação: Hospital Centra, Rua das Flores 123",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

// Conteúdo branco inferior
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    color = Color.White,
//                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
//                )
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 24.dp, vertical = 24.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = "Lembrete",
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(
//                    text = """
//                        No dia da sua doação, não se esqueça de:
//
//                        ✅ Levar um documento oficial com foto (RG ou CNH).
//                        ✅ Esteja bem alimentado e hidratado (evite jejum, comidas gordurosas e álcool nas 12h anteriores).
//                        ✅ Usar roupas confortáveis e fáceis de arregaçar a manga.
//
//                        Sua doação fará toda a diferença. Obrigado por esse gesto de amor e solidariedade!
//
//                        Se não puder comparecer, avise com 24h de antecedência para reorganizarmos os atendimentos e ajudarmos mais pessoas.
//                    """.trimIndent(),
//                    fontSize = 15.sp,
//                    color = Color.Black,
//                    textAlign = TextAlign.Start
//                )
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                Button(
//                    onClick = { /* ação de confirmar */ },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFFB71C1C)
//                    ),
//                    shape = RoundedCornerShape(12.dp),
//                    modifier = Modifier
//                        .height(48.dp)
//                        .width(180.dp)
//                ) {
//                    Text(
//                        text = "Confirmar",
//                        fontSize = 16.sp,
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun TelaProtocoloAgendamentoPreview() {
    TelaProtocoloAgendamento()
}
