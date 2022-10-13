package com.example.lugares_j.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lugares_j.databinding.LugarFilaBinding
import com.example.lugares_j.model.Lugar

class LugarAdapter : RecyclerView.Adapter<LugarAdapter.LugarViewHolder>() {

    //Clase interna que se encarga de finalmente dibujar la informacion
    inner class LugarViewHolder(private val itemBinding: LugarFilaBinding)
        :RecyclerView.ViewHolder(itemBinding.root){
        fun dibuja(lugar: Lugar){
            itemBinding.tvNombre.text = lugar.nombre
            itemBinding.tvCorreo.text = lugar.correo
            itemBinding.tvTelefono.text = lugar.telefono
        }
    }

    //Las lista donde están los objetos Lugar a dibujarse...
    private var listaLugares = emptyList<Lugar>()


    //Esta funcion crea "cajitas" para cada lugar... en memoria
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LugarViewHolder {
        val itemBinding = LugarFilaBinding.
        inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return LugarViewHolder(itemBinding)
    }

    //Esta función toma un lugar y lo envia a dibujar...
    override fun onBindViewHolder(holder: LugarViewHolder, position: Int) {
        val lugar= listaLugares[position]
        holder.dibuja(lugar)
    }

    //Esta función devuelve la cantidad de elementos a dibujar...
    override fun getItemCount(): Int {
        return listaLugares.size
    }

    fun setListaLugares(lugares: List<Lugar>){
        this.listaLugares = lugares
        notifyDataSetChanged()
    }

}