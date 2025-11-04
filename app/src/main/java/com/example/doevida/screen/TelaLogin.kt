package com.example.doevida.screen

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.doevida.util.TokenManager
import com.example.doevida.util.UserDataManager // Import correto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaLogin(navController: NavController) {
    val login = remember { mutableStateOf("") }
    val senha = remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var senhaError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun validarCampos(): Boolean {
        emailError = when {
            login.value.isBlank() -> "Campo obrigatório"
            login.value.contains("@") && !Patterns.EMAIL_ADDRESS.matcher(login.value).matches() ->
                "Formato de e-mail inválido"
            else -> null
        }

        senhaError = when {
            senha.value.isBlank() -> "Campo obrigatório"
            senha.value.length < 6 -> "Senha deve ter no mínimo 8 caracteres"
            else -> null
        }

        return emailError == null && senhaError == null
    }

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
            if (emailError != null) {
                Text(emailError!!,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.Start))
            }

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
            if (senhaError != null) {
                Text(senhaError!!,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.Start))
            }

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
                    if (validarCampos()) {
                        val request = if (login.value.contains("@")) {
                            LoginRequest(email = login.value, username = null, senha = senha.value)
                        } else {
                            LoginRequest(email = null, username = login.value, senha = senha.value)
                        }

                        scope.launch(Dispatchers.IO) {
                            try {
                                val response = RetrofitFactory(context).getUserService().login(request)
                                withContext(Dispatchers.Main) {
                                    if (response.isSuccessful) {
                                        val body = response.body()
                                        if (body?.usuario != null && body.token != null) {
                                            UserDataManager.saveUserData(
                                                context,
                                                id = body.usuario.id,
                                                name = body.usuario.nome ?: "",
                                                email = body.usuario.email ?: ""
                                            )
                                            TokenManager(context).saveToken(body.token)

                                            Toast.makeText(context, "Login realizado com sucesso!", Toast.LENGTH_LONG).show()

                                            navController.navigate("tela_home") {
                                                popUpTo("tela_login") { inclusive = true }
                                            }
                                        } else {
                                            Toast.makeText(context, "Resposta inválida do servidor.", Toast.LENGTH_LONG).show()
                                        }
                                    } else {
                                        Toast.makeText(context, "Falha no login: ${response.code()}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Log.e("TelaLogin", "Erro ao conectar", e)
                                    Toast.makeText(context, "Erro ao conectar. Verifique sua conexão.", Toast.LENGTH_LONG).show()
                                }
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