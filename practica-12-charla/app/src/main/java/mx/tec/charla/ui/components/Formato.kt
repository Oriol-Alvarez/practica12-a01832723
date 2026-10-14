package mx.tec.charla.ui.components

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** «14:05». `en` viene en segundos (Python); Date quiere milisegundos. */
fun horaDe(en: Double): String =
    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date((en * 1000).toLong()))

/** «quad-sic-well-accuracy.trycloudflare.com», sin el https:// que nadie necesita leer. */
fun sinEsquema(servidor: String): String = servidor.substringAfter("://")
