package com.example.pantallasdbpii.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pantallasdbpii.pantallas.Pantalla2
import com.example.pantallasdbpii.pantallas.Pantalla3
import com.example.pantallasdbpii.pantallas.PantallaElevarCubo
import com.example.pantallasdbpii.pantallas.PantallaInicio
import com.example.pantallasdbpii.pantallas.PantallaSuma

@Composable
fun AppNavegacion(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppPantallas.pantallaPrincipal.route
    ) {
        composable(AppPantallas.pantallaPrincipal.route){
            PantallaInicio(navController)
        }

        composable(AppPantallas.segundaPantalla.route){
            Pantalla2(navController)
        }

        composable(AppPantallas.terceraPantalla.route){
            Pantalla3(navController)
        }

        composable("pantalla_suma"){
            PantallaSuma()
        }

        composable(AppPantallas.PantallaElevarCubo.route){
            PantallaElevarCubo()
        }

    }
}