package com.example.doevida.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doevida.service.SharedPreferencesUtils

@Composable
fun TelaProtocoloAgendamento() {
    val userName = remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        userName.value = SharedPreferencesUtils.getUserName(context)
        Log.d("TelaHome", "Nome de usuário carregado: ${userName.value}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF990410)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Protocolo de agendamento",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "Nome: ${userName.value}",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "CPF: xxx.xxx.xxx-xx",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Data da doação: 05/07/2025",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Local da doação: Hospital Central, Rua das Flores 123",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Lembrete",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = """
                    No dia da sua doação, não se esqueça de:
                    
                    ✅ Levar um documento oficial com foto (RG ou CNH).
                    ✅ Esteja bem alimentado e hidratado (evite jejum, comidas gordurosas e álcool nas 12h anteriores).
                    ✅ Usar roupas confortáveis e fáceis de arregaçar a manga.
                    
                    Sua doação fará toda a diferença. Obrigado por esse gesto de amor e solidariedade!
                    
                    Se não puder comparecer, avise com 24h de antecedência para reorganizarmos os atendimentos e ajudarmos mais pessoas.
                """.trimIndent(), //deixa o texto multilinha
                fontSize = 15.sp,
                color = Color.Black,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(100.dp))

            Button(
                onClick = { /* ação do botão Confirmar */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF990410)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .width(180.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Confirmar",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun TelaProtocoloAgendamentoPreview() {
    TelaProtocoloAgendamento()
}
