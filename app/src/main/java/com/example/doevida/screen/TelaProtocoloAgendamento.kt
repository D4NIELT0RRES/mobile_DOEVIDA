package com.example.doevida.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
            .background(Color(0xFF990410))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Protocolo de agendamento",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = userName.value,
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

        Spacer(modifier = Modifier.height(32.dp))

    }
}


@Preview(showSystemUi = true)
@Composable
fun TelaProtocoloAgendamentoPreview() {
    TelaProtocoloAgendamento()
}
