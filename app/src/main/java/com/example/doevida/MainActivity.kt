 package com.example.doevida

 import android.os.Bundle
 import androidx.activity.ComponentActivity
 import androidx.activity.compose.setContent
 import androidx.activity.enableEdgeToEdge
 import androidx.compose.runtime.Composable
 import androidx.navigation.compose.NavHost
 import androidx.navigation.compose.composable
 import androidx.navigation.compose.rememberNavController
 import com.example.doevida.screen.TelaInicial
 import com.example.doevida.screen.TelaLogin
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
         startDestination = "tela_inicial"
     ) {
         composable("tela_inicial") {
             TelaInicial(navController)
         }
         composable("tela_login") {
             TelaLogin(navController)
         }
     }
 }

