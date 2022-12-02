package com.example.lugares_j.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lugares_j.databinding.FragmentGalleryBinding
import com.example.lugares_j.model.Lugar
import com.example.lugares_j.viewmodel.GalleryViewModel
import com.example.lugares_j.viewmodel.LugarViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GalleryFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    //Un objeto para interacturar con la vista  -mapa-
    private lateinit var googleMap: GoogleMap
    private var mapReady = false;

    //Se utiliza lugarViewmOdel donde está el arrayList de lugares
    private lateinit var lugarViewModel: LugarViewModel

    //Programar la función para solicitar la "descarga" del objeto mapa

    override fun onActivityCreated(savedInstanceState: Bundle?) { //Sirve para descargar o atualizar el mapa
        super.onActivityCreated(savedInstanceState)
        binding.map.onCreate(savedInstanceState)
        binding.map.onResume()
        binding.map.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {//se ejecuta cuando el mapa se descarga
        map.let {
            googleMap = it
            mapReady = true

            //Se recorre el arreglo para dibujar los lugares
            lugarViewModel.getLugares.observe(viewLifecycleOwner) { lugares ->
                updatedMap(lugares)
            }
        }
    }

    private fun updatedMap(lugares: List<Lugar>) {
        if(mapReady){
            lugares.forEach { lugar ->
                if(lugar.latitud?.isFinite()==true && lugar.longitud?.isFinite() == true) {
                    val marca =LatLng(lugar.latitud,lugar.longitud)
                    googleMap.addMarker(MarkerOptions().position(marca).title(lugar.nombre))
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel =
            ViewModelProvider(this)[lugarViewModel::class.java]

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

