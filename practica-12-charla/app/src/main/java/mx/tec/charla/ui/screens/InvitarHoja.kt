package mx.tec.charla.ui.screens

import android.content.ClipData
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mx.tec.charla.R
import mx.tec.charla.ui.state.InvitacionUi
import mx.tec.charla.ui.theme.CharlaTema

/** Tu QR y tu enlace: las dos formas de que alguien encuentre tu sala. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitarHoja(invitacion: InvitacionUi, onReintentar: () -> Unit, onCerrar: () -> Unit) {
    val e = CharlaTema.espaciado
    ModalBottomSheet(onDismissRequest = onCerrar) {
        Column(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = e.xl).padding(bottom = e.xl),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Invita a tu sala", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.size(e.xs))
            Text(
                "Que lo escaneen, o pásales el enlace.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.size(e.xl))
            when (invitacion) {
                InvitacionUi.Cerrada, InvitacionUi.Cargando -> Box(Modifier.size(264.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                is InvitacionUi.Lista -> Lista(invitacion)
                is InvitacionUi.Error -> Falla(invitacion.mensaje, onReintentar)
            }
        }
    }
}

@Composable
private fun Lista(invitacion: InvitacionUi.Lista) {
    val e = CharlaTema.espaciado
    val contexto = LocalContext.current
    val portapapeles = LocalClipboard.current
    val alcance = rememberCoroutineScope()
    val qr = remember(invitacion.qr) { invitacion.qr.asImageBitmap() }
    // Blanco a propósito, también en modo oscuro: un QR claro sobre oscuro muchos lectores no lo leen.
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = MaterialTheme.shapes.extraLarge) {
        Image(
            bitmap = qr,
            contentDescription = "Código QR de tu sala",
            filterQuality = FilterQuality.None,
            modifier = Modifier.padding(e.lg).size(232.dp)
        )
    }
    Spacer(Modifier.size(e.lg))
    Text(
        invitacion.url,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
    Spacer(Modifier.size(e.lg))
    Row(horizontalArrangement = Arrangement.spacedBy(e.sm)) {
        OutlinedButton(onClick = {
            alcance.launch {
                portapapeles.setClipEntry(ClipData.newPlainText("Sala de Charla", invitacion.url).toClipEntry())
            }
        }) {
            Icon(painterResource(R.drawable.ic_copiar), contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(e.sm))
            Text("Copiar")
        }
        Button(onClick = {
            val enviar = Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, "Entra a mi sala de Charla: ${invitacion.url}")
            contexto.startActivity(Intent.createChooser(enviar, null))
        }) {
            Icon(painterResource(R.drawable.ic_compartir), contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(e.sm))
            Text("Compartir")
        }
    }
}

@Composable
private fun Falla(mensaje: String, onReintentar: () -> Unit) {
    val e = CharlaTema.espaciado
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.errorContainer, MaterialTheme.shapes.extraLarge)
            .padding(e.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(e.md)
    ) {
        Icon(
            painterResource(R.drawable.ic_sin_conexion),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.size(40.dp)
        )
        Text(mensaje, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        Text(
            "Revisa que tu servidor y su túnel estén corriendo: docker compose ps",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
        Button(onClick = onReintentar) { Text("Reintentar") }
    }
}
