package mx.tec.charla.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import mx.tec.charla.domain.EstadoConexion
import mx.tec.charla.ui.theme.CharlaTema

/**
 * Cómo está la conexión, siempre a la vista. Va sobre el encabezado verde:
 * por eso usa los colores «on primary» y los contenedores claros del tema.
 *
 * El punto late mientras la app está intentando: algo está pasando, espera.
 */
@Composable
fun ChipConexion(estado: EstadoConexion, modifier: Modifier = Modifier) {
    val c = MaterialTheme.colorScheme
    val (texto, punto) = when (estado) {
        EstadoConexion.Conectado -> "En línea" to c.inversePrimary
        EstadoConexion.Conectando -> "Conectando…" to c.tertiaryContainer
        is EstadoConexion.Reconectando -> "Reconectando en ${estado.enSegundos} s" to c.tertiaryContainer
        EstadoConexion.Desconectado -> "Sin conexión" to c.errorContainer
        EstadoConexion.Rechazado -> "La sala no te reconoce" to c.errorContainer
    }
    val late = estado == EstadoConexion.Conectando || estado is EstadoConexion.Reconectando
    val pulso by rememberInfiniteTransition(label = "pulso").animateFloat(
        initialValue = 1f,
        targetValue = if (late) 0.25f else 1f,
        animationSpec = infiniteRepeatable(tween(700, easing = LinearEasing), RepeatMode.Reverse),
        label = "pulso"
    )
    val e = CharlaTema.espaciado
    Row(
        modifier = modifier
            .background(c.onPrimary.copy(alpha = 0.16f), CircleShape)
            .padding(horizontal = e.md, vertical = e.xs + 2.dp),
        horizontalArrangement = Arrangement.spacedBy(e.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(8.dp).alpha(pulso).background(punto, CircleShape))
        Text(texto, style = MaterialTheme.typography.labelMedium, color = c.onPrimary)
    }
}
