package com.example.doevida.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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


    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)
    ){
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
            verticalArrangement = Arrangement.Top // <- muda para Top
        ) {
            Spacer(modifier = Modifier.height(100.dp)) // espaço para afastar do topo

            Image(
                painter = painterResource(id = R.drawable.logocadastro),
                contentDescription = "Logo cadastro DOEVIDA",
                modifier = Modifier
                    .size(170.dp)
                    .padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(39.dp)) // espaço entre logo e formulário


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 1.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Nome Completo",
                    fontSize = 14.sp,
                    color = Color(0xFF990410),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 4.dp)
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


                Text(
                    text = "E-mail",
                    fontSize = 14.sp,
                    color = Color(0xFF990410),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = nomeCompleto.value,
                    onValueChange = { nomeCompleto.value = it },
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


                Text(
                    text = "Digite sua senha",
                    fontSize = 14.sp,
                    color = Color(0xFF990410),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = nomeCompleto.value,
                    onValueChange = { nomeCompleto.value = it },
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

                Text(
                    text = "Confirme sua senha",
                    fontSize = 14.sp,
                    color = Color(0xFF990410),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = nomeCompleto.value,
                    onValueChange = { nomeCompleto.value = it },
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
                modifier = Modifier
                    .clickable { navController.navigate("tela_login")},
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