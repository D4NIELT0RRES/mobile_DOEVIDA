package com.example.doevida.screen


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R
import androidx.compose.ui.tooling.preview.Preview
import com.example.doevida.service.SharedPreferencesUtils
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow


@Composable
fun TelaHome(navController: NavController) {

    // Declaração de variáveis para armazenar o nome e o e-mail do usuário

    val userName = remember { mutableStateOf("") }
    val userEmail = remember { mutableStateOf("") }

    // Recuperando os dados do usuário dos SharedPreferences
    val context = LocalContext.current


    // Usando LaunchedEffect para carregar os dados assim que a TelaHome for composta
    LaunchedEffect(Unit) {
        userName.value = SharedPreferencesUtils.getUserName(context)
        userEmail.value = SharedPreferencesUtils.getUserEmail(context)
        Log.d("TelaHome", "Nome de usuário carregado: ${userName.value}")
        Log.d("TelaHome", "E-mail do usuário carregado: ${userEmail.value}")
    }

    Scaffold(
        bottomBar = {
            BarraDeNavegacao(navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF990410))
                        .padding(16.dp)
                ) {
                    Column {
                        ListItem(
                            leadingContent = {
                                Box(
                                    Modifier
                                        .size(55.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                            },
                            headlineContent = {
                                Text(
                                    text = userName.value,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = userEmail.value,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                contentAlignment = Alignment.Center
                            ){
                                Row (
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ){
                                    Box(
                                        modifier = Modifier
                                            .size(70.dp)
                                            .border(
                                                width = 3.dp,
                                                color = Color.White,
                                                shape = CircleShape
                                            )
                                            .clip(CircleShape),
                                        contentAlignment = Alignment.Center
                                    ){
                                        Text(
                                            text = "13",
                                            color = Color.White,
                                            fontSize = 36.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = "Dias restantes para\na próxima doação",
                                        color = Color.White,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))

                //barra de pesquisa
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Find Seekers") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF990410)
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )


                Spacer(modifier = Modifier.height(50.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CardButton(
                            "Doar Sangue",
                            R.drawable.doarsangue,
                            navController,
                            "tela_agendamento"
                        )
                        CardButton(
                            "Hospitais",
                            R.drawable.hospitais,
                            navController,
                            "tela_hospitais"
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CardButton(
                            "Banco de Sangue",
                            R.drawable.bancodesangue,
                            navController,
                            "tela_banco_sangue"
                        )
                        CardButton(
                            "Histórico",
                            R.drawable.historicos,
                            navController,
                            "tela_historico"
                        )
                    }

                    CardButton(
                        "Registrar Doação",
                        R.drawable.doarsangue,
                        navController,
                        " " // arrumar dps de pronto
                    )
                }

                    Spacer(modifier = Modifier.weight(1f))

                HorizontalDivider(
                    modifier = Modifier
                        .padding(
                            top = 10.dp,
                            start = 6.dp,
                            end = 6.dp
                        ),
                    color = Color(0xFFD9D9D9)
                )
            }
        }
    }
}
//-------------------------------------
@Composable
fun CardButton(text: String, imageRes: Int, navController: NavController, rota: String) {
    Card(
        modifier = Modifier
            .size(width = 150.dp, height = 90.dp)
            .clickable { navController.navigate(rota) }, //navegação
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = text,
                modifier = Modifier.size(50.dp)
            )

            Text(
                text = text,
                color = Color(0xFFB71C1C),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
//-------------------------------------
@Composable
fun BarraDeNavegacao(navController: NavController) {
    var selectedItem by remember { mutableStateOf("home") }

    NavigationBar(
        containerColor = Color.White
    ) {
        val items = listOf(
            "home" to Icons.Default.Home,
            "noticias" to Icons.Default.Newspaper,
            "profile" to Icons.Default.Person
        )

        items.forEach { (route, icon) ->
            NavigationBarItem(
                selected = selectedItem == route,
                onClick = {
                    selectedItem = route
                    navController.navigate(route)
                },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = route,
                                tint = if (selectedItem == route)
                                    Color.White
                                else Color.Black
                            )
                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = route.replaceFirstChar { it.uppercase() },
                                color = if (selectedItem == route)
                                    Color.White
                                else Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (selectedItem == route) {

                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                },
                alwaysShowLabel = false,
                label = {
                    Spacer(modifier = Modifier.height(0.dp)) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFF990410)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TelaHomePreview() {
    val navController = rememberNavController()
    TelaHome(navController = navController)
}