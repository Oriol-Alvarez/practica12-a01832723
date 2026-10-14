package mx.tec.charla.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.tec.charla.domain.Solicitud
import mx.tec.charla.ui.theme.CharlaTema

/** Alguien toca la puerta. Solo la ve el anfitrión. */
@Composable
fun TarjetaSolicitud(
    solicitud: Solicitud,
    onAceptar: () -> Unit,
    onRechazar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val e = CharlaTema.espaciado
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        Row(
            modifier = Modifier.padding(e.md),
            horizontalArrangement = Arrangement.spacedBy(e.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(solicitud.nickname, tamano = 40.dp)
            Column(Modifier.weight(1f)) {
                Text(solicitud.nickname, style = MaterialTheme.typography.titleMedium)
                Text(
                    "quiere entrar",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            OutlinedButton(onClick = onRechazar) { Text("No") }
            Button(onClick = onAceptar) { Text("Aceptar") }
        }
    }
}
