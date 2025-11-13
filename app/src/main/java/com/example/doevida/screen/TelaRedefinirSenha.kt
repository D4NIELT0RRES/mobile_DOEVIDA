package com.example.doevida.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.model.RedefinirSenhaRequest
import com.example.doevida.service.RetrofitFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaRedefinirSenha(navController: NavController) {
    var codigo by rememberSaveable { mutableStateOf("") }
    var novaSenha by rememberSaveable { mutableStateOf("") }
    var confirmarSenha by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var codigoError by remember { mutableStateOf<String?>(null) }
    var senhaError by remember { mutableStateOf<String?>(null) }
    var confirmarSenhaError by remember { mutableStateOf<String?>(null) }

    var senhaVisible by rememberSaveable { mutableStateOf(false) }
    var confirmarSenhaVisible by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun validarCampos(): Boolean {
        codigoError = if (codigo.length != 6) "O código deve ter 6 dígitos." else null
        senhaError = if (novaSenha.length < 8) "A senha deve ter no mínimo 8 caracteres." else null
        confirmarSenhaError = if (novaSenha != confirmarSenha) "As senhas não coincidem." else null
        return listOf(codigoError, senhaError, confirmarSenhaError).all { it == null }
    }

    fun redefinirSenha() {
        if (!validarCampos()) return

        isLoading = true
        scope.launch {
            try {
                val request = RedefinirSenhaRequest(codigo, novaSenha)
                val response = RetrofitFactory(context).getUserService().redefinirSenha(request)
                if (response.isSuccessful && response.body()?.status == true) {
                    Toast.makeText(context, "Senha redefinida com sucesso!", Toast.LENGTH_LONG).show()
                    navController.navigate("tela_login") { popUpTo(0) }
                } else {
                    Toast.makeText(context, response.body()?.message ?: "Código inválido ou expirado", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Erro de conexão: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = { TopBarRedefinir(navController) },
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Redefinir Senha", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Insira o código de 6 dígitos enviado para seu e-mail e crie uma nova senha.", color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 4.dp, bottom = 24.dp))

            FormFieldRedefinir("Código de 6 dígitos", codigo, { if(it.length <= 6) codigo = it.filter { c -> c.isDigit() } }, KeyboardType.Number, error = codigoError)
            FormFieldRedefinir("Nova Senha", novaSenha, { novaSenha = it }, KeyboardType.Password, if (senhaVisible) VisualTransformation.None else PasswordVisualTransformation(), error = senhaError) {
                IconButton(onClick = { senhaVisible = !senhaVisible }) {
                    Icon(if (senhaVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, null)
                }
            }
            FormFieldRedefinir("Confirmar Nova Senha", confirmarSenha, { confirmarSenha = it }, KeyboardType.Password, if (confirmarSenhaVisible) VisualTransformation.None else PasswordVisualTransformation(), error = confirmarSenhaError) {
                IconButton(onClick = { confirmarSenhaVisible = !confirmarSenhaVisible }) {
                    Icon(if (confirmarSenhaVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, null)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { redefinirSenha() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF990410)),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Redefinir Senha", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarRedefinir(navController: NavController) {
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

@Composable
private fun FormFieldRedefinir(label: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType, visualTransformation: VisualTransformation = VisualTransformation.None, error: String?, trailingIcon: @Composable (() -> Unit)? = null) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        isError = error != null,
        supportingText = { if (error != null) Text(error, color = MaterialTheme.colorScheme.error) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        singleLine = true,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF990410),
            unfocusedBorderColor = Color.LightGray,
            cursorColor = Color(0xFF990410),
            focusedLabelColor = Color(0xFF990410)
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TelaRedefinirSenhaPreview() {
    TelaRedefinirSenha(rememberNavController())
}
