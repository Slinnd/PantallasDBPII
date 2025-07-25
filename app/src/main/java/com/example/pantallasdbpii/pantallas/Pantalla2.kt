package com.example.pantallasdbpii.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pantallasdbpii.R
import com.example.pantallasdbpii.navegacion.AppPantallas

@Composable
fun Pantalla2(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    var loginUsername by remember { mutableStateOf("") }
    var loginCode by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }
    var registerUsername by remember { mutableStateOf("") }
    var registerCode by remember { mutableStateOf("") }
    var registerPassword by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }
    var registerError by remember { mutableStateOf(false) }
    val tabs = listOf("Ingresar", "Registrarse")

    // Usuarios válidos para login
    val validLoginUsers = mapOf(
        "73517333" to mapOf("codigo" to "215385", "password" to "08062000"),
        "76546319" to mapOf("codigo" to "236302", "password" to "29042005"),
        "75921149" to mapOf("codigo" to "236262", "password" to "12092004")
    )

    // Lista para almacenar nuevos usuarios registrados
    var registeredUsers by remember { mutableStateOf(listOf<Map<String, String>>()) }
    //Box(modifier = Modifier.background(Color(0xFF2196F3))) {



    // Contenedor principal con fondo azul claro
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF8CBBEC)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .padding(24.dp)
                .background(Color(0xFF8CBBEC)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logos
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_unap),
                    contentDescription = "Logo Unap",
                    modifier = Modifier.size(80.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.episunap),
                    contentDescription = "Logo Sistemas",
                    modifier = Modifier.size(80.dp)
                )
            }

            // Título del equipo
            Text(
                text = "Lástimas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Subtítulo
            Text(
                text = "Solo que nada que nada se...",
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Pestañas
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = Color.Green,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color(0xFF11457C),
                        height = 3.dp
                    )
                },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = {
                            Text(
                                title,
                                fontSize = 16.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            // Limpiar errores al cambiar de pestaña
                            loginError = false
                            registerError = false
                        },
                        selectedContentColor = Color(0xFF11457C),
                        unselectedContentColor = Color.Black
                    )
                }
            }

            // Contenedor del formulario con borde
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, Color.Black, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                // Contenido dinámico basado en la pestaña seleccionada
                when (selectedTab) {
                    0 -> LoginContent(
                        username = loginUsername,
                        onUsernameChange = {
                            loginUsername = it
                            loginError = false
                        },
                        code = loginCode,
                        onCodeChange = {
                            loginCode = it
                            loginError = false
                        },
                        password = loginPassword,
                        onPasswordChange = {
                            loginPassword = it
                            loginError = false
                        },
                        error = loginError
                    )

                    1 -> RegisterContent(
                        username = registerUsername,
                        onUsernameChange = {
                            registerUsername = it
                            registerError = false
                        },
                        code = registerCode,
                        onCodeChange = {
                            registerCode = it
                            registerError = false
                        },
                        password = registerPassword,
                        onPasswordChange = {
                            registerPassword = it
                            registerError = false
                        },
                        error = registerError
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón principal
            Button(
                onClick = {
                    when (selectedTab) {
                        0 -> { // Login - validar usuario, código y contraseña
                            val userInfo = validLoginUsers[loginUsername]
                            if (userInfo != null &&
                                userInfo["codigo"] == loginCode &&
                                userInfo["password"] == loginPassword
                            ) {
                                loginError = false
                                navController.popBackStack()
                                navController.navigate(AppPantallas.terceraPantalla.route)
                            } else {
                                loginError = true
                            }
                        }

                        1 -> { // Registro - agregar nuevo usuario a la lista
                            if (registerUsername.isNotEmpty() &&
                                registerCode.isNotEmpty() &&
                                registerPassword.isNotEmpty()
                            ) {

                                // Crear nuevo usuario
                                val newUser = mapOf(
                                    "usuario" to registerUsername,
                                    "codigo" to registerCode,
                                    "password" to registerPassword
                                )

                                // Agregar a la lista de usuarios registrados
                                registeredUsers = registeredUsers + newUser

                                registerError = false
                                navController.popBackStack()
                                navController.navigate(AppPantallas.terceraPantalla.route)
                            } else {
                                registerError = true
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF33AB5F)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(140.dp)
                    .height(48.dp)
            ) {
                Text(
                    text = if (selectedTab == 0) "Ingresar" else "Registrarse",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }


//}
}

@Composable
fun LoginContent(
    username: String,
    onUsernameChange: (String) -> Unit,
    code: String,
    onCodeChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    error: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("Usuario") },
            isError = error,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (error) Color.Red else Color.Gray,
                unfocusedBorderColor = if (error) Color.Red else Color.Gray,
                focusedLabelColor = if (error) Color.Red else Color.Gray,
                unfocusedLabelColor = if (error) Color.Red else Color.Gray
            )
        )

        OutlinedTextField(
            value = code,
            onValueChange = onCodeChange,
            label = { Text("Código") },
            isError = error,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (error) Color.Red else Color.Gray,
                unfocusedBorderColor = if (error) Color.Red else Color.Gray,
                focusedLabelColor = if (error) Color.Red else Color.Gray,
                unfocusedLabelColor = if (error) Color.Red else Color.Gray
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = error,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = if (error) 8.dp else 12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (error) Color.Red else Color.Gray,
                unfocusedBorderColor = if (error) Color.Red else Color.Gray,
                focusedLabelColor = if (error) Color.Red else Color.Gray,
                unfocusedLabelColor = if (error) Color.Red else Color.Gray
            )
        )

        if (error) {
            Text(
                text = "Usuario, código o contraseña incorrectos",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterContent(
    username: String,
    onUsernameChange: (String) -> Unit,
    code: String,
    onCodeChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    error: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        var gender by remember { mutableStateOf("Género") }
        var confirmPassword by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }
        val genderOptions = listOf("Masculino", "Femenino", "Otro")

        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("Usuario") },
            isError = error,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (error) Color.Red else Color.Gray,
                unfocusedBorderColor = if (error) Color.Red else Color.Gray,
                focusedLabelColor = if (error) Color.Red else Color.Gray,
                unfocusedLabelColor = if (error) Color.Red else Color.Gray
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = code,
                onValueChange = onCodeChange,
                label = { Text("Código") },
                isError = error,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (error) Color.Red else Color.Gray,
                    unfocusedBorderColor = if (error) Color.Red else Color.Gray,
                    focusedLabelColor = if (error) Color.Red else Color.Gray,
                    unfocusedLabelColor = if (error) Color.Red else Color.Gray
                )
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Género") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray
                    ),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                gender = option
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = error,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (error) Color.Red else Color.Gray,
                unfocusedBorderColor = if (error) Color.Red else Color.Gray,
                focusedLabelColor = if (error) Color.Red else Color.Gray,
                unfocusedLabelColor = if (error) Color.Red else Color.Gray
            )
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = if (error) 8.dp else 12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.Gray,
                unfocusedLabelColor = Color.Gray
            )
        )

        if (error) {
            Text(
                text = "Todos los campos son requeridos",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        }
    }
}