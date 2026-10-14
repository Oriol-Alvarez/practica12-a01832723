package mx.tec.charla.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** La inicial de alguien en un círculo, con SU color. */
@Composable
fun Avatar(nombre: String, modifier: Modifier = Modifier, tamano: Dp = 32.dp) {
    val (fondo, texto) = coloresDe(nombre)
    Box(
        modifier = modifier.size(tamano).background(fondo, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = nombre.take(1).uppercase(),
            style = if (tamano >= 56.dp) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.labelLarge,
            color = texto
        )
    }
}

/**
 * El mismo nickname tiene siempre el mismo color, en todos los teléfonos:
 * sale de su `hashCode`, no del azar. Así se reconoce a alguien sin leer.
 * Solo roles del tema: en modo oscuro siguen teniendo contraste.
 */
@Composable
fun coloresDe(nombre: String): Pair<Color, Color> {
    val c = MaterialTheme.colorScheme
    val opciones = listOf(
        c.primaryContainer to c.onPrimaryContainer,
        c.tertiaryContainer to c.onTertiaryContainer,
        c.secondaryContainer to c.onSecondaryContainer,
        c.inverseSurface to c.inverseOnSurface
    )
    return opciones[Math.floorMod(nombre.lowercase().hashCode(), opciones.size)]
}
