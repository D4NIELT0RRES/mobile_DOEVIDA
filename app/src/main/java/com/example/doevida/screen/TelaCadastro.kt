package com.example.doevida.screen

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R
import com.example.doevida.model.Cadastro
import com.example.doevida.service.RetrofitFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun TelaCadastro(navController: NavController) {

    val doevidaApi = RetrofitFactory().getUserService()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var nomeCompleto = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val senha = remember { mutableStateOf("") }
    val confirmarSenha = remember { mutableStateOf("") }

    //mensagens de erro
    var isNomeError by remember { mutableStateOf<String?>(null) }
    var isEmailError by remember { mutableStateOf<String?>(null) }
    var isSenhaError by remember { mutableStateOf<String?>(null) }
    var isConfirmarSenhaError by remember { mutableStateOf<String?>(null) }

    fun validarCadastro(): Boolean {
        isNomeError = when {
            nomeCompleto.value.isBlank() -> "Campo obrigatório"
            nomeCompleto.value.length < 2 -> "Nome deve ser completo"
            else -> null
        }
        isEmailError = when {
            email.value.isBlank() -> "Campo obrigatório"
            !Patterns.EMAIL_ADDRESS.matcher(email.value).matches() -> "Formato inválido"
            else -> null
        }
        isSenhaError = when {
            senha.value.isBlank() -> "Campo obrigatório"
            senha.value.length < 6 -> "Senha fraca"
            else -> null
        }
        isConfirmarSenhaError = when {
            confirmarSenha.value.isBlank() -> "Campo obrigatório"
            senha.value != confirmarSenha.value -> "Senhas não coincidem"
            else -> null
        }

        return listOf(isNomeError, isEmailError, isSenhaError, isConfirmarSenhaError)
            .all { it == null }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .offset(x = (-100).dp, y = (-120).dp)
                .background(
                    color = Color(0xFF990410),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            Image(
                painter = painterResource(id = R.drawable.logocadastro),
                contentDescription = "Logo cadastro DOEVIDA",
                modifier = Modifier
                    .size(170.dp)
                    .padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(39.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 1.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Nome
                Column {
                    Text(
                        text = "Nome Completo",
                        fontSize = 14.sp,
                        color = Color(0xFF990410),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 1.dp),
                        textAlign = TextAlign.Start
                    )
                    OutlinedTextField(
                        value = nomeCompleto.value,
                        onValueChange = { nomeCompleto.value = it },
                        placeholder = {
                            Text(
                                "Digite seu nome completo",
                                color = Color(0x80FFFFFF)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF990410), shape = RoundedCornerShape(8.dp)),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedPlaceholderColor = Color.White,
                            unfocusedPlaceholderColor = Color.White
                        )
                    )
                    if (isNomeError != null) Text(
                        isNomeError!!,
                        color = Color.Red,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // E-mail
                    Text(
                        text = "E-mail",
                        fontSize = 14.sp,
                        color = Color(0xFF990410),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 1.dp),
                        textAlign = TextAlign.Start
                    )
                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        placeholder = { Text("Digite seu e-mail", color = Color(0x80FFFFFF)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF990410), shape = RoundedCornerShape(8.dp)),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedPlaceholderColor = Color.White,
                            unfocusedPlaceholderColor = Color.White
                        )
                    )
                    if (isEmailError != null) Text(
                        isEmailError!!,
                        color = Color.Red,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Senha
                    Text(
                        text = "Digite sua senha",
                        fontSize = 14.sp,
                        color = Color(0xFF990410),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 1.dp),
                        textAlign = TextAlign.Start
                    )
                    OutlinedTextField(
                        value = senha.value,
                        onValueChange = { senha.value = it },
                        placeholder = { Text("Digite sua senha", color = Color(0x80FFFFFF)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF990410), shape = RoundedCornerShape(8.dp)),
                        shape = RoundedCornerShape(8.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedPlaceholderColor = Color.White,
                            unfocusedPlaceholderColor = Color.White
                        )
                    )
                    if (isSenhaError != null) Text(
                        isSenhaError!!,
                        color = Color.Red,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirmar Senha
                    Text(
                        text = "Confirme sua senha",
                        fontSize = 14.sp,
                        color = Color(0xFF990410),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 1.dp),
                        textAlign = TextAlign.Start
                    )
                    OutlinedTextField(
                        value = confirmarSenha.value,
                        onValueChange = { confirmarSenha.value = it },
                        placeholder = { Text("Confirme sua senha", color = Color(0x80FFFFFF)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF990410), shape = RoundedCornerShape(8.dp)),
                        shape = RoundedCornerShape(8.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedPlaceholderColor = Color.White,
                            unfocusedPlaceholderColor = Color.White
                        )
                    )
                    if (isConfirmarSenhaError != null) Text(
                        isConfirmarSenhaError!!,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))

                Button(
                    onClick = {
                           if (validarCadastro()) {
                               val cadastro = Cadastro(
                                   nome = nomeCompleto.value,
                                   email = email.value,
                                   senha = senha.value,
                                   confirmarSenha = confirmarSenha.value
                               )
                               coroutineScope.launch(Dispatchers.IO) {
                                   try {
                                       val response = doevidaApi.insert(cadastro) // se insert é suspend
                                       Log.d("API_Cadastro", "Resposta do backend: $response")

                                       withContext(Dispatchers.Main) {
                                           Toast.makeText(
                                               context,
                                               "Cadastro realizado com sucesso!",
                                               Toast.LENGTH_LONG
                                           ).show()
                                           navController.navigate("tela_home") {
                                               popUpTo("tela_cadastro") { inclusive = true }
                                           }
                                       }
                                   } catch (e: Exception) {
                                       Log.e("API_Cadastro", "Erro ao cadastrar", e)
                                       withContext(Dispatchers.Main) {
                                           Toast.makeText(
                                               context,
                                               "Erro ao cadastrar: ${e.message}",
                                               Toast.LENGTH_LONG
                                           ).show()
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
                        .width(133.dp)
                ) {
                    Text(
                        text = "Criar conta",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = "Já tem uma conta?",
                    color = Color(0xFF990410),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(top = 10.dp)
                )

                Text(
                    text = "Fazer login",
                    color = Color(0xFFB71C1C),
                    modifier = Modifier
                        .clickable { navController.navigate("tela_login") },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TelaCadastroPreview() {
    val navController = rememberNavController()
    TelaCadastro(navController = navController)
}
