package com.example.lugares_j.repository

import androidx.lifecycle.MutableLiveData
import com.example.lugares_j.data.LugarDao
import com.example.lugares_j.model.Lugar

class LugarRepository(private val lugarDao: LugarDao) {

     fun saveLugar(lugar: Lugar) {
        lugarDao.saveLugar(lugar)
    }

     fun deleteLugar(lugar: Lugar) {
         lugarDao.deleteLugar(lugar)
    }

    val getLugares: MutableLiveData<List<Lugar>> = lugarDao.getLugares()
}