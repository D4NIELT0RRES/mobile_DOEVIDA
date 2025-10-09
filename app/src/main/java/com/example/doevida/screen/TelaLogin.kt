package com.example.doevida.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R
import com.example.doevida.model.LoginRequest
import com.example.doevida.service.RetrofitFactory
import com.example.doevida.service.SharedPreferencesUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaLogin(navController: NavController) {
    val login = remember { mutableStateOf("") }
    val senha = remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .size(270.dp)
                .offset(x = (-100).dp, y = (-120).dp)
                .background(
                    color = Color(0xFF990410),
                    shape = CircleShape
                )
        )
        IconButton(
            onClick = { navController.navigate("tela_inicial") },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.voltar),
                contentDescription = "Voltar",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logologin),
                contentDescription = "Logo DOEVIDA",
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = 32.dp)
            )

            Text(
                text = "Digite seu Email ou Usuário",
                fontSize = 14.sp,
                color = Color(0xFF990410),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            OutlinedTextField(
                value = login.value,
                onValueChange = { login.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(56.dp),
                placeholder = { Text("Email ou Usuário", color = Color.White) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF990410),
                    unfocusedContainerColor = Color(0xFF990410),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.perfil),
                        contentDescription = "Ícone de usuário",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )

            Text(
                text = "Digite sua Senha",
                fontSize = 14.sp,
                color = Color(0xFF990410),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            OutlinedTextField(
                value = senha.value,
                onValueChange = { senha.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(56.dp),
                placeholder = { Text("Senha", color = Color.White) },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF990410),
                    unfocusedContainerColor = Color(0xFF990410),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.senha),
                        contentDescription = "Ícone de senha",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            )

            Text(
                text = "Esqueci minha senha?",
                color = Color(0xFF990410),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { navController.navigate(route = "tela_recuperacao") }
                    .padding(top = 8.dp, bottom = 32.dp),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    val request = if (login.value.contains("@")) {
                        LoginRequest(email = login.value, username = null, senha = senha.value)
                    } else {
                        LoginRequest(email = null, username = login.value, senha = senha.value)
                    }

                    scope.launch(Dispatchers.IO) {
                        try {
                            val response = RetrofitFactory().getUserService().login(request)

                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    val body = response.body()

                                    // ===== ADICIONADO: LOGS DE DEBUG =====
                                    println("=== DEBUG LOGIN ===")
                                    println("Email/Username digitado: ${login.value}")
                                    println("Resposta da API: $body")
                                    println("Nome retornado: ${body?.usuario?.nome}")
                                    println("Email retornado: ${body?.usuario?.email}")
                                    println("==================")

                                    // ===== CORRIGIDO: SALVANDO DADOS DO USUÁRIO =====
                                    if (body?.usuario != null) {
                                        SharedPreferencesUtils.saveUserData(
                                            context = context,
                                            userName = body.usuario.nome ?: "Usuário",
                                            userEmail = body.usuario.email ?: "email@exemplo.com"
                                        )
                                        println("Dados salvos no SharedPreferences!")
                                    }

                                    Toast.makeText(
                                        context,
                                        "Login realizado com sucesso!",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate("tela_home") {
                                        popUpTo("tela_login") { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Falha no login: ${response.code()}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "Erro ao conectar: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF990410)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(48.dp)
                    .width(130.dp)
            ) {
                Text(
                    text = "Entrar",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = "Não tem uma conta?",
                color = Color(0xFF990410),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 32.dp)
            )
            Text(
                text = "Fazer cadastro",
                color = Color(0xFF990410),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable { navController.navigate(route = "tela_cadastro") }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TelaLoginPreview() {
    val navController = rememberNavController()
    TelaLogin(navController = navController)
}
