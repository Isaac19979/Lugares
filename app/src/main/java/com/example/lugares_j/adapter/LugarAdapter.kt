package com.example.lugares_j.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lugares_j.databinding.LugarFilaBinding
import com.example.lugares_j.model.Lugar
import com.example.lugares_j.ui.lugar.LugarFragmentDirections

class LugarAdapter : RecyclerView.Adapter<LugarAdapter.LugarViewHolder>() {

    //Clase interna que se encarga de finalmente dibujar la informacion
    inner class LugarViewHolder(private val itemBinding: LugarFilaBinding)
        :RecyclerView.ViewHolder(itemBinding.root){
        fun dibuja(lugar: Lugar){
            itemBinding.tvNombre.text = lugar.nombre
            itemBinding.tvCorreo.text = lugar.correo
            itemBinding.tvTelefono.text = lugar.telefono

            Glide.with(itemBinding.root.context)
                .load(lugar.ruta_imagen)
                .circleCrop()
                .into(itemBinding.imageView2)

            itemBinding.vistaFila.setOnClickListener {
                // Creo una accion para navegar a updateLugar pasando un argumento Lugar
                val action = LugarFragmentDirections
                    .actionNavLugarToUpdateLugarFragment(lugar)

                //Efectivamente se pasa al fragmento...
                itemView.findNavController().navigate(action)
            }
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