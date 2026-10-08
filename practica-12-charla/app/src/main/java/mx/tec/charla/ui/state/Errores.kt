package mx.tec.charla.ui.state

import java.io.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException

/**
 * Un error de red, en palabras para el usuario.
 *
 * FastAPI explica sus errores en `{"detail": "…"}`. Si es un texto, se usa
 * tal cual: el servidor sabe mejor que la app por qué dijo que no.
 */
fun mensajeDe(e: Exception): String = when (e) {
    is HttpException -> when (e.code()) {
        422 -> "El nickname debe tener de 2 a 20 caracteres"
        else -> detalle(e) ?: "La sala respondió ${e.code()}"
    }
    is IOException -> "No se pudo llegar a la sala. ¿Está corriendo, y la dirección es la correcta?"
    else -> e.message ?: "Algo salió mal"
}

private fun detalle(e: HttpException): String? = runCatching {
    val cuerpo = e.response()?.errorBody()?.string() ?: return null
    Json.parseToJsonElement(cuerpo).jsonObject["detail"]?.jsonPrimitive?.content
}.getOrNull()
