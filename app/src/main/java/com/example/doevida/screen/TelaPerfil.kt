package com.example.doevida.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.service.SharedPreferencesUtils

@Composable
fun TelaPerfil(navController: NavController) {
    val userName = remember { mutableStateOf("") }

    val context = LocalContext.current

    // Usando LaunchedEffect para carregar os dados assim que a TelaHome for composta
    LaunchedEffect(Unit) {
        userName.value = SharedPreferencesUtils.getUserName(context)
        Log.d("TelaHome", "Nome de usuário carregado: ${userName.value}")
    }

    Scaffold(
        bottomBar = {
            Column {
                Divider(
                    color = Color(0xFFD9D9D9),
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                MenuInferior(navController)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF8B0000))
                    .padding(bottom = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = userName.value,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Nome de usuário",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Divider(
                        color = Color.White
                            .copy(alpha = 0.1f), // quase transparente
                        thickness = 1.dp
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp))

                    Text(
                        "Total de Doações:",
                        color = Color.White)

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        "Doações este ano:",
                        color = Color.White)
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(
                                topStart = 50.dp,
                                topEnd = 50.dp
                            )
                        )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    "Dados Pessoais",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B0000)
                )
                DadosPessoaisCard()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* ação ao clicar em Certificados */ }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Certificados",
                    fontSize = 20.sp,
                    color = Color(0xFF8B0000),
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    modifier = Modifier
                        .size(25.dp),
                    contentDescription = "Ir para certificados",
                    tint = Color(0xFF8B0000)
                )
            }

            Divider(
                color = Color(0xFF8B0000),
                thickness = 1.dp,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* ação ao clicar em Sair */ }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Sair",
                    tint = Color(0xFF8B0000),
                    modifier = Modifier
                        .size(27.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sair",
                    color = Color(0xFF8B0000),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}
//-------------------------------------
@Composable
fun MenuInferior(navController: NavController) {
    var selectedItem by remember { mutableStateOf("profile") }

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
//-------------------------------------
@Composable
fun DadosPessoaisCard() {
    var isEditing by remember { mutableStateOf(false) }

    var nome by remember { mutableStateOf("Rafaella Toscano") }
    var email by remember { mutableStateOf("rafaella@email.com") }
    var cpf by remember { mutableStateOf("123.456.789-00") }
    var cep by remember { mutableStateOf("12345-678") }
    var dataNascimento by remember { mutableStateOf("01/01/2005") }
    var celular by remember { mutableStateOf("(11) 91234-5678") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDADADA))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp))
        {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { isEditing = !isEditing }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar Dados Pessoais",
                        tint = Color(0xFF8B0000),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            EditableFieldStyled("Nome", nome, { nome = it }, isEditing)
            EditableFieldStyled("Email", email, { email = it }, isEditing)
            EditableFieldStyled("CPF", cpf, { cpf = it }, isEditing)
            EditableFieldStyled("Cep", cep, { cep = it }, isEditing)
            EditableFieldStyled("Data de Nascimento", dataNascimento, { dataNascimento = it }, isEditing)
            EditableFieldStyled("Celular", celular, { celular = it }, isEditing)
        }
    }
}
//-------------------------------------
@Composable
fun EditableFieldStyled(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditing: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        if (isEditing) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(label, color = Color(0xFF8B0000)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
            )
        } else {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .background(Color(0xFFE0E0E0),
                        RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "$label: $value",
                    color = Color.Black,
                    fontSize = 14.sp)
            }
        }
    }
}
//-------------------------------------
@Composable
fun BarraInterior(navController: NavController) {

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
//-------------------------------------
@Preview(showBackground = true)
@Composable
fun PreviewDadosPessoaisCard() {
    DadosPessoaisCard()
}
//-------------------------------------
@Preview
@Composable
private fun TelaPerfilPreview() {
    val navController = rememberNavController()
    TelaPerfil(navController = navController)
}