package com.example.doevida.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.doevida.R

@Composable
fun TelaCadastro(navController: NavController) {

    val nomeCompleto = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val senha = remember { mutableStateOf("") }
    val confirmarSenha = remember { mutableStateOf("") }

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
            Spacer(modifier = Modifier.height(100.dp))

            Image(
                painter = painterResource(id = R.drawable.logocadastro),
                contentDescription = "Logo cadastro DOEVIDA",
                modifier = Modifier
                    .size(170.dp)
                    .padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(39.dp))

            Column {
                // Nome Completo
                Text(
                    text = "Nome Completo",
                    fontSize = 14.sp,
                    color = Color(0xFF990410),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp, start = 1.dp),
                    textAlign = TextAlign.Start
                )
                OutlinedTextField(
                    value = nomeCompleto.value,
                    onValueChange = { nomeCompleto.value = it },
                    placeholder = { Text("Digite seu nome completo", color = Color(0x80FFFFFF)) },
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

                Spacer(modifier = Modifier.height(16.dp))

                // E-mail
                Text(
                    text = "E-mail",
                    fontSize = 14.sp,
                    color = Color(0xFF990410),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp, start = 1.dp),
                    textAlign = TextAlign.Start
                )
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    placeholder = { Text("Digite seu e-mail", color = Color(0x80FFFFFF)) },
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

                Spacer(modifier = Modifier.height(16.dp))

                // Senha
                Text(
                    text = "Digite sua senha",
                    fontSize = 14.sp,
                    color = Color(0xFF990410),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp, start = 1.dp),
                    textAlign = TextAlign.Start
                )
                OutlinedTextField(
                    value = senha.value,
                    onValueChange = { senha.value = it },
                    placeholder = { Text("Digite sua senha", color = Color(0x80FFFFFF)) },
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

                Spacer(modifier = Modifier.height(16.dp))

                // Confirmar Senha
                Text(
                    text = "Confirme sua senha",
                    fontSize = 14.sp,
                    color = Color(0xFF990410),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp, start = 1.dp),
                    textAlign = TextAlign.Start
                )
                OutlinedTextField(
                    value = confirmarSenha.value,
                    onValueChange = { confirmarSenha.value = it },
                    placeholder = { Text("Confirme sua senha", color = Color(0x80FFFFFF)) },
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
            }

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF990410)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(48.dp)
                    .width(133.dp)
            ) {
                Text(
                    text = "Criar conta",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = "Já tem uma conta?",
                color = Color(0xFF990410),
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 10.dp)
            )

            Text(
                text = "Fazer login",
                color = Color(0xFFB71C1C),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TelaCadastroPreview() {
    val navController = rememberNavController()
    TelaCadastro(navController = navController)
}
