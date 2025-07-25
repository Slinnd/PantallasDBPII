package com.example.pantallasdbpii.pantallas

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pantallasdbpii.R
import kotlinx.coroutines.launch

// Modelo de datos para los registros
data class Registro(
    val id: Int,
    val titulo: String,
    val tipo: String, // "Ingreso" o "Egreso"
    val monto: Float,
    val moneda: String,
    val descripcion: String,
    val ordinario: Boolean,
    val empresa: Boolean,
    val aprobado: Boolean,
    val centroEstudios: Boolean,
    val emergencia: Boolean,
    val imagenId: Int
)

@Composable
fun Pantalla3(navController: NavController) {
    var seccionActual by remember { mutableStateOf("inicio") }
    val snackbarHostState = remember { SnackbarHostState() }

    // Estado compartido para todos los registros
    var registros by remember { mutableStateOf(listOf<Registro>()) }
    var contadorId by remember { mutableStateOf(1) }

    // Función para agregar un nuevo registro
    val agregarRegistro: (Registro) -> Unit = { nuevoRegistro ->
        registros = registros + nuevoRegistro.copy(id = contadorId)
        contadorId++
    }

    // Calcular totales basados en los registros - CORREGIDO
    val totalIngresos = registros.filter { it.tipo == "Ingreso" }.sumOf { it.monto.toDouble() }.toFloat()
    val totalEgresos = registros.filter { it.tipo == "Egreso" }.sumOf { it.monto.toDouble() }.toFloat()
    val disponible = totalIngresos - totalEgresos

    val valores = listOf(totalIngresos, totalEgresos, maxOf(disponible, 0f))
    val colores = listOf(
        Color(0xFF4CAF50), // Verde para ingresos
        Color(0xFFF44336), // Rojo para egresos
        Color(0xFF2196F3)  // Azul para disponible
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = seccionActual == "inicio",
                    onClick = { seccionActual = "inicio" },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = seccionActual == "registrar",
                    onClick = { seccionActual = "registrar" },
                    icon = { Icon(Icons.Default.MailOutline, contentDescription = "Registrar") },
                    label = { Text("Registrar") }
                )
                NavigationBarItem(
                    selected = seccionActual == "listar",
                    onClick = { seccionActual = "listar" },
                    icon = { Icon(Icons.Default.List, contentDescription = "Listar") },
                    label = { Text("Listar") }
                )
                NavigationBarItem(
                    selected = seccionActual == "sobreMi",
                    onClick = { seccionActual = "sobreMi" },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Sobre mí") },
                    label = { Text("Sobre mi") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (seccionActual) {
                "inicio" -> InicioView(valores, colores, totalIngresos, totalEgresos, disponible)
                "registrar" -> RegistrarView(snackbarHostState, agregarRegistro)
                "listar" -> ListarView(registros)
                "sobreMi" -> IntegrantesView()
                else -> Text("Sección: $seccionActual", fontSize = 24.sp)
            }
        }
    }
}

@Composable
fun InicioView(valores: List<Float>, colores: List<Color>, totalIngresos: Float, totalEgresos: Float, disponible: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (valores.sumOf { it.toDouble() } > 0) { // CORREGIDO: usar sumOf
                PieChartManual(values = valores, colors = colores)
            } else {
                // Mostrar un círculo gris cuando no hay datos
                Canvas(modifier = Modifier.size(180.dp)) {
                    drawCircle(
                        color = Color.Gray,
                        radius = size.minDimension / 2,
                        center = center
                    )
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            "Sin datos",
                            center.x,
                            center.y,
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.WHITE
                                textAlign = android.graphics.Paint.Align.CENTER
                                textSize = 32f
                                isFakeBoldText = true
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "S/. ${String.format("%.2f", disponible)}",
                fontSize = 28.sp,
                color = if (disponible >= 0) Color.Green else Color.Red
            )
            Text("Saldo Disponible", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Ingresos", color = Color.Gray)
                    Text("S/. ${String.format("%.2f", totalIngresos)}", color = colores[0], fontSize = 16.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Egresos", color = Color.Gray)
                    Text("S/. ${String.format("%.2f", totalEgresos)}", color = colores[1], fontSize = 16.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Disponible", color = Color.Gray)
                    Text(
                        "S/. ${String.format("%.2f", disponible)}",
                        color = if (disponible >= 0) colores[2] else Color.Red,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

// Función para convertir el monto a soles
fun convertirASoles(monto: Float, moneda: String): Float {
    return when (moneda) {
        "Dólares" -> monto * 3.5f
        "Euros" -> monto * 4.0f
        "Soles" -> monto
        else -> monto
    }
}

@Composable
fun ListarView(registros: List<Registro>) {
    var busqueda by remember { mutableStateOf("") }

    val registrosFiltrados = registros.filter {
        it.titulo.contains(busqueda, ignoreCase = true) || it.tipo.contains(busqueda, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            placeholder = { Text("Buscar por título o tipo...") },
            leadingIcon = { Icon(Icons.Default.List, contentDescription = "Buscar") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (registrosFiltrados.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (busqueda.isEmpty()) "No hay registros aún" else "No se encontraron resultados",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    if (busqueda.isEmpty()) {
                        Text(
                            text = "Agrega tu primer registro en la sección 'Registrar'",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            registrosFiltrados.forEach { registro ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = registro.imagenId),
                            contentDescription = "Imagen ${registro.titulo}",
                            modifier = Modifier
                                .size(48.dp)
                                .padding(end = 12.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(registro.titulo, fontSize = 16.sp)
                            Text(registro.tipo, fontSize = 14.sp, color = Color.Gray)
                            if (registro.descripcion.isNotEmpty()) {
                                Text(
                                    registro.descripcion,
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    maxLines = 1
                                )
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                "S/. ${String.format("%.2f", registro.monto)}",
                                fontSize = 16.sp,
                                color = if (registro.tipo == "Ingreso") Color.Green else Color.Red
                            )
                            // Mostrar etiquetas si están activas
                            val etiquetas = mutableListOf<String>()
                            if (registro.ordinario) etiquetas.add("Ord")
                            if (registro.empresa) etiquetas.add("Emp")
                            if (registro.aprobado) etiquetas.add("Apr")
                            if (registro.centroEstudios) etiquetas.add("Est")
                            if (registro.emergencia) etiquetas.add("Emg")

                            if (etiquetas.isNotEmpty()) {
                                Text(
                                    etiquetas.joinToString(", "),
                                    fontSize = 10.sp,
                                    color = Color.Blue
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarView(snackbarHostState: SnackbarHostState, agregarRegistro: (Registro) -> Unit) {
    val scope = rememberCoroutineScope()

    var flujoSeleccionado by remember { mutableStateOf("Ingreso") }
    var monto by remember { mutableStateOf("") }
    var monedaSeleccionada by remember { mutableStateOf("Soles") }
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    var ordinario by remember { mutableStateOf(false) }
    var empresa by remember { mutableStateOf(false) }
    var aprobado by remember { mutableStateOf(false) }
    var centroEstudios by remember { mutableStateOf(false) }
    var emergencia by remember { mutableStateOf(false) }

    var mostrarDialogo by remember { mutableStateOf(false) }

    // Validación de campos
    val camposValidos = titulo.isNotEmpty() && monto.isNotEmpty() && monto.toFloatOrNull() != null && monto.toFloat() > 0

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Confirmación") },
            text = { Text("¿Desea continuar con el registro, dado que no se puede editar?") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogo = false

                    // Crear el nuevo registro
                    val montoEnSoles = convertirASoles(monto.toFloat(), monedaSeleccionada)
                    val nuevoRegistro = Registro(
                        id = 0, // Se asignará automáticamente
                        titulo = titulo,
                        tipo = flujoSeleccionado,
                        monto = montoEnSoles, // AHORA CONVIERTE A SOLES
                        moneda = "Soles", // SIEMPRE ALMACENAR COMO SOLES
                        descripcion = descripcion,
                        ordinario = ordinario,
                        empresa = empresa,
                        aprobado = aprobado,
                        centroEstudios = centroEstudios,
                        emergencia = emergencia,
                        imagenId = if (flujoSeleccionado == "Ingreso") R.drawable.ingreso1 else R.drawable.egreso1
                    )

                    // Agregar el registro
                    agregarRegistro(nuevoRegistro)

                    // Mostrar snackbar
                    scope.launch {
                        snackbarHostState.showSnackbar("Registro exitoso: ${flujoSeleccionado} de S/. ${monto}")
                    }

                    // Limpiar campos
                    flujoSeleccionado = "Ingreso"
                    monto = ""
                    monedaSeleccionada = "Soles"
                    titulo = ""
                    descripcion = ""
                    ordinario = false
                    empresa = false
                    aprobado = false
                    centroEstudios = false
                    emergencia = false
                }) {
                    Text("Ok")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Selector de flujo
        var flujoExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = flujoExpanded,
            onExpandedChange = { flujoExpanded = !flujoExpanded }
        ) {
            TextField(
                value = flujoSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Flujo") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = flujoExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = flujoExpanded,
                onDismissRequest = { flujoExpanded = false }
            ) {
                listOf("Ingreso", "Egreso").forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            flujoSeleccionado = opcion
                            flujoExpanded = false
                        }
                    )
                }
            }
        }

        // Selector de moneda y monto
        var monedaExpanded by remember { mutableStateOf(false) }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ExposedDropdownMenuBox(
                expanded = monedaExpanded,
                onExpandedChange = { monedaExpanded = !monedaExpanded }
            ) {
                TextField(
                    value = when (monedaSeleccionada) {
                        "Soles" -> "S/."
                        "Dólares" -> "$"
                        "Euros" -> "€"
                        else -> monedaSeleccionada
                    },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Moneda") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = monedaExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .width(100.dp)
                )
                ExposedDropdownMenu(
                    expanded = monedaExpanded,
                    onDismissRequest = { monedaExpanded = false }
                ) {
                    listOf("Soles", "Dólares", "Euros").forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                monedaSeleccionada = opcion
                                monedaExpanded = false
                            }
                        )
                    }
                }
            }

            TextField(
                value = monto,
                onValueChange = {
                    // Solo permitir números y un punto decimal
                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                        monto = it
                    }
                },
                label = { Text("Monto") },
                isError = monto.isNotEmpty() && (monto.toFloatOrNull() == null || monto.toFloat() <= 0),
                supportingText = {
                    if (monto.isNotEmpty() && (monto.toFloatOrNull() == null || monto.toFloat() <= 0)) {
                        Text("Ingrese un monto válido mayor a 0", color = Color.Red)
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }

        // Título (obligatorio)
        TextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título *") },
            isError = titulo.isEmpty(),
            supportingText = {
                if (titulo.isEmpty()) {
                    Text("Campo obligatorio", color = Color.Red)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Descripción (opcional)
        TextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Checkboxes
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = ordinario, onCheckedChange = { ordinario = it })
                    Text("Ordinario")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = aprobado, onCheckedChange = { aprobado = it })
                    Text("Aprobado")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = emergencia, onCheckedChange = { emergencia = it })
                    Text("Emergencia")
                }
            }
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = empresa, onCheckedChange = { empresa = it })
                    Text("Empresa")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = centroEstudios, onCheckedChange = { centroEstudios = it })
                    Text("Centro de estudios")
                }
            }
        }

        // Botón Registrar
        Button(
            onClick = { mostrarDialogo = true },
            enabled = camposValidos,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Registrar")
        }

        if (!camposValidos) {
            Text(
                "Complete los campos obligatorios (*) con valores válidos",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun IntegrantesView() {
    val context = LocalContext.current
    var imagenSeleccionada by remember { mutableStateOf<Int?>(null) }

    // Diálogo para mostrar imagen completa
    if (imagenSeleccionada != null) {
        AlertDialog(
            onDismissRequest = { imagenSeleccionada = null },
            title = { Text("Imagen completa") },
            text = {
                Image(
                    painter = painterResource(id = imagenSeleccionada!!),
                    contentDescription = "Imagen completa",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                )
            },
            confirmButton = {
                TextButton(onClick = { imagenSeleccionada = null }) {
                    Text("Cerrar")
                }
            }
        )
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Grupo Lastima", fontSize = 20.sp, style = MaterialTheme.typography.titleMedium)
        Text("Integrantes", fontSize = 28.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            integranteCard(R.drawable.joel, "236302", "Joel Edson Huerta B.") { imagenSeleccionada = R.drawable.joel }
            integranteCard(R.drawable.dino, "236262", "Dino Jhoel Condori Ch.") { imagenSeleccionada = R.drawable.dino }
            integranteCard(R.drawable.denilson, "215385", "Denilson Lope Soncco") { imagenSeleccionada = R.drawable.denilson }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            val url = "https://github.com/Slinnd/PantallasDBPII_EVALU"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }) {
            Text("Ir a código fuente en GIT")
        }
    }
}
// Modifica la función integranteCard para recibir el callback:
@Composable
fun integranteCard(imagenId: Int, codigo: String, nombre: String, onVerImagen: () -> Unit) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = imagenId),
                contentDescription = "Foto de $nombre",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("<$codigo>", style = MaterialTheme.typography.labelSmall)
            Text(nombre, fontSize = 14.sp)
            Button(
                onClick = onVerImagen, // CAMBIO AQUÍ
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text("V/D")
            }
        }
    }
}
@Composable
fun PieChartManual(
    values: List<Float>,
    colors: List<Color>,
    modifier: Modifier = Modifier.size(180.dp)
) {
    val total = values.sumOf { it.toDouble() }.toFloat() // CORREGIDO: convertir a Double primero
    var startAngle = -90f

    Canvas(modifier = modifier) {
        val radius = size.minDimension / 2
        val center = Offset(size.width / 2, size.height / 2)

        values.forEachIndexed { index, value ->
            if (value > 0) {
                val sweepAngle = (value / total) * 360f
                val angleInRad = Math.toRadians((startAngle + sweepAngle / 2).toDouble())

                // Dibuja el arco
                drawArc(
                    color = colors[index],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset.Zero,
                    size = Size(size.width, size.height)
                )

                // Calcular posición del texto solo si el valor es significativo
                if (sweepAngle > 10f) { // Solo mostrar porcentaje si el segmento es lo suficientemente grande
                    val labelRadius = radius * 0.6f
                    val labelX = center.x + labelRadius * kotlin.math.cos(angleInRad).toFloat()
                    val labelY = center.y + labelRadius * kotlin.math.sin(angleInRad).toFloat()

                    // Dibujar el porcentaje
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            "${((value / total) * 100).toInt()}%",
                            labelX,
                            labelY,
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.WHITE
                                textAlign = android.graphics.Paint.Align.CENTER
                                textSize = 32f
                                isFakeBoldText = true
                            }
                        )
                    }
                }

                startAngle += sweepAngle
            }
        }
    }
}