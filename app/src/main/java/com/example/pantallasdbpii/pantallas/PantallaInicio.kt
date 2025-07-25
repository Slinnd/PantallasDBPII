package com.example.pantallasdbpii.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pantallasdbpii.R
import com.example.pantallasdbpii.navegacion.AppPantallas
import kotlinx.coroutines.delay

@Composable
fun PantallaInicio(navController: NavController) {
    //Navegar automáticamente después de 1.5 segundos
    LaunchedEffect(true) {
        delay(1500)
        navController.popBackStack()
        navController.navigate(AppPantallas.segundaPantalla.route)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF072236)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Imagen principal (logo_unap)
        Image(
            painter = painterResource(id = R.drawable.logo_unap),
            contentDescription = "Logo UNAP",
            modifier = Modifier
                //.padding(top = 130.dp)
                .size(200.dp)
        )

        //Imagen secundaria (logo.png)
        /*Row(
            modifier = Modifier
                .fillMaxWidth().background(Color.Red)
                .padding(bottom = 60.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo adicional",
                modifier = Modifier.size(100.dp)
            )
        }*/
    }
}