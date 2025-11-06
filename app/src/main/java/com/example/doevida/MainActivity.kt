package com.example.doevida

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.doevida.screen.TelaAgendamento
import com.example.doevida.screen.TelaBancodeSangue
import com.example.doevida.screen.TelaCadastro
import com.example.doevida.screen.TelaCertificado
import com.example.doevida.screen.TelaDetalheHospital
import com.example.doevida.screen.TelaHistorico
import com.example.doevida.screen.TelaHome
import com.example.doevida.screen.TelaHospitais
import com.example.doevida.screen.TelaInformacaoDoDoador
import com.example.doevida.screen.TelaInicial
import com.example.doevida.screen.TelaLogin
import com.example.doevida.screen.TelaPerfil
import com.example.doevida.screen.TelaProtocoloAgendamento
import com.example.doevida.screen.TelaRecuperacaoEmail
import com.example.doevida.screen.TelaRedefinirSenha
import com.example.doevida.screen.TelaRegistrarDoacao
import com.example.doevida.screen.TelaSplash
import com.example.doevida.ui.theme.DoevidaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoevidaTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "tela_home"
    ) {
        composable("splash") {
            TelaSplash(navController)
        }
        composable("tela_inicial") {
            TelaInicial(navController)
        }
        composable("tela_login") {
            TelaLogin(navController)
        }
        composable("tela_cadastro") {
            TelaCadastro(navController)
        }
        composable("tela_home") {
            TelaHome(navController)
        }
        composable("tela_recuperacao") {
            TelaRecuperacaoEmail(navController)
        }
        composable(
            route = "tela_informacao/{hospitalId}/{data}/{horario}", // Rota atualizada
            arguments = listOf(
                navArgument("hospitalId") { type = NavType.IntType },
                navArgument("data") { type = NavType.StringType },
                navArgument("horario") { type = NavType.StringType } // Novo argumento
            )
        ) { backStackEntry ->
            val hospitalId = backStackEntry.arguments?.getInt("hospitalId") ?: 0
            val data = backStackEntry.arguments?.getString("data") ?: ""
            val horario = backStackEntry.arguments?.getString("horario") ?: "" // Novo argumento
            TelaInformacaoDoDoador(navController, hospitalId, data, horario)
        }
        composable("tela_redefinir_senha") {
            TelaRedefinirSenha(navController)
        }
        composable(
            route = "tela_detalhes_hospitais/{hospitalId}",
            arguments = listOf(navArgument("hospitalId") { type = NavType.IntType })
        ) { backStackEntry ->
            val hospitalId = backStackEntry.arguments?.getInt("hospitalId") ?: 0
            TelaDetalheHospital(navController, hospitalId)
        }
        composable("tela_hospitais") {
            TelaHospitais(navController)
        }
        composable("tela_historico") {
            TelaHistorico(navController)
        }
        composable("tela_banco_sangue") {
            TelaBancodeSangue(navController)
        }
        composable("tela_agendamento") {
            TelaAgendamento(navController)
        }
        composable(
            route = "tela_protocolo/{hospitalId}/{data}/{horario}", // Rota atualizada
            arguments = listOf(
                navArgument("hospitalId") { type = NavType.IntType },
                navArgument("data") { type = NavType.StringType },
                navArgument("horario") { type = NavType.StringType } // Novo argumento
            )
        ) { backStackEntry ->
            val hospitalId = backStackEntry.arguments?.getInt("hospitalId") ?: 0
            val data = backStackEntry.arguments?.getString("data") ?: ""
            val horario = backStackEntry.arguments?.getString("horario") ?: ""
            TelaProtocoloAgendamento(
                navController = navController,
                hospitalId = hospitalId,
                dataSelecionada = data,
                horarioSelecionado = horario // Novo argumento
            )
        }
        composable("tela_perfil") {
            TelaPerfil(navController)
        }
        composable("tela_certificado") {
            TelaCertificado(navController)
        }
        composable("tela_registrar_doacao") {
            TelaRegistrarDoacao(navController)
        }
    }
}
