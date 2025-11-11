package com.example.doevida.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.doevida.model.ComplementoPerfilRequest
import com.example.doevida.service.RetrofitFactory
import com.example.doevida.util.UserDataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaInformacaoDoDoador(navController: NavController, hospitalId: Int, data: String, horario: String) {
    var cpf by rememberSaveable { mutableStateOf("") }
    var dataDeNascimento by rememberSaveable { mutableStateOf("") }
    var celular by rememberSaveable { mutableStateOf("") }
    var cep by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var cpfError by remember { mutableStateOf<String?>(null) }
    var dataNascimentoError by remember { mutableStateOf<String?>(null) }
    var celularError by remember { mutableStateOf<String?>(null) }
    var cepError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun validarCampos(): Boolean {
        cpfError = if (cpf.length != 11) "CPF inválido" else null
        dataNascimentoError = if (dataDeNascimento.length != 8) "Data inválida" else null
        celularError = if (celular.length < 10) "Celular inválido" else null
        cepError = if (cep.length != 8) "CEP inválido" else null
        return listOf(cpfError, dataNascimentoError, celularError, cepError).all { it == null }
    }

    fun completarPerfil() {
        if (!validarCampos()) return

        isLoading = true
        scope.launch(Dispatchers.IO) {
            try {
                val dataFormatada = "${dataDeNascimento.substring(4, 8)}-${dataDeNascimento.substring(2, 4)}-${dataDeNascimento.substring(0, 2)}"
                val request = ComplementoPerfilRequest(cpf, dataFormatada, celular, cep)
                val response = RetrofitFactory(context).getUserService().completarPerfil(request)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                        UserDataManager.saveCpf(context, cpf)
                        navController.navigate("tela_protocolo/$hospitalId/$data/$horario")
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                        Log.e("TelaInformacao", "Erro: ${response.code()} - $errorBody")
                        Toast.makeText(context, "Erro: $errorBody", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("TelaInformacao", "Falha na conexão", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Falha na conexão. Verifique sua internet.", Toast.LENGTH_SHORT).show()
                }
            } finally {
                withContext(Dispatchers.Main) { isLoading = false }
            }
        }
    }

    Scaffold(
        topBar = { TopBarInformacao(navController) },
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
            Text("Complete seus dados", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Text("Precisamos de mais algumas informações para confirmar seu agendamento.", textAlign = TextAlign.Center, color = Color.Gray, modifier = Modifier.padding(top = 4.dp, bottom = 24.dp))

            FormField(label = "CPF", value = cpf, onValueChange = { if (it.length <= 11) cpf = it.filter { c -> c.isDigit() } }, keyboardType = KeyboardType.Number, visualTransformation = CpfVisualTransformation(), error = cpfError)
            FormField(label = "Data de Nascimento", value = dataDeNascimento, onValueChange = { if (it.length <= 8) dataDeNascimento = it.filter { c -> c.isDigit() } }, keyboardType = KeyboardType.Number, visualTransformation = DateVisualTransformation(), error = dataNascimentoError)
            FormField(label = "Celular", value = celular, onValueChange = { if (it.length <= 11) celular = it.filter { c -> c.isDigit() } }, keyboardType = KeyboardType.Phone, visualTransformation = PhoneVisualTransformation(), error = celularError)
            FormField(label = "CEP", value = cep, onValueChange = { if (it.length <= 8) cep = it.filter { c -> c.isDigit() } }, keyboardType = KeyboardType.Number, visualTransformation = CepVisualTransformation(), error = cepError)

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { completarPerfil() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp).padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF990410)),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Confirmar Dados", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarInformacao(navController: NavController) {
    TopAppBar(
        title = { Text("", fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
private fun FormField(label: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType, visualTransformation: VisualTransformation, error: String?) {
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
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF990410),
            unfocusedBorderColor = Color.LightGray,
            cursorColor = Color(0xFF990410),
            focusedLabelColor = Color(0xFF990410)
        )
    )
}

// MÁSCARAS
class CpfVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 11) text.text.substring(0..10) else text.text
        val out = buildString {
            trimmed.forEachIndexed { index, char ->
                append(char)
                if (index == 2 || index == 5) append(".")
                if (index == 8) append("-")
            }
        }
        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset + 1
                if (offset <= 8) return offset + 2
                return offset + 3
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 6) return offset - 1
                if (offset <= 10) return offset - 2
                return offset - 3
            }
        }
        return TransformedText(AnnotatedString(out), mapping)
    }
}

class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        val out = buildString {
            trimmed.forEachIndexed { index, char ->
                append(char)
                if (index == 1 || index == 3) append("/")
            }
        }
        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 3) return offset + 1
                return offset + 2
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                return offset - 2
            }
        }
        return TransformedText(AnnotatedString(out), mapping)
    }
}

class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 11) text.text.substring(0..10) else text.text
        val out = buildString {
            when (trimmed.length) {
                in 1..2 -> append("(${trimmed}")
                in 3..6 -> append("(${trimmed.substring(0, 2)}) ${trimmed.substring(2)}")
                in 7..10 -> append("(${trimmed.substring(0, 2)}) ${trimmed.substring(2, 6)}-${trimmed.substring(6)}")
                11 -> append("(${trimmed.substring(0, 2)}) ${trimmed.substring(2, 7)}-${trimmed.substring(7)}")
            }
        }
        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 2) return offset + 1
                if (offset <= 6) return offset + 3
                if (offset <= 10) return offset + 4
                return offset + 5
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset - 1
                if (offset <= 7) return offset - 3
                if (offset <= 12) return offset - 4
                return offset - 5
            }
        }
        return TransformedText(AnnotatedString(out), mapping)
    }
}

class CepVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        val out = buildString {
            trimmed.forEachIndexed { index, char ->
                append(char)
                if (index == 4) append("-")
            }
        }
        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return if (offset <= 4) offset else offset + 1
            }

            override fun transformedToOriginal(offset: Int): Int {
                return if (offset <= 5) offset else offset - 1
            }
        }
        return TransformedText(AnnotatedString(out), mapping)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TelaInformacaoDoDoadorPreview() {
    TelaInformacaoDoDoador(navController = rememberNavController(), hospitalId = 1, data = "2023-12-25", horario = "14:30")
}
