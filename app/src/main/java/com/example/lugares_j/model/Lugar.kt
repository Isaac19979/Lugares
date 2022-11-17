package com.example.lugares_j.model

import android.os.Parcelable
//import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lugar(
    var id: String, //firebase les asigna una clave alpha numerica entonces tiene que ser string
    val nombre: String,
    val correo: String?, //El signo de pregunta me dice el campo puede ser nulo en la base de datos
    val telefono: String?,
    val web: String?,
    val latitud: Double?,
    val longitud: Double?,
    val altura: Double?,
    val ruta_audio: String?,
    val ruta_imagen: String?
) : Parcelable {
    constructor () :
            this("", "", "", "", "", 0.0, 0.0, 0.0, "", "")
}
