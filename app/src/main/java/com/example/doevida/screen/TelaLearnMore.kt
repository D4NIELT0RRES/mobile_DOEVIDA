package com.example.doevida.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaLearnMore(navController: NavController) {
    Scaffold(
        topBar = { TopBarLearnMore(navController) },
        containerColor = Color(0xFFF7F7F7)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            HeaderImage()
            Spacer(modifier = Modifier.height(16.dp))
            Content(navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarLearnMore(navController: NavController) {
    TopAppBar(
        title = { Text("Sobre o DOEVIDA", fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = Color(0xFF990410))
    )
}

@Composable
fun HeaderImage() {
    Image(
        painter = painterResource(id = R.drawable.image), // Sua ilustração
        contentDescription = "Ilustração Doação de Sangue",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun Content(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        InfoSection(
            icon = Icons.Default.Flag,
            title = "Nossa Missão",
            text = "O projeto DOEVIDA nasceu para transformar solidariedade em impacto real. Aproximamos doadores e hemocentros, tornando o processo de doação de sangue mais acessível, humano e eficiente."
        )
        Spacer(modifier = Modifier.height(16.dp))
        InfoSection(
            icon = Icons.Default.Favorite,
            title = "Por Que Doar?",
            text = "Mais do que uma plataforma, somos uma ponte entre quem quer ajudar e quem precisa. Acreditamos que cada gota conta, e que juntos podemos salvar milhares de vidas. Uma atitude sua pode salvar até quatro pessoas."
        )
        Spacer(modifier = Modifier.height(24.dp))
        QuoteSection()
        Spacer(modifier = Modifier.height(24.dp))
        ActionButton(navController)
    }
}

@Composable
fun InfoSection(icon: ImageVector, title: String, text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF990410))
                Spacer(modifier = Modifier.width(12.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.DarkGray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text, fontSize = 14.sp, color = Color.Gray, lineHeight = 20.sp)
        }
    }
}

@Composable
fun QuoteSection() {
    Text(
        text = "\"Uma atitude salva até quatro vidas, seja você essa atitude.\"",
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF990410),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    )
}

@Composable
fun ActionButton(navController: NavController) {
    Button(
        onClick = { navController.navigate("tela_cadastro") },
        modifier = Modifier.fillMaxWidth().height(52.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF990410)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text("Quero Fazer Parte!", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaLearnMorePreview() {
    TelaLearnMore(rememberNavController())
}
