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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R
import com.example.doevida.ui.theme.DoevidaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalheNoticia(navController: NavController, noticiaId: Int) {
    val noticia = NoticiaRepository.getNoticiaById(noticiaId)

    if (noticia == null) {
        // Exibir uma mensagem de erro ou tela de loading
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Notícia não encontrada.")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notícia") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                 colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(id = noticia.imageUrl),
                contentDescription = noticia.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = noticia.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Image(
                        painter = painterResource(id = noticia.authorImageUrl),
                        contentDescription = noticia.author,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = noticia.author,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )
                }
                Text(
                    text = noticia.content,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaDetalheNoticiaPreview() {
    DoevidaTheme {
        TelaDetalheNoticia(rememberNavController(), 2)
    }
}
