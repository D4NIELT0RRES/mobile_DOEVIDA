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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.platform.LocalConfiguration
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaInformacaoDoDoador(navController: NavController) {

    var cpf = remember { mutableStateOf("") }
    val datadenascimento = remember { mutableStateOf("") }
    val celular = remember { mutableStateOf("") }
    val cep = remember { mutableStateOf("") }

    val listaSexos = listOf(
        Pair(1, "MASCULINO"),
        Pair(2, "FEMININO"),
        Pair(3, "OUTRO")
    )
    var expanded by remember { mutableStateOf(false) }
    var selectedSexo by remember { mutableStateOf(listaSexos[0].first) }
    var selectedLabel by remember { mutableStateOf(listaSexos[0].second) }

    val listaTipos = listOf(
        Pair(1, "A+"),
        Pair(2, "A-"),
        Pair(3, "B+"),
        Pair(4, "B-"),
        Pair(5, "AB+"),
        Pair(6, "AB-"),
        Pair(7, "O+"),
        Pair(8, "O-")
    )
    var expandedTipo by remember { mutableStateOf(false) }
    var selectedTipo by remember { mutableStateOf(listaTipos[0].first) }
    var selectedLabelTipo by remember { mutableStateOf(listaTipos[0].second) }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

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
            Spacer(modifier = Modifier.height(65.dp))

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
                        value = cpf.value,
                        onValueChange = { cpf.value = it },
                        placeholder = {
                            Text(
                                "Digite Seu CPF",
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

                    Spacer(modifier = Modifier.height(10.dp))

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
                        value = datadenascimento.value,
                        onValueChange = { datadenascimento.value = it },
                        placeholder = {
                            Text(
                                "Digite Sua Data De Nascimento",
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

                    Spacer(modifier = Modifier.height(10.dp))

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
                        value = celular.value,
                        onValueChange = { celular.value = it },
                        placeholder = { Text("Digite Seu Celular", color = Color(0x80FFFFFF)) },
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

                    Spacer(modifier = Modifier.height(10.dp))

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
                        value = cep.value,
                        onValueChange = { cep.value = it },
                        placeholder = { Text("Digite Seu CEP", color = Color(0x80FFFFFF)) },
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

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Sexo",
                        fontSize = 14.sp,
                        color = Color(0xFF990410),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp, start = 1.dp),
                        textAlign = TextAlign.Start
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedLabel,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = "Selecionar sexo",
                                    modifier = Modifier.clickable { expanded = !expanded },
                                    tint = Color.White
                                )
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .width(175.dp)
                                .background(Color(0xFF990410), shape = RoundedCornerShape(8.dp)),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White,
                                cursorColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
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

                    Spacer(modifier = Modifier.height(10.dp))


                    Text(
                        text = "Tipo Sanguíneo",
                        fontSize = 14.sp,
                        color = Color(0xFF990410),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        textAlign = TextAlign.Start
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedTipo,
                        onExpandedChange = { expandedTipo = !expandedTipo }
                    ) {
                        OutlinedTextField(
                            value = selectedLabelTipo,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = "Selecionar tipo sanguíneo",
                                    modifier = Modifier.clickable { expandedTipo = !expandedTipo },
                                    tint = Color.White
                                )
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .width(150.dp)
                                .background(Color(0xFF990410), shape = RoundedCornerShape(8.dp)),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White,
                                cursorColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedTipo,
                            onDismissRequest = { expandedTipo = false }
                        ) {
                            listaTipos.forEach { (id, label) ->
                                DropdownMenuItem(
                                    text = { Text(label) },
                                    onClick = {
                                        selectedTipo = id
                                        selectedLabelTipo = label
                                        expandedTipo = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(17.dp))

                Button(
                    onClick = {

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF990410)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .height(50.dp)
                        .width(200.dp)
                ) {
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TelaInformacaoDoDoadorPreview() {
    val navController = rememberNavController()
    TelaInformacaoDoDoador(navController = navController)
}
