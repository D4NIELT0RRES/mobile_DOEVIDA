package com.example.doevida.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.ui.theme.DoevidaTheme

@Composable
fun TelaDetalheNoticia(navController: NavController, noticiaId: Int) {
    val noticia = NoticiaRepository.getNoticiaById(noticiaId)

    if (noticia == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Notícia não encontrada.")
            IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.TopStart)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            NewsDetailHeader(noticia)
            NewsDetailBody(noticia)
        }

        FloatingBackButton(onClick = { navController.popBackStack() })
    }
}

@Composable
private fun FloatingBackButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .padding(16.dp)
            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
            .size(40.dp)
    ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
    }
}

@Composable
private fun NewsDetailHeader(noticia: Noticia) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Image(
            painter = painterResource(id = noticia.imageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                        startY = 350f
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = noticia.category.uppercase(),
                color = Color.White.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = noticia.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 28.sp
            )
        }
    }
}

@Composable
private fun NewsDetailBody(noticia: Noticia) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Image(
                painter = painterResource(id = noticia.authorImageUrl),
                contentDescription = noticia.author,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = noticia.author,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray,
                    fontSize = 16.sp
                )
                Text(
                    text = "Fonte: ${noticia.source}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
        Divider(
            color = Color.LightGray.copy(alpha = 0.5f),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Text(
            text = noticia.content + "\n\nPara ilustrar o layout com um artigo mais longo, adicionamos este texto. Em um aplicativo real, todo o conteúdo viria da notícia. A doação de sangue é um ato de solidariedade que pode salvar até quatro vidas com uma única bolsa. O processo é rápido, seguro e essencial para manter os estoques de sangue em hospitais, atendendo a emergências, cirurgias e tratamentos de doenças crônicas.",
            fontSize = 17.sp,
            lineHeight = 26.sp,
            color = Color.DarkGray.copy(alpha = 0.9f)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TelaDetalheNoticiaPreview() {
    DoevidaTheme {
        TelaDetalheNoticia(rememberNavController(), 2)
    }
}
