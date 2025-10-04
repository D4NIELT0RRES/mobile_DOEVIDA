package com.example.doevida.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R
import com.example.doevida.model.RecuperarSenhaRequest
import com.example.doevida.service.RetrofitFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaRecuperacaoEmail(navController: NavController) {
    var email by remember { mutableStateOf("") }
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
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.logorecuperacao),
                contentDescription = "Logo DOEVIDA",
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = 32.dp)
            )

            Spacer(modifier = Modifier.weight(0.5f))

            Text(
                text = "Digite seu Email",
                fontSize = 14.sp,
                color = Color(0xFF990410),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(56.dp),
                placeholder = { Text("Digite seu email", color = Color.White) },
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

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // Validações
                    if (email.isBlank()) {
                        Toast.makeText(context, "Por favor, digite seu email", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                        Toast.makeText(context, "Formato de email inválido", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    // Chamar a API
                    isLoading = true
                    val userService = RetrofitFactory().getUserService()

                    coroutineScope.launch {
                        try {
                            val request = RecuperarSenhaRequest(email = email.trim())
                            val response = userService.recuperarSenha(request)

                            // Debug - adicionar logs para verificar a resposta
                            println("Response Code: ${response.code()}")
                            println("Response Body: ${response.body()}")
                            println("Is Successful: ${response.isSuccessful}")

                            isLoading = false

                            if (response.isSuccessful) {
                                val body = response.body()

                                // Debug adicional
                                println("Body não é null: ${body != null}")
                                if (body != null) {
                                    println("Status: ${body.status}")  // CORRIGIDO: usando status
                                    println("Message: ${body.message}")
                                }

                                // CORRIGIDO: Verificação baseada na mensagem OU status
                                if (body != null && (
                                            body.status == true ||
                                                    body.message.contains("enviado", ignoreCase = true)
                                            )) {
                                    Toast.makeText(
                                        context,
                                        "Código enviado para seu email!",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    // Debug da navegação
                                    println("Tentando navegar para tela_redefinir_senha")

                                    // Navegação forçada
                                    try {
                                        navController.navigate("tela_redefinir_senha")
                                        println("Navegação executada!")
                                    } catch (e: Exception) {
                                        println("Erro na navegação: ${e.message}")
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        body?.message ?: "Erro ao enviar email",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Erro no servidor: ${response.code()}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } catch (e: Exception) {
                            isLoading = false
                            Toast.makeText(
                                context,
                                "Erro de conexão: Verifique sua internet",
                                Toast.LENGTH_LONG
                            ).show()
                            // Debug - mostrar erro completo
                            e.printStackTrace()
                            println("Erro detalhado: ${e.message}")
                        }
                    }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF990410)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(48.dp)
                    .width(200.dp)
            ) {
                Text(
                    text = if (isLoading) "Enviando..." else "Enviar Código",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}

@Preview
@Composable
private fun TelaRecuperacaoEmailPreview() {
    val navController = rememberNavController()
    TelaRecuperacaoEmail(navController = navController)
}