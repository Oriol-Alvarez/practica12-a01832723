package mx.tec.charla.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.tec.charla.R
import mx.tec.charla.ui.components.Avatar
import mx.tec.charla.ui.components.sinEsquema
import mx.tec.charla.ui.state.Etapa
import mx.tec.charla.ui.state.UnirseUiState
import mx.tec.charla.ui.theme.CharlaTema
import mx.tec.charla.ui.theme.CharlaTheme

/**
 * Entrar a la sala de alguien más: elegir la sala por una de tres vías,
 * escribir tu nickname, y esperar a que te acepten.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnirseScreen(
    estado: UnirseUiState,
    onAtras: () -> Unit,
    onEscanear: () -> Unit,
    onElegirImagen: () -> Unit,
    onPegar: (String) -> Unit,
    onNickname: (String) -> Unit,
    onPedirEntrar: () -> Unit,
    onCancelar: () -> Unit,
    onOtraSala: () -> Unit
) {
    val e = CharlaTema.espaciado
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Entrar a otra sala") },
                navigationIcon = {
                    IconButton(onClick = onAtras) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { relleno ->
        Column(
            modifier = Modifier
                .padding(relleno)
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = e.lg, vertical = e.sm),
            verticalArrangement = Arrangement.spacedBy(e.md)
        ) {
            when {
                estado.etapa == Etapa.Esperando -> Esperando(estado.nickname, onCancelar)
                estado.servidor == null -> TresVias(onEscanear, onElegirImagen, onPegar)
                else -> Nickname(estado, onNickname, onPedirEntrar, onOtraSala)
            }
            val etapa = estado.etapa
            AnimatedVisibility(etapa is Etapa.Error) {
                if (etapa is Etapa.Error) Problema(etapa.mensaje)
            }
        }
    }
}

@Composable
private fun TresVias(onEscanear: () -> Unit, onElegirImagen: () -> Unit, onPegar: (String) -> Unit) {
    val e = CharlaTema.espaciado
    var pegando by rememberSaveable { mutableStateOf(false) }
    var enlace by rememberSaveable { mutableStateOf("") }
    Text(
        "Pídele a quien te invita su QR o su enlace.",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Opcion(R.drawable.ic_escanear, "Escanear su QR", "Con la cámara. En un teléfono de verdad.", onEscanear)
    Opcion(R.drawable.ic_imagen, "Leer el QR de una imagen", "Una captura que te pasaron.", onElegirImagen)
    Opcion(R.drawable.ic_enlace, "Pegar el enlace", "El que te mandaron por mensaje.") { pegando = true }
    AnimatedVisibility(pegando) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(e.sm)) {
            OutlinedTextField(
                value = enlace,
                onValueChange = { enlace = it },
                modifier = Modifier.weight(1f),
                label = { Text("Enlace de la sala") },
                placeholder = { Text("https://…trycloudflare.com") },
                singleLine = true,
                // Teclado de direcciones y sin autocorrección: si no, «sala-de-ana» se vuelve «sala de ana».
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, autoCorrectEnabled = false)
            )
            Button(onClick = { onPegar(enlace) }, enabled = enlace.isNotBlank()) { Text("Usar") }
        }
    }
}

@Composable
private fun Opcion(icono: Int, titulo: String, detalle: String, onClick: () -> Unit) {
    val c = MaterialTheme.colorScheme
    val e = CharlaTema.espaciado
    ElevatedCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(e.lg),
            horizontalArrangement = Arrangement.spacedBy(e.lg),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(48.dp).background(c.primaryContainer, CircleShape), contentAlignment = Alignment.Center) {
                Icon(painterResource(icono), contentDescription = null, tint = c.onPrimaryContainer)
            }
            Column {
                Text(titulo, style = MaterialTheme.typography.titleMedium)
                Text(detalle, style = MaterialTheme.typography.bodySmall, color = c.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun Nickname(
    estado: UnirseUiState,
    onNickname: (String) -> Unit,
    onPedirEntrar: () -> Unit,
    onOtraSala: () -> Unit
) {
    val c = MaterialTheme.colorScheme
    val e = CharlaTema.espaciado
    Card(colors = CardDefaults.cardColors(containerColor = c.primaryContainer)) {
        Row(Modifier.padding(start = e.lg, end = e.sm, top = e.md, bottom = e.md), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Sala elegida", style = MaterialTheme.typography.labelMedium, color = c.onPrimaryContainer)
                Text(sinEsquema(estado.servidor.orEmpty()), style = MaterialTheme.typography.titleSmall, color = c.onPrimaryContainer)
            }
            TextButton(onClick = onOtraSala) { Text("Cambiar") }
        }
    }
    OutlinedTextField(
        value = estado.nickname,
        onValueChange = onNickname,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Tu nickname") },
        supportingText = { Text("De 2 a 20 caracteres. Así te verán en la sala.") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.None, autoCorrectEnabled = false)
    )
    Button(
        onClick = onPedirEntrar,
        enabled = estado.puedePedir,
        modifier = Modifier.fillMaxWidth().height(52.dp)
    ) {
        if (estado.etapa == Etapa.Enviando) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
        else Text("Pedir entrar")
    }
}

@Composable
private fun Esperando(nickname: String, onCancelar: () -> Unit) {
    val e = CharlaTema.espaciado
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = e.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(e.lg)
    ) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(Modifier.size(112.dp), strokeWidth = 4.dp)
            Avatar(nickname.ifBlank { "?" }, tamano = 80.dp)
        }
        Text("Esperando a que te acepten…", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
        Text(
            "Tu solicitud ya está en la pantalla del anfitrión.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(e.sm))
        OutlinedButton(onClick = onCancelar) { Text("Cancelar") }
    }
}

@Composable
private fun Problema(mensaje: String) {
    val e = CharlaTema.espaciado
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Text(mensaje, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(e.lg))
    }
}

@Preview(showBackground = true, heightDp = 720)
@Composable
private fun UnirsePreview() {
    CharlaTheme {
        UnirseScreen(UnirseUiState(), {}, {}, {}, {}, {}, {}, {}, {})
    }
}
