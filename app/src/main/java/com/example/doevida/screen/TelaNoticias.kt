package com.example.doevida.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R
import com.example.doevida.components.MenuInferior
import com.example.doevida.ui.theme.DoevidaTheme

// --- MOCK DATA ---
data class Noticia(
    val id: Int,
    val title: String,
    val category: String,
    val source: String,
    val imageUrl: Int, // Usando Int para recursos drawable mocados
    val content: String,
    val author: String,
    val authorImageUrl: Int
)

// Mock data atualizado para usar ícones
val noticiasDestacadas = listOf(
    Noticia(1, "Doe Sangue, Salve Vidas: A Importância da Doação Regular", "Campanha", "sindalesc.org.br", R.drawable.doarsangue, "Conteúdo da notícia 1...", "Redação", R.drawable.hospitais),
    Noticia(2, "Banco de sangue registra movimentação de bolsas para atender hospitais", "Urgente", "g1.globo.com", R.drawable.doarsangue, "A Câmara Municipal de Barueri encerrou a campanha Junho Vermelho...", "Prefeitura de Barueri", R.drawable.hospitais),
    Noticia(3, "Novas regras para doação de sangue entram em vigor", "Saúde", "saude.gov.br", R.drawable.doarsangue, "Conteúdo da notícia 3...", "Ministério da Saúde", R.drawable.hospitais)
)

val noticiasRecomendadas = listOf(
    Noticia(4, "Junho Vermelho é mês de incentivo à doação de sangue.", "Campanha", "sindalesc.org.br", R.drawable.doarsangue, "Conteúdo da notícia 4...", "Redação", R.drawable.hospitais),
    Noticia(5, "Ministério da Saúde lança campanha para incentivar doação regular de sangue.", "Campanha", "www.gov.br", R.drawable.hospitais, "Conteúdo da notícia 5...", "Redação", R.drawable.hospitais),
    Noticia(6, "Mpox deixa de ser emergência de saúde internacional, anuncia OMS.", "Curiosidade", "g1.globo.com", R.drawable.bancodesangue, "Conteúdo da notícia 6...", "Redação", R.drawable.hospitais),
)

// Repositório simples para acessar os dados
object NoticiaRepository {
    private val allNoticias = noticiasDestacadas + noticiasRecomendadas
    fun getNoticiaById(id: Int): Noticia? {
        return allNoticias.find { it.id == id }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TelaNoticias(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Notícias",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF8B0000),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = { MenuInferior(navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // Carrossel de notícias
            item {
                val pagerState = rememberPagerState(pageCount = { noticiasDestacadas.size })
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp),
                ) { page ->
                    NoticiaCardDestaque(noticia = noticiasDestacadas[page], navController = navController)
                }
                // Indicador de página
                Row(
                    Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color = if (pagerState.currentPage == iteration) Color(0xFF990410) else Color.LightGray
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(10.dp)
                        )
                    }
                }
            }

            // Seção de recomendados
            item {
                Text(
                    text = "Recomendados",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                    color = Color.Black
                )
            }

            items(noticiasRecomendadas) { noticia ->
                NoticiaItemRecomendado(noticia = noticia, navController = navController)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun NoticiaCardDestaque(noticia: Noticia, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxHeight()
            .aspectRatio(0.9f)
            .clickable { navController.navigate("tela_detalhe_noticia/${noticia.id}") },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)) // Fundo cinza claro
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = noticia.imageUrl),
                contentDescription = noticia.title,
                contentScale = ContentScale.Fit, // Ajusta a imagem (ícone) para caber
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp), // Adiciona preenchimento para o ícone não colar nas bordas
                colorFilter = ColorFilter.tint(Color(0xFF990410)) // Pinta o ícone de vermelho
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 300f // Ajusta o início do gradiente
                        )
                    )
            )
            Text(
                text = noticia.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun NoticiaItemRecomendado(noticia: Noticia, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("tela_detalhe_noticia/${noticia.id}") }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Usa Icon em vez de Image para melhor controle de tint e tamanho
        Icon(
            painter = painterResource(id = noticia.imageUrl),
            contentDescription = noticia.title,
            tint = Color(0xFF990410), // Pinta o ícone de vermelho
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = noticia.category,
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = noticia.title,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = noticia.source,
                color = Color(0xFF007BFF), // Cor de link azul
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaNoticiasPreview() {
    DoevidaTheme {
        TelaNoticias(rememberNavController())
    }
}
