package mx.tec.charla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import mx.tec.charla.ui.navigation.CharlaNavHost
import mx.tec.charla.ui.theme.CharlaTheme

/** `@AndroidEntryPoint`: sin esto, `hiltViewModel()` no tiene de dónde sacar los ViewModels. */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { CharlaTheme { CharlaNavHost() } }
    }
}
