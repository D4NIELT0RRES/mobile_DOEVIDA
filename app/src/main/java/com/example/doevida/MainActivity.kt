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
import com.example.doevida.model.HistoricoDoacaoCombinado
import com.example.doevida.screen.*
import com.example.doevida.ui.theme.DoevidaTheme
import com.google.gson.Gson
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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
        startDestination = "tela_inicial"
    ) {
        composable("splash") { TelaSplash(navController) }
        composable("tela_inicial") { TelaInicial(navController) }
        composable("tela_login") { TelaLogin(navController) }
        composable("tela_cadastro") { TelaCadastro(navController) }
        composable("tela_home") { TelaHome(navController) }
        composable("tela_recuperacao") { TelaRecuperacaoEmail(navController) }
        composable("tela_redefinir_senha") { TelaRedefinirSenha(navController) }
        composable("tela_hospitais") { TelaHospitais(navController) }
        composable("tela_historico") { TelaHistorico(navController) }
        composable("tela_banco_sangue") { TelaBancodeSangue(navController) }
        composable("tela_agendamento") { TelaAgendamento(navController) }
        composable("tela_perfil") { TelaPerfil(navController) }
        composable("tela_certificado") { TelaCertificado(navController) }
        composable("tela_registrar_doacao") { TelaRegistrarDoacao(navController) }
        composable("tela_noticias") { TelaNoticias(navController) }

        composable(
            route = "tela_informacao/{hospitalId}/{data}/{horario}",
            arguments = listOf(
                navArgument("hospitalId") { type = NavType.IntType },
                navArgument("data") { type = NavType.StringType },
                navArgument("horario") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val hospitalId = backStackEntry.arguments?.getInt("hospitalId") ?: 0
            val data = backStackEntry.arguments?.getString("data") ?: ""
            val horario = backStackEntry.arguments?.getString("horario") ?: ""
            TelaInformacaoDoDoador(navController, hospitalId, data, horario)
        }

        composable(
            route = "tela_detalhes_hospitais/{hospitalId}",
            arguments = listOf(navArgument("hospitalId") { type = NavType.IntType })
        ) { backStackEntry ->
            val hospitalId = backStackEntry.arguments?.getInt("hospitalId") ?: 0
            TelaDetalheHospital(navController, hospitalId)
        }

        composable(
            route = "tela_protocolo/{hospitalId}/{data}/{horario}",
            arguments = listOf(
                navArgument("hospitalId") { type = NavType.IntType },
                navArgument("data") { type = NavType.StringType },
                navArgument("horario") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val hospitalId = backStackEntry.arguments?.getInt("hospitalId") ?: 0
            val data = backStackEntry.arguments?.getString("data") ?: ""
            val horario = backStackEntry.arguments?.getString("horario") ?: ""
            TelaProtocoloAgendamento(navController, hospitalId, data, horario)
        }

        composable(
            route = "tela_detalhe_noticia/{noticiaId}",
            arguments = listOf(navArgument("noticiaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val noticiaId = backStackEntry.arguments?.getInt("noticiaId") ?: 0
            TelaDetalheNoticia(navController, noticiaId)
        }

        // Rota corrigida para receber o JSON, decodificar e passar o objeto para a tela
        composable(
            route = "tela_detalhe_doacao/{doacaoJson}",
            arguments = listOf(navArgument("doacaoJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val doacaoJson = backStackEntry.arguments?.getString("doacaoJson")?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.name())
            }
            val doacao = doacaoJson?.let { Gson().fromJson(it, HistoricoDoacaoCombinado::class.java) }

            if (doacao != null) {
                TelaDetalheDoacao(navController, doacao)
            }
        }
    }
}
