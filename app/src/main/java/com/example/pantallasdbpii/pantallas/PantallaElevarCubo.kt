package com.example.pantallasdbpii.pantallas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaElevarCubo(){
    Box(modifier = Modifier.fillMaxSize().padding(50.dp),
        contentAlignment = Alignment.Center) {
        Text("Pantalla que eleva al cubo")
    }
}