package com.example.doevida.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R
import com.example.doevida.model.RedefinirSenhaRequest
import com.example.doevida.service.RetrofitFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaRedefinirSenha(navController: NavController) {
    var token by remember { mutableStateOf("") }
    var novaSenha by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logoredefinirsenha),
                contentDescription = "Logo DOEVIDA",
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = 32.dp)
            )
            Text(
                text = "Digite o código de 6 dígitos",
                fontSize = 14.sp,
                color = Color(0xFF990410),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            OutlinedTextField(
                value = token,
                onValueChange = { newValue ->
                    if(newValue.length <= 6 && newValue.all { it.isDigit()}) {
                        token = newValue
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(56.dp),
                placeholder = { Text("000000", color = Color.White.copy(alpha = 0.7f))},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
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
                        contentDescription = "Ícone de token",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                    )
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp // Espaçamento entre os números
                )
            )
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Crie sua nova senha",
                fontSize = 14.sp,
                color = Color(0xFF990410),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            OutlinedTextField(
                value = novaSenha,
                onValueChange = { novaSenha = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(56.dp),
                placeholder = { Text("Criar senha", color = Color.White) },
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

            Spacer(modifier = Modifier.height(70.dp))

            Button(
                onClick = {
                    if(token.length != 6){
                        Toast.makeText(context, "Digite o código de 6 dígitos", Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if(novaSenha.length < 8){
                        Toast.makeText(context, "A senha deve conter pelo menos 8 caracteres", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    isLoading = true
                    val userService = RetrofitFactory(context).getUserService()

                    coroutineScope.launch {
                        try {
                            val request = RedefinirSenhaRequest(
                                codigo = token,
                                novaSenha = novaSenha
                            )
                            val response = userService.redefinirSenha(request)

                            isLoading = false

                            if(response.isSuccessful){
                                val body = response.body()
                                if(body != null && body.status == true){
                                    Toast.makeText(
                                        context,
                                        "Senha redefinida com sucesso!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.navigate("tela_login")
                                }else{
                                    Toast.makeText(
                                        context,
                                        body?.message ?: "Erro ao redefinir senha",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }else{
                                Toast.makeText(
                                    context,
                                    "Código inválido ou expirado",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }catch (e: Exception){
                            isLoading = false
                            Toast.makeText(
                                context,
                                "Erro de conexão: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF990410)
                ),
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier
                    .height(46.dp)
                    .width(209.dp)
            ) {
                Text(
                    text = "Confirmar nova senha",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TelaRedefinirSenhaPreview() {
    val navController = rememberNavController()
    TelaRedefinirSenha(navController = navController)
}
