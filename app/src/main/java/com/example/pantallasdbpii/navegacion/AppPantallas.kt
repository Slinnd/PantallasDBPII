package com.example.pantallasdbpii.navegacion

sealed class AppPantallas (val route: String) {
    object pantallaPrincipal : AppPantallas("pantalla_principal")
    object segundaPantalla : AppPantallas("segunda_pantalla")
    object terceraPantalla : AppPantallas("tercera_pantalla")
    object PantallaSuma : AppPantallas("pantalla_suma")
    object PantallaElevarCubo : AppPantallas("pantalla_potencia")
}