package com.example.doevida.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProtocoloInfoSection(
    nome: String,
    cpf: String,
    data: String,
    local: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF9A0410))
            .padding(24.dp)
    ) {
        Text(
            text = "Protocolo de agendamento",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        ProtocoloInfoLinha(label = "Nome:", value = nome)
        ProtocoloInfoLinha(label = "CPF:", value = cpf)
        ProtocoloInfoLinha(label = "Data da doação:", value = data)
        ProtocoloInfoLinha(label = "Local da doação:", value = local)
    }
}

@Composable
private fun ProtocoloInfoLinha(label: String, value: String) {
    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(text = label, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = value, color = Color.White, fontSize = 16.sp)
    }
}

@Composable
fun LembreteCard(onConfirm: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Lembrete", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No dia da sua doação, não se esqueça de:",
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Itens do Lembrete
            LembreteItem(text = "Levar um documento oficial com foto (RG ou CNH).")
            LembreteItem(text = "Esteja bem alimentado e hidratado (evite jejum, comidas gordurosas e álcool nas 12h anteriores).")
            LembreteItem(text = "Usar roupas confortáveis e fáceis de arregaçar a manga.")

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Sua doação fará toda a diferença. Obrigado por esse gesto de amor e solidariedade!",
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Se não puder comparecer, avise com 24h de antecedência para reorganizarmos os atendimentos e ajudarmos mais pessoas.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9A0410)),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(48.dp)
            ) {
                Text("Confirmar", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun LembreteItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.CheckBox,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(20.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 14.sp, color = Color.Black)
    }
}
