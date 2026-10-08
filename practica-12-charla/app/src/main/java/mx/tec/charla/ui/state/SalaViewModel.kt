package mx.tec.charla.ui.state

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import mx.tec.charla.domain.Solicitud

/**
 * Todavía no hace nada: la pantalla ya se dibuja, pero no hay sala detrás.
 * Lo escribes en el B4 y lo completas en el C2.
 */
@HiltViewModel
class SalaViewModel @Inject constructor() : ViewModel() {

    private val _ui = MutableStateFlow(SalaUiState())
    val ui: StateFlow<SalaUiState> = _ui.asStateFlow()

    fun alAparecer() {}

    fun alDesaparecer() {}

    fun enviar(texto: String): Boolean = false

    fun aprobar(solicitud: Solicitud) {}

    fun rechazar(solicitud: Solicitud) {}

    fun invitar() {}

    fun cerrarInvitacion() {}

    fun reiniciar() {}
}
