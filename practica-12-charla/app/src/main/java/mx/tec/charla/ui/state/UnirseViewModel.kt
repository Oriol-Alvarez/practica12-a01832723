package mx.tec.charla.ui.state

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Todavía no hace nada: lo escribes en el C4. */
@HiltViewModel
class UnirseViewModel @Inject constructor() : ViewModel() {

    private val _ui = MutableStateFlow(UnirseUiState())
    val ui: StateFlow<UnirseUiState> = _ui.asStateFlow()

    fun usarEnlace(texto: String) {}

    fun escanear() {}

    fun leerImagen(imagen: Uri) {}

    fun cambiarNickname(nickname: String) {}

    fun pedirEntrar() {}

    fun cancelar() {}

    fun otraSala() {}
}
