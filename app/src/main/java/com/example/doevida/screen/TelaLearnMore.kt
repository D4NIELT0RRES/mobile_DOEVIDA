package com.example.doevida.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doevida.R

@Composable
fun TelaLearnMore() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Bolinhas decorativas
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFCDD2))
                .align(Alignment.TopStart)
                .offset((-40).dp, 60.dp)
        )
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFCDD2))
                .align(Alignment.TopEnd)
                .offset(40.dp, 180.dp)
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFCDD2))
                .align(Alignment.BottomStart)
                .offset((-30).dp, 20.dp)
        )
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFCDD2))
                .align(Alignment.BottomEnd)
                .offset(30.dp, 60.dp)
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top vermelho com logo centralizado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF990410),
                        shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                    )
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logocadastro), // ícone gota
                        contentDescription = "Logo",
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DOEVIDA",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título "Learn More"
            Text(
                text = "Learn More",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF990410),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Imagem central
            Image(
                painter = painterResource(id = R.drawable.image), // sua ilustração
                contentDescription = "Ilustração Doação",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 32.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Texto explicativo com negrito
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("O projeto DOEVIDA nasceu da vontade de transformar solidariedade em impacto real. ")
                    }
                    append("Percebemos que muitas pessoas têm o desejo de doar sangue, mas nem sempre sabem como, quando ou onde. Foi assim que criamos essa iniciativa para aproximar doadores e hemocentros, tornando o processo mais acessível, humano e eficiente.\n\n")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Mais do que uma plataforma, somos uma ponte entre quem quer ajudar e quem precisa. ")
                    }
                    append("Acreditamos que cada gota conta, e que juntos podemos salvar milhares de vidas.")
                },
                fontSize = 16.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Frase final em vermelho
            Text(
                text = "\"Uma atitude salva até quatro vidas, seja você essa atitude.\"",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF990410),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaLearnMorePreview() {
    TelaLearnMore()
}
