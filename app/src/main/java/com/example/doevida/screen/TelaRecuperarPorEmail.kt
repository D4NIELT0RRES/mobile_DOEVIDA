package com.example.doevida.screen

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
    var email by rememberSaveable { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun validateEmail(): Boolean {
        emailError = if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Por favor, insira um e-mail válido."
        } else {
            null
        }
        return emailError == null
    }

    fun sendRecoveryCode() {
        if (!validateEmail()) return

        isLoading = true
        scope.launch {
            try {
                val request = RecuperarSenhaRequest(email = email.trim())
                val response = RetrofitFactory(context).getUserService().recuperarSenha(request)
                if (response.isSuccessful && response.body()?.status == true) {
                    Toast.makeText(context, "Código enviado para seu e-mail!", Toast.LENGTH_LONG).show()
                    navController.navigate("tela_redefinir_senha")
                } else {
                    Toast.makeText(context, response.body()?.message ?: "Erro ao enviar e-mail", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Erro de conexão. Verifique sua internet.", Toast.LENGTH_LONG).show()
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = { TopBarRecuperacao(navController) },
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(painter = painterResource(id = R.drawable.logorecuperacao), contentDescription = null, modifier = Modifier.size(150.dp))
            Spacer(modifier = Modifier.height(16.dp))

            Text("Recuperar Senha", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Digite seu e-mail para receber um código de recuperação.", color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 4.dp, bottom = 24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError != null,
                supportingText = { if (emailError != null) Text(emailError!!, color = MaterialTheme.colorScheme.error) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF990410),
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = Color(0xFF990410),
                    focusedLabelColor = Color(0xFF990410)
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { sendRecoveryCode() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF990410)),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Enviar Código", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarRecuperacao(navController: NavController) {
    TopAppBar(
        title = { Text("") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TelaRecuperacaoEmailPreview() {
    TelaRecuperacaoEmail(rememberNavController())
}
