package mx.tec.charla.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import mx.tec.charla.R
import mx.tec.charla.ui.theme.CharlaTema
import mx.tec.charla.ui.theme.CharlaTheme

/** Android 17. Todavía no tiene nombre en `Build.VERSION_CODES` de este SDK. */
private const val ANDROID_17 = 37

/**
 * Desde Android 17, una app con `targetSdk 37` no puede hablar con la red
 * local (10.x, 192.168.x… y el 10.0.2.2 del emulador) sin el permiso
 * ACCESS_LOCAL_NETWORK. Sin él, la conexión ni sale del teléfono y la sala
 * diría «Sin conexión» para siempre.
 *
 * Se pide ANTES de mostrar la app: así el primer intento de conexión ya sale
 * con permiso. En Android 16 o menos no hace falta y se pasa directo.
 */
@Composable
fun ConRedLocal(contenido: @Composable () -> Unit) {
    if (Build.VERSION.SDK_INT < ANDROID_17) return contenido()
    val contexto = LocalContext.current
    var concedido by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(contexto, Manifest.permission.ACCESS_LOCAL_NETWORK) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    val pedir = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido = it }
    if (concedido) contenido() else RedLocalScreen(onPermitir = { pedir.launch(Manifest.permission.ACCESS_LOCAL_NETWORK) })
}

@Composable
fun RedLocalScreen(onPermitir: () -> Unit) {
    val c = MaterialTheme.colorScheme
    val e = CharlaTema.espaciado
    Surface(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(e.xxl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(e.lg, Alignment.CenterVertically)
        ) {
            Box(Modifier.size(104.dp).background(c.primaryContainer, CircleShape), contentAlignment = Alignment.Center) {
                Icon(painterResource(R.drawable.ic_globos), contentDescription = null, tint = c.onPrimaryContainer, modifier = Modifier.size(52.dp))
            }
            Text("Charla habla con tu computadora", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
            Text(
                "Tu sala corre en tu red local. Desde Android 17, para llegar a ella la app necesita el permiso de " +
                    "dispositivos cercanos. Si lo negaste, actívalo en Ajustes → Apps → Charla → Permisos.",
                style = MaterialTheme.typography.bodyMedium,
                color = c.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Button(onClick = onPermitir) { Text("Permitir") }
        }
    }
}

@Preview(showBackground = true, heightDp = 640)
@Composable
private fun RedLocalPreview() {
    CharlaTheme { RedLocalScreen(onPermitir = {}) }
}
