package com.example.doevida.screen

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R
import com.example.doevida.model.LoginRequest
import com.example.doevida.service.RetrofitFactory
import com.example.doevida.service.SharedPreferencesUtils
import com.example.doevida.util.TokenManager
import com.example.doevida.util.UserDataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaLogin(navController: NavController) {
    var login by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var senhaError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val primaryColor = Color(0xFF990410)
    val scrollState = rememberScrollState()

    // Lógica de Biometria
    var canAuthenticate by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        val biometricManager = BiometricManager.from(context)
        canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
        
        // Se houver token válido e biometria, tenta logar
        val token = TokenManager(context).getToken()
        if (token != null && canAuthenticate) {
            // Aqui poderia acionar a biometria automaticamente se desejado
        }
    }

    fun authenticateWithBiometrics() {
        val executor = ContextCompat.getMainExecutor(context)
        val biometricPrompt = BiometricPrompt(context as FragmentActivity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Sucesso: Login automático se houver token ou preencher campos salvos
                    // Como não estamos salvando senha localmente por segurança, 
                    // a biometria serve como "Fast Pass" se o token ainda for válido.
                    val token = TokenManager(context).getToken()
                    if (token != null) {
                        Toast.makeText(context, "Autenticado com sucesso!", Toast.LENGTH_SHORT).show()
                        navController.navigate("tela_home") {
                            popUpTo("tela_login") { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "Sessão expirada. Por favor, faça login com senha.", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(context, "Erro na autenticação: $errString", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login Biométrico")
            .setSubtitle("Use sua digital ou face para entrar")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    fun validarCampos(): Boolean {
        emailError = if (login.isBlank()) {
            "Campo obrigatório"
        } else if (login.contains("@") && !Patterns.EMAIL_ADDRESS.matcher(login).matches()) {
            "Formato de e-mail inválido"
        } else {
            null
        }

        senhaError = if (senha.isBlank()) {
            "Campo obrigatório"
        } else if (senha.length < 8) {
            "Senha deve ter no mínimo 8 caracteres"
        } else {
            null
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
                    color = primaryColor,
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
                .padding(horizontal = 32.dp)
                .verticalScroll(scrollState), 
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top 
        ) {
            Spacer(modifier = Modifier.height(160.dp)) 

            Image(
                painter = painterResource(id = R.drawable.logologin),
                contentDescription = "Logo DOEVIDA",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(120.dp) 
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email ou Usuário") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.perfil),
                        contentDescription = "Ícone de usuário",
                        modifier = Modifier.size(24.dp),
                        tint = primaryColor
                    )
                },
                isError = emailError != null,
                supportingText = {
                    if (emailError != null) {
                        Text(emailError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = primaryColor,
                    cursorColor = primaryColor,
                    focusedLeadingIconColor = primaryColor,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Senha") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.senha),
                        contentDescription = "Ícone de senha",
                        modifier = Modifier.size(24.dp),
                        tint = primaryColor
                    )
                },
                isError = senhaError != null,
                supportingText = {
                    if (senhaError != null) {
                        Text(senhaError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Esconder senha" else "Mostrar senha"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description, tint = Color.Gray)
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = primaryColor,
                    cursorColor = primaryColor,
                    focusedLeadingIconColor = primaryColor,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Text(
                text = "Esqueci minha senha?",
                color = primaryColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { navController.navigate(route = "tela_recuperacao") }
                    .padding(top = 8.dp),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (validarCampos()) {
                        val request = if (login.contains("@")) {
                            LoginRequest(email = login, username = null, senha = senha)
                        } else {
                            LoginRequest(email = null, username = login, senha = senha)
                        }

                        scope.launch(Dispatchers.IO) {
                            try {
                                val response = RetrofitFactory(context).getUserService().login(request)
                                withContext(Dispatchers.Main) {
                                    if (response.isSuccessful) {
                                        val body = response.body()
                                        if (body?.usuario != null && body.token != null) {
                                            senhaError = null
                                            UserDataManager.saveUserData(
                                                context,
                                                id = body.usuario.id,
                                                name = body.usuario.nome ?: "",
                                                email = body.usuario.email ?: ""
                                            )
                                            
                                            SharedPreferencesUtils.clearUserData(context)
                                            SharedPreferencesUtils.saveUserData(
                                                context,
                                                userId = body.usuario.id,
                                                userName = body.usuario.nome ?: "",
                                                userEmail = body.usuario.email ?: "",
                                                userCpf = ""
                                            )

                                            TokenManager(context).saveToken(body.token)

                                            Toast.makeText(context, "Login realizado com sucesso!", Toast.LENGTH_LONG).show()

                                            navController.navigate("tela_home") {
                                                popUpTo("tela_login") { inclusive = true }
                                            }
                                        } else {
                                            senhaError = "Erro inesperado. Tente novamente."
                                        }
                                    } else {
                                        when (response.code()) {
                                            401 -> senhaError = "Senha incorreta"
                                            403 -> senhaError = "Acesso negado"
                                            else -> senhaError = "Erro ao fazer login: ${'$'}{response.code()}"
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    Log.e("TelaLogin", "Erro ao conectar", e)
                                    senhaError = "Erro ao conectar. Verifique sua conexão."
                                }
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Entrar",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Botão de Biometria (Só aparece se houver hardware disponível)
            if (canAuthenticate) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedButton(
                    onClick = { authenticateWithBiometrics() },
                    border = BorderStroke(1.dp, primaryColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.Fingerprint, contentDescription = "Biometria", tint = primaryColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Entrar com Biometria", color = primaryColor)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Não tem uma conta?",
                    color = primaryColor,
                    fontSize = 14.sp,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Cadastre-se",
                    color = primaryColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { navController.navigate(route = "tela_cadastro") }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TelaLoginPreview() {
    val navController = rememberNavController()
    TelaLogin(navController = navController)
}
