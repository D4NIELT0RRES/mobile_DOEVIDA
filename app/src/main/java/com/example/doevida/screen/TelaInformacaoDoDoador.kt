package com.example.doevida.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R
import com.example.doevida.model.ComplementoPerfilRequest
import com.example.doevida.service.RetrofitFactory
import com.example.doevida.util.UserDataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaInformacaoDoDoador(
    navController: NavController,
    hospitalId: Int,
    data: String,
    horario: String // Novo parâmetro
) {

    var cpf by remember { mutableStateOf("") }
    var dataDeNascimento by remember { mutableStateOf("") }
    var celular by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun validarCampos(): String? {
        when {
            cpf.isBlank() -> return "CPF é obrigatório"
            cpf.replace(Regex("[^0-9]"), "").length != 11 -> return "CPF deve ter 11 dígitos"
            dataDeNascimento.isBlank() -> return "Data de nascimento é obrigatória"
            dataDeNascimento.length != 8 -> return "Data de nascimento deve ser preenchida completamente"
            celular.isBlank() -> return "Celular é obrigatório"
            celular.replace(Regex("[^0-9]"), "").length < 10 -> return "Celular deve ter pelo menos 10 dígitos"
            cep.isBlank() -> return "CEP é obrigatório"
            cep.replace(Regex("[^0-9]"), "").length != 8 -> return "CEP deve ter 8 dígitos"
            else -> return null
        }
    }

    fun completarPerfil() {
        val erro = validarCampos()
        if (erro != null) {
            Toast.makeText(context, erro, Toast.LENGTH_SHORT).show()
            return
        }

        isLoading = true

        scope.launch {
            try {
                val dataFormatadaParaApi = dataDeNascimento.let {
                    val dia = it.substring(0, 2)
                    val mes = it.substring(2, 4)
                    val ano = it.substring(4, 8)
                    "$ano-$mes-$dia"
                }

                val service = RetrofitFactory(context).getUserService()
                val request = ComplementoPerfilRequest(
                    cpf = cpf.replace(Regex("[^0-9]"), ""),
                    data_nascimento = dataFormatadaParaApi, // Envia a data formatada
                    numero = celular.replace(Regex("[^0-9]"), ""),
                    cep = cep.replace(Regex("[^0-9]"), "")
                )

                val response = service.completarPerfil(request)

                withContext(Dispatchers.Main) {
                    isLoading = false

                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body?.status == true) {
                            Toast.makeText(context, body.message, Toast.LENGTH_SHORT).show()
                            UserDataManager.saveCpf(context, cpf.replace(Regex("[^0-9]"), ""))
                            // Navega para a tela de protocolo, repassando TODOS os dados
                            navController.navigate("tela_protocolo/$hospitalId/$data/$horario")
                        } else {
                            Toast.makeText(context, body?.message ?: "Erro desconhecido", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        when (response.code()) {
                            400 -> Toast.makeText(context, "Dados inválidos. Verifique os campos.", Toast.LENGTH_SHORT).show()
                            401 -> Toast.makeText(context, "Sessão expirada. Faça login novamente.", Toast.LENGTH_SHORT).show()
                            409 -> Toast.makeText(context, "CPF já cadastrado para outro usuário.", Toast.LENGTH_SHORT).show()
                            else -> Toast.makeText(context, "Erro no servidor. Tente novamente.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isLoading = false
                    Log.e("TelaInformacaoDoDoador", "Erro ao completar perfil", e)
                    Toast.makeText(context, "Erro de conexão. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                }
            }
        }
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

        IconButton(
            onClick = { navController.popBackStack() },
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
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Image(
                painter = painterResource(id = R.drawable.logoinfo),
                contentDescription = "Logo cadastro DOEVIDA",
                modifier = Modifier
                    .size(170.dp)
                    .padding(bottom = 16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 1.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo CPF
                Column {
                    Text(
                        text = "CPF",
                        fontSize = 14.sp,
                        color = Color(0xFF990410),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 1.dp),
                        textAlign = TextAlign.Start
                    )
                    OutlinedTextField(
                        value = cpf,
                        onValueChange = {
                            val numeros = it.replace(Regex("[^0-9]"), "")
                            if (numeros.length <= 11) {
                                cpf = numeros
                            }
                        },
                        placeholder = { Text("00000000000", color = Color.White.copy(alpha = 0.5f)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF990410), shape = RoundedCornerShape(8.dp)),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                        )
                    )
                    Spacer(modifier = Modifier.height(15.dp))

                    // Campo Data de Nascimento
                    Text(
                        text = "Data De Nascimento",
                        fontSize = 14.sp,
                        color = Color(0xFF990410),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 1.dp),
                        textAlign = TextAlign.Start
                    )
                    OutlinedTextField(
                        value = dataDeNascimento,
                        onValueChange = { 
                            val numeros = it.replace(Regex("[^0-9]"), "")
                            if (numeros.length <= 8) {
                                dataDeNascimento = numeros
                            }
                        },
                        placeholder = { Text("DD/MM/AAAA", color = Color.White.copy(alpha = 0.5f)) },
                        visualTransformation = DateVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF990410), shape = RoundedCornerShape(8.dp)),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                        )
                    )
                    Spacer(modifier = Modifier.height(15.dp))

                    // Campo Celular
                    Text(
                        text = "Celular",
                        fontSize = 14.sp,
                        color = Color(0xFF990410),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 1.dp),
                        textAlign = TextAlign.Start
                    )
                    OutlinedTextField(
                        value = celular,
                        onValueChange = {
                            val numeros = it.replace(Regex("[^0-9]"), "")
                            if (numeros.length <= 11) {
                                celular = numeros
                            }
                        },
                        placeholder = { Text("00000000000", color = Color.White.copy(alpha = 0.5f)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF990410), shape = RoundedCornerShape(8.dp)),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                        )
                    )
                    Spacer(modifier = Modifier.height(15.dp))

                    // Campo CEP
                    Text(
                        text = "CEP",
                        fontSize = 14.sp,
                        color = Color(0xFF990410),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 1.dp),
                        textAlign = TextAlign.Start
                    )
                    OutlinedTextField(
                        value = cep,
                        onValueChange = {
                            val numeros = it.replace(Regex("[^0-9]"), "")
                            if (numeros.length <= 8) {
                                cep = numeros
                            }
                        },
                        placeholder = { Text("00000000", color = Color.White.copy(alpha = 0.5f)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF990410), shape = RoundedCornerShape(8.dp)),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                        )
                    )
                    Spacer(modifier = Modifier.height(150.dp))
                }

                // Botão de Confirmar
                Button(
                    onClick = { completarPerfil() },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF990410)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .height(50.dp)
                        .width(200.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = "Confirmar Dados",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

// Classe para a transformação visual da data - VERSÃO CORRIGIDA
class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Limita a 8 dígitos: DDMMAAAA
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        
        // Constrói a string formatada: DD/MM/AAAA
        val formattedText = buildString {
            for (i in trimmed.indices) {
                append(trimmed[i])
                if (i == 1 || i == 3) {
                    if (i < trimmed.length - 1) { // Só adiciona a barra se não for o último caractere
                        append('/')
                    }
                }
            }
        }

        val offsetMapping = object : OffsetMapping {
            // Mapeia do texto original (só dígitos) para o formatado (com barras)
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset <= 4 -> offset + 1
                    else -> offset + 2
                }.coerceAtMost(10) // Garante que não ultrapasse o tamanho máximo de DD/MM/AAAA
            }

            // Mapeia do texto formatado (com barras) de volta para o original (só dígitos)
            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 2 -> offset
                    offset <= 5 -> offset - 1
                    else -> offset - 2
                }.coerceAtLeast(0) // Garante que não seja menor que zero
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TelaInformacaoDoDoadorPreview() {
    val navController = rememberNavController()
    TelaInformacaoDoDoador(navController = navController, hospitalId = 0, data = "", horario = "")
}
