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
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
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
import com.example.doevida.R
import com.example.doevida.components.MenuInferior

// --- MOCK DATA NOTÍCIAS ---
data class Noticia(
    val id: Int,
    val title: String,
    val category: String,
    val source: String,
    val imageUrl: Int,
    val content: String,
    val author: String,
    val authorImageUrl: Int
)

val noticiasDestacadas = listOf(
    Noticia(1, "Doe Sangue, Salve Vidas: A Importância da Doação Regular", "Campanha", "sindalesc.org.br", R.drawable.foto1, "Conteúdo da notícia 1...", "Redação", R.drawable.hospitais),
    Noticia(2, "Banco de sangue registra movimentação de bolsas para atender hospitais", "Urgente", "g1.globo.com", R.drawable.foto2, "A Câmara Municipal de Barueri encerrou a campanha Junho Vermelho...", "Prefeitura de Barueri", R.drawable.hospitais),
    Noticia(3, "Novas regras para doação de sangue entram em vigor", "Saúde", "saude.gov.br", R.drawable.foto3, "Conteúdo da notícia 3...", "Ministério da Saúde", R.drawable.hospitais)
)

val noticiasRecomendadas = listOf(
    Noticia(4, "Junho Vermelho é mês de incentivo à doação de sangue.", "Campanha", "sindalesc.org.br", R.drawable.doarsangue, "Conteúdo da notícia 4...", "Redação", R.drawable.hospitais),
    Noticia(5, "Ministério da Saúde lança campanha para incentivar doação regular de sangue.", "Campanha", "www.gov.br", R.drawable.hospitais, "Conteúdo da notícia 5...", "Redação", R.drawable.hospitais),
    Noticia(6, "Mpox deixa de ser emergência de saúde internacional, anuncia OMS.", "Curiosidade", "g1.globo.com", R.drawable.bancodesangue, "Conteúdo da notícia 6...", "Redação", R.drawable.hospitais),
)

object NoticiaRepository {
    private val allNoticias = noticiasDestacadas + noticiasRecomendadas
    fun getNoticiaById(id: Int): Noticia? {
        return allNoticias.find { it.id == id }
    }
}

// --- DADOS DE CURIOSIDADES ---
data class Curiosidade(
    val title: String,
    val description: String
)

val curiosidadesList = listOf(
    Curiosidade("Salva múltiplas vidas", "Uma única doação pode ser dividida em até quatro componentes sanguíneos (hemácias, plasma, plaquetas e crioprecipitado), cada um sendo usado em pacientes diferentes."),
    Curiosidade("Recuperação rápida", "O corpo repõe o volume de sangue doado em até 72 horas. A quantidade retirada representa menos de 10% do volume total de sangue do doador."),
    Curiosidade("Não há risco de contaminação", "Todo o material utilizado durante o processo é estéril e descartável, evitando qualquer risco de contaminação para o doador."),
    Curiosidade("Segurança do processo", "A doação é um procedimento simples, rápido e seguro, realizado por profissionais capacitados em locais adequados, com aferição da pressão, verificação de anemia e análise do histórico de saúde."),
    Curiosidade("Doador universal", "O tipo de sangue O- é considerado o doador universal, pois pode ser transfundido para pacientes de todos os outros tipos sanguíneos."),
    Curiosidade("O que pode impedir a doação temporariamente", "Se a pessoa estiver gripada ou resfriada, precisará esperar 7 dias após o fim dos sintomas."),
    Curiosidade("O que impede a doação", "Tatuagens ou piercings são restrições temporárias que exigem um período de espera de 6 a 12 meses, dependendo do local onde foram feitos, devido ao risco de infecções."),
    Curiosidade("Frequência da doação", "Homens podem doar até 4 vezes por ano, com intervalo de 60 dias, e mulheres até 3 vezes por ano, com intervalo de 90 dias.")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaNoticias(navController: NavController) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Notícias", "Curiosidades")

    Scaffold(
        topBar = { NoticiasTopAppBar() },
        bottomBar = { MenuInferior(navController) }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color(0xFFF7F7F7))
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Color(0xFF990410),
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color(0xFF990410)
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title, fontWeight = FontWeight.SemiBold) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }

            when (selectedTab) {
                0 -> NoticiasContent(navController)
                1 -> CuriosidadesContent()
            }
        }
    }
}

@Composable
fun NoticiasContent(navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item { HighlightsSection(navController = navController) }
        item { RecommendedSection(navController = navController) }
    }
}

@Composable
fun CuriosidadesContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF990410)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Você sabia?",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Uma única doação de sangue pode salvar até quatro vidas, pois o volume coletado (cerca de 450 ml) é fracionado em componentes como plaquetas, hemácias e plasma.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 16.sp,
                        lineHeight = 22.sp
                    )
                }
            }
        }
        items(curiosidadesList) { curiosidade ->
            CuriosidadeCard(curiosidade)
        }
    }
}

@Composable
fun CuriosidadeCard(curiosidade: Curiosidade) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = curiosidade.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF990410)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = curiosidade.description,
                fontSize = 15.sp,
                color = Color.DarkGray,
                lineHeight = 22.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoticiasTopAppBar() {
    TopAppBar(
        title = { Text("Informativo", fontWeight = FontWeight.Bold, fontSize = 24.sp) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color(0xFF990410)
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HighlightsSection(navController: NavController) {
    val pagerState = rememberPagerState { noticiasDestacadas.size }
    Column(modifier = Modifier.background(Color.White).padding(bottom = 16.dp)) {
        Text(
            text = "Destaques",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 12.dp
        ) { page ->
            NoticiaDestaqueCard(noticia = noticiasDestacadas[page], navController = navController)
        }
        Row(
            Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color(0xFF990410) else Color.LightGray
                Box(
                    modifier = Modifier.padding(4.dp).clip(CircleShape).background(color).size(8.dp)
                )
            }
        }
    }
}

@Composable
fun NoticiaDestaqueCard(noticia: Noticia, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { navController.navigate("tela_detalhe_noticia/${noticia.id}") },
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = noticia.imageUrl),
                contentDescription = noticia.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 300f
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
                    fontSize = 12.sp
                )
                Text(
                    text = noticia.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun RecommendedSection(navController: NavController) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = "Recomendados",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
        )
        LazyColumn(
            modifier = Modifier.heightIn(max = 1000.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(noticiasRecomendadas) { noticia ->
                NoticiaRecomendadoItem(noticia = noticia, navController = navController)
            }
        }
    }
}

@Composable
fun NoticiaRecomendadoItem(noticia: Noticia, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("tela_detalhe_noticia/${noticia.id}") },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = noticia.imageUrl),
                contentDescription = noticia.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(70.dp).clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = noticia.category.uppercase(),
                    color = Color(0xFF990410),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = noticia.title,
                    color = Color.DarkGray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = noticia.source,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TelaNoticiasPreview() {
    TelaNoticias(rememberNavController())
}
