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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    var nomeCompleto by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    var isNomeError by remember { mutableStateOf<String?>(null) }
    var isEmailError by remember { mutableStateOf<String?>(null) }
    var isSenhaError by remember { mutableStateOf<String?>(null) }
    var isConfirmarSenhaError by remember { mutableStateOf<String?>(null) }

    var listaTipoSanguineo = listOf(
        Pair(1, "A+"),
        Pair(2, "A-"),
        Pair(3, "B+"),
        Pair(4, "B-"),
        Pair(5, "AB+"),
        Pair(6, "AB-"),
        Pair(7, "O+"),
        Pair(8, "O-"),
    )

    var expandedTipo by remember { mutableStateOf(false) }
    var selectedTipo by remember { mutableStateOf(listaTipoSanguineo[0].first)}
    var selectedTipoLabel by remember { mutableStateOf(listaTipoSanguineo[0].second)}

    val listaSexos = listOf(
        Pair(1, "MASCULINO"),
        Pair(2, "FEMININO"),
        Pair(3, "OUTRO")
    )
    var expanded by remember { mutableStateOf(false) }
    var selectedSexo by remember { mutableStateOf(listaSexos[0].first) }
    var selectedLabel by remember { mutableStateOf(listaSexos[0].second) }

    fun validarCadastro(): Boolean {
        isNomeError = when {
            nomeCompleto.isBlank() -> "Campo obrigatório"
            nomeCompleto.length < 2 -> "Nome deve ser completo"
            else -> null
        }
        isEmailError = when {
            email.isBlank() -> "Campo obrigatório"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Formato inválido"
            else -> null
        }
        isSenhaError = when {
            senha.isBlank() -> "Campo obrigatório"
            senha.length < 8 -> "Senha fraca"
            else -> null
        }

        return listOf(isNomeError, isEmailError, isSenhaError, isConfirmarSenhaError)
            .all { it == null }
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
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState()),
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
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Nome Completo",
                    fontSize = 14.sp,
                    color = Color(0xFF990410),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth())
                OutlinedTextField(
                    value = nomeCompleto,
                    onValueChange = { nomeCompleto = it },
                    placeholder = { Text("Digite seu nome completo", color = Color(0x80FFFFFF)) },
                    modifier = Modifier.fillMaxWidth().background(Color(0xFF990410), RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                if (isNomeError != null) Text(isNomeError!!, color = Color.Red, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(16.dp))

                Text("E-mail", fontSize = 14.sp, color = Color(0xFF990410), fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Digite seu e-mail", color = Color(0x80FFFFFF)) },
                    modifier = Modifier.fillMaxWidth().background(Color(0xFF990410), RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                if (isEmailError != null) Text(isEmailError!!, color = Color.Red, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(16.dp))

                // Senha
                Text(
                    text = if (senha.length < 8)
                        "Digite sua senha (mínimo 8 caracteres)"
                    else
                        "Digite sua senha",
                    fontSize = 14.sp,
                    color = if (senha.length < 8) Color.Red else Color(0xFF990410),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp, start = 1.dp)
                )
                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it},
                    placeholder = { Text("Digite sua senha", color = Color(0x80FFFFFF))},
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF990410),
                            shape = RoundedCornerShape(8.dp)),
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

                Spacer(modifier = Modifier.height(16.dp))

                Text("Sexo", fontSize = 14.sp, color = Color(0xFF990410), fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = selectedLabel,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor().fillMaxWidth().background(Color(0xFF990410), RoundedCornerShape(8.dp)),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        listaSexos.forEach { (id, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedSexo = id
                                    selectedLabel = label
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Tipo sanguíneo", fontSize = 14.sp, color = Color(0xFF990410), fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())
                ExposedDropdownMenuBox(expanded = expandedTipo, onExpandedChange = { expandedTipo = !expandedTipo }) {
                    OutlinedTextField(
                        value = selectedTipoLabel,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor().fillMaxWidth().background(Color(0xFF990410), RoundedCornerShape(8.dp)),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    ExposedDropdownMenu(expanded = expandedTipo, onDismissRequest = { expandedTipo = false }) {
                        listaTipoSanguineo.forEach { (id, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    selectedTipo = id
                                    selectedTipoLabel = label
                                    expandedTipo = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        if (validarCadastro()) {

                            val cadastro = Cadastro(
                                nome = nomeCompleto,
                                email = email,
                                senha = senha,
                                id_sexo = selectedSexo,
                                id_tipo_sanguineo = selectedTipo ?: 0
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
                                                    context = context,
                                                    userId = usuario.id ?: 0,
                                                    userName = usuario.nome ?: "",
                                                    userEmail = usuario.email ?: ""
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF990410)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(48.dp).width(160.dp)
                ) {
                    Text("Criar conta", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Medium)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 12.dp)) {
                    Text("Já tem uma conta?", color = Color(0xFF990410), fontSize = 14.sp)
                    Text(
                        text = "Fazer login",
                        color = Color(0xFFB71C1C),
                        modifier = Modifier.padding(top = 4.dp).clickable { navController.navigate("tela_login") },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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
