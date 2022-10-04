package com.example.lugares_j.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lugares_j.model.Lugar

@Dao

interface LugarDao {
    //Las funciones de bajo nivel para hacer un CRUD
    //Create Read Update Delete

    @Insert(onConflict = OnConflictStrategy.IGNORE) //Si no puede insertarlo que no de error
    suspend fun addLugar(lugar: Lugar)

    @Update(onConflict = OnConflictStrategy.IGNORE) //Si no puede insertarlo que no de error
    suspend fun updateLugar(lugar: Lugar)

    @Delete
    suspend fun deleteLugar(lugar: Lugar)

    @Query("SELECT * FROM LUGAR") //Me devuelve un conjunto de lugares
    fun getLugares() : LiveData<List<Lugar>>

}