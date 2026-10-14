package mx.tec.charla.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

/**
 * El sistema de diseño entero, en un composable. Todo lo que esté dentro
 * hereda los colores, la tipografía, las formas y el espaciado.
 *
 * `oscuro` sigue al sistema por omisión, pero es un parámetro: las previews
 * lo fijan para ver los dos temas lado a lado.
 *
 * No se usa color dinámico (Material You) a propósito: toma los colores del
 * fondo de pantalla del usuario y la app deja de verse como la app. Para una
 * marca, eso es perder la marca.
 */
@Composable
fun CharlaTheme(
    oscuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalEspaciado provides Espaciado()) {
        MaterialTheme(
            colorScheme = if (oscuro) ColoresOscuros else ColoresClaros,
            typography = TipografiaCharla,
            shapes = FormasCharla,
            content = content
        )
    }
}

/**
 * El atajo para leer los tokens propios igual que los de Material:
 * `CharlaTema.espaciado.lg`, como `MaterialTheme.colorScheme.primary`.
 */
object CharlaTema {
    val espaciado: Espaciado
        @Composable
        @ReadOnlyComposable
        get() = LocalEspaciado.current
}
