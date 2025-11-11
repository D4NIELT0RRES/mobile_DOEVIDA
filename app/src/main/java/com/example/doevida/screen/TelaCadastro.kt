package com.example.doevida.screen

import android.util.Log
import android.util.Patterns
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R
import com.example.doevida.model.Cadastro
import com.example.doevida.service.RetrofitFactory
import com.example.doevida.service.SharedPreferencesUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastro(navController: NavController) {

    val context = LocalContext.current
    val doevidaApi = RetrofitFactory(context).getUserService()
    val coroutineScope = rememberCoroutineScope()
    val primaryColor = Color(0xFF990410)

    var nomeCompleto by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var confirmarSenha by rememberSaveable { mutableStateOf("") }
    var senhaVisible by rememberSaveable { mutableStateOf(false) }
    var confirmarSenhaVisible by rememberSaveable { mutableStateOf(false) }

    var nomeError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var senhaError by remember { mutableStateOf<String?>(null) }
    var confirmarSenhaError by remember { mutableStateOf<String?>(null) }

    val listaTipoSanguineo = listOf(
        Pair(1, "A+"), Pair(2, "A-"), Pair(3, "B+"), Pair(4, "B-"),
        Pair(5, "AB+"), Pair(6, "AB-"), Pair(7, "O+"), Pair(8, "O-")
    )
    var expandedTipo by remember { mutableStateOf(false) }
    var selectedTipo by remember { mutableStateOf(listaTipoSanguineo[0].first) }
    var selectedTipoLabel by remember { mutableStateOf(listaTipoSanguineo[0].second) }

    val listaSexos = listOf(Pair(1, "MASCULINO"), Pair(2, "FEMININO"), Pair(3, "OUTRO"))
    var expandedSexo by remember { mutableStateOf(false) }
    var selectedSexo by remember { mutableStateOf(listaSexos[0].first) }
    var selectedSexoLabel by remember { mutableStateOf(listaSexos[0].second) }

    fun validarCadastro(): Boolean {
        nomeError = if (nomeCompleto.isBlank()) "Campo obrigatório" else null
        emailError = when {
            email.isBlank() -> "Campo obrigatório"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Formato de e-mail inválido"
            else -> null
        }
        senhaError = when {
            senha.isBlank() -> "Campo obrigatório"
            senha.length < 8 -> "Senha deve ter no mínimo 8 caracteres"
            else -> null
        }
        confirmarSenhaError = when {
            confirmarSenha.isBlank() -> "Campo obrigatório"
            senha != confirmarSenha -> "As senhas não coincidem"
            else -> null
        }
        return listOf(nomeError, emailError, senhaError, confirmarSenhaError).all { it == null }
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
                .background(color = primaryColor, shape = CircleShape)
        )
        IconButton(
            onClick = { navController.navigate("tela_inicial") },
            modifier = Modifier.align(Alignment.TopStart).padding(top = 16.dp, start = 16.dp)
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.logocadastro),
                contentDescription = "Logo cadastro DOEVIDA",
                modifier = Modifier.size(150.dp)
            )

            Text(
                text = "Cadastro",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            OutlinedTextField(
                value = nomeCompleto,
                onValueChange = { nomeCompleto = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nome Completo") },
                isError = nomeError != null,
                supportingText = {
                    if (nomeError != null) {
                        Text(nomeError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor, unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = primaryColor, cursorColor = primaryColor
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("E-mail") },
                isError = emailError != null,
                supportingText = {
                    if (emailError != null) {
                        Text(emailError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor, unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = primaryColor, cursorColor = primaryColor
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Senha") },
                isError = senhaError != null,
                supportingText = {
                    if (senhaError != null) {
                        Text(senhaError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                visualTransformation = if (senhaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (senhaVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { senhaVisible = !senhaVisible }) {
                        Icon(imageVector = image, if (senhaVisible) "Esconder senha" else "Mostrar senha")
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor, unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = primaryColor, cursorColor = primaryColor
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Confirmar Senha") },
                isError = confirmarSenhaError != null,
                supportingText = {
                    if (confirmarSenhaError != null) {
                        Text(confirmarSenhaError!!, color = MaterialTheme.colorScheme.error)
                    }
                },
                visualTransformation = if (confirmarSenhaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (confirmarSenhaVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { confirmarSenhaVisible = !confirmarSenhaVisible }) {
                        Icon(imageVector = image, if (confirmarSenhaVisible) "Esconder senha" else "Mostrar senha")
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor, unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = primaryColor, cursorColor = primaryColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth()) {
                ExposedDropdownMenuBox(
                    expanded = expandedSexo,
                    onExpandedChange = { expandedSexo = !expandedSexo },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedSexoLabel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Sexo") },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor, unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = primaryColor, cursorColor = primaryColor
                        )
                    )
                    ExposedDropdownMenu(expanded = expandedSexo, onDismissRequest = { expandedSexo = false }) {
                        listaSexos.forEach { (id, label) ->
                            DropdownMenuItem(text = { Text(label) }, onClick = {
                                selectedSexo = id
                                selectedSexoLabel = label
                                expandedSexo = false
                            })
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedTipo,
                    onExpandedChange = { expandedTipo = !expandedTipo },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedTipoLabel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo Sanguíneo") },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryColor, unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = primaryColor, cursorColor = primaryColor
                        )
                    )
                    ExposedDropdownMenu(expanded = expandedTipo, onDismissRequest = { expandedTipo = false }) {
                        listaTipoSanguineo.forEach { (id, label) ->
                            DropdownMenuItem(text = { Text(label) }, onClick = {
                                selectedTipo = id
                                selectedTipoLabel = label
                                expandedTipo = false
                            })
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (validarCadastro()) {
                        val cadastro = Cadastro(
                            nome = nomeCompleto, email = email, senha = senha,
                            id_sexo = selectedSexo, id_tipo_sanguineo = selectedTipo
                        )
                        coroutineScope.launch(Dispatchers.IO) {
                            try {
                                val response = doevidaApi.insert(cadastro)
                                withContext(Dispatchers.Main) {
                                    if (response.isSuccessful) {
                                        val resposta = response.body()
                                        val usuario = resposta?.usuario
                                        if (usuario != null) {
                                            SharedPreferencesUtils.saveUserData(
                                                context = context, userId = usuario.id ?: 0,
                                                userName = usuario.nome ?: "", userEmail = usuario.email ?: ""
                                            )
                                            Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show()
                                            navController.navigate("tela_login") {
                                                popUpTo("tela_cadastro") { inclusive = true }
                                            }
                                        } else {
                                            Toast.makeText(context, "Erro: resposta sem usuário.", Toast.LENGTH_LONG).show()
                                        }
                                    } else {
                                        Toast.makeText(context, "Erro: ${response.message()}", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e("API_Cadastro", "Erro ao cadastrar", e)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Erro ao cadastrar: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Criar conta", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }

            Row(
                modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Já tem uma conta?", color = primaryColor, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Fazer login",
                    color = primaryColor,
                    modifier = Modifier.clickable { navController.navigate("tela_login") },
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
