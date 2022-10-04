package com.example.lugares_j.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "lugar")
data class Lugar(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo(name = "nombre")
    val nombre: String,
    @ColumnInfo(name = "correo")
    val correo: String?, //El signo de pregunta me dice el campo puede ser nulo en la base de datos
    @ColumnInfo(name = "telefono")
    val telefono: String?,
    @ColumnInfo(name = "web")
    val web: String?,
    @ColumnInfo(name = "latitud")
    val latitud: Double?,
    @ColumnInfo(name = "longitud")
    val longitud: Double?,
    @ColumnInfo(name = "altura")
    val altura: Double?,
    @ColumnInfo(name = "ruta_audio")
    val ruta_audio: String?,
    @ColumnInfo(name = "ruta_imagen")
    val ruta_imagen: String?
) : Parcelable
