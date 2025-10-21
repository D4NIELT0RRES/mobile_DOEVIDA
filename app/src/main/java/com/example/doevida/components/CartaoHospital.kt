package com.example.doevida.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.doevida.R
import com.example.doevida.model.HospitaisCards
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp

@Composable
fun CartaoHospital(
    hospital: HospitaisCards,
    isSelected: Boolean,
    onHospitalSelected: (HospitaisCards) -> Unit
) {
    val borderColor = if (isSelected) Color(0xFF990410) else Color(0x26000000)

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .width(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onHospitalSelected(hospital) }
            .background(Color.White)
            .padding(10.dp)
    ) {
        coil.compose.AsyncImage(
            model = hospital.foto,
            contentDescription = "Foto do hospital",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.hospital),
            error = painterResource(R.drawable.hospital),
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(10.dp))
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = hospital.nomeHospital,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            maxLines = 1
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = hospital.endereco,
            color = Color.Gray,
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}
