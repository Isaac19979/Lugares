package com.example.lugares_j.ui.lugar

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.lugares_j.R
import com.example.lugares_j.databinding.FragmentAddLugarBinding
import com.example.lugares_j.model.Lugar
import com.example.lugares_j.viewmodel.LugarViewModel
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.lugares.utiles.AudioUtiles
import com.lugares.utiles.ImagenUtiles


class AddLugarFragment : Fragment() {
    //El objeto para interactuar finalmente con la tabla...
    private lateinit var lugarViewModel: LugarViewModel

    private var _binding: FragmentAddLugarBinding? = null
    private val binding get() = _binding!!

    private lateinit var audioUtiles: AudioUtiles
    private lateinit var imagenUtiles: ImagenUtiles
    private lateinit var tomarFotoActivity: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel = ViewModelProvider(this).get(LugarViewModel::class.java)

        _binding = FragmentAddLugarBinding.inflate(inflater, container, false)

        binding.btAdd.setOnClickListener {
            binding.progressBar.visibility = ProgressBar.VISIBLE
            binding.msgMensaje.text = getString(R.string.msg_subiendo_audio)
            binding.msgMensaje.visibility = TextView.VISIBLE
            subeNota() }

        activaGPS()

        audioUtiles = AudioUtiles( //Constructor que requiere el objeto
            requireActivity(),
            requireContext(),
            binding.btAccion,
            binding.btPlay,
            binding.btDelete,
            getString(R.string.msg_graba_audio),
            getString(R.string.msg_detener_audio)
        )

        tomarFotoActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK){
                imagenUtiles.actualizaFoto()
            }
        }

        imagenUtiles = ImagenUtiles( //Le pasa al objeto imagen utiles la informacion
            requireContext(),
            binding.btPhoto,
            binding.btRotaL,
            binding.btRotaR,
            binding.imagen,
            tomarFotoActivity)

        return binding.root
    }

    //Esta funcion sube la nota de audio al storage y pasa la ruta la ruta publica del archivo a la siguiente funcion
    private fun subeNota() {
        val archivoLocal = audioUtiles.audioFile
        if(archivoLocal.exists() && archivoLocal.isFile &&
                archivoLocal.canRead()){
            //Se fija la ruta (uri) del archivoLocal de audio
            val rutaLocal = Uri.fromFile(archivoLocal)

            //Se establece la ruta en la nube de la nota de audio
            val rutaNube = "lugaresApp/${Firebase.auth.currentUser?.email}/audios/${archivoLocal.name}"
            // ejemplo "lugaresApp/juan33@gmail.com/audios/202221117190001.mp3"
            //Se hace la referencia Real...
            val referencia: StorageReference = Firebase.storage.reference.child(rutaNube)

            //se sube el archivo y se establecen los "listen" para saber que hacer...
            referencia.putFile(rutaLocal)
                .addOnSuccessListener {
                    referencia.downloadUrl
                        .addOnSuccessListener {
                            //Se obtiene la ruta publica del archivo
                            val rutaAudio = it.toString()
                            subeImagen(rutaAudio)
                        }
                }
                .addOnFailureListener{
                    subeImagen("")
                }
        }else{ //No hay foto, o hay un error en el archivo o algo que no puede leer
            subeImagen("")
        }
    }

    private fun subeImagen(rutaAudio: String) {
        binding.msgMensaje.text = getString(R.string.msg_subiendo_imagen)
        val archivoLocal = imagenUtiles.imagenFile
        if(archivoLocal.exists() && archivoLocal.isFile &&
            archivoLocal.canRead()){
            //Se fija la ruta (uri) del archivoLocal de foto
            val rutaLocal = Uri.fromFile(archivoLocal)

            //Se establece la ruta en la nube de la nota de foto
            val rutaNube = "lugaresApp/${Firebase.auth.currentUser?.email}/imagenes/${archivoLocal.name}"
            // ejemplo "lugaresApp/juan33@gmail.com/audios/202221117190001.jpg"
            //Se hace la referencia Real...
            val referencia: StorageReference = Firebase.storage.reference.child(rutaNube)

            //se sube el archivo y se establecen los "listen" para saber que hacer...
            referencia.putFile(rutaLocal)
                .addOnSuccessListener {
                    referencia.downloadUrl
                        .addOnSuccessListener {
                            //Se obtiene la ruta publica del archivo
                            val rutaImagen = it.toString()
                            addLugar(rutaAudio,rutaImagen)
                        }
                }
                .addOnFailureListener{
                    addLugar(rutaAudio,"")
                }
        }else{ //No hay foto, o hay un error en el archivo o algo que no puede leer
            addLugar(rutaAudio,"")
        }
    }

    private fun activaGPS() {
        if (requireActivity()
                .checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
            && requireActivity()
                .checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            //si estamos acá hay que pedir autorizacion para hacer la ubicacion gps
            requireActivity()
                .requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), 105
                )
        } else {
            //Si se tienen los permisos se busca la ubicacion gps
            val ubicacion: FusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireContext())
            ubicacion.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    "${location.latitude}".also { binding.tvLatitud.text = it }
                    "${location.longitude}".also { binding.tvLongitud.text = it }
                    "${location.altitude}".also { binding.tvAltura.text = it }
                } else {
                    binding.tvAltura.text = "0.0"
                    binding.tvLongitud.text = "0.0"
                    binding.tvAltura.text = "0.0"
                }
            }
        }
    }

    private fun addLugar(rutaAudio: String, rutaImagen: String) {
        binding.msgMensaje.text = getString(R.string.msg_subiendo_lugar)
        val nombre =
            binding.etNombre.text.toString() //Obtiene el texto de lo que el usuario escribío
        if (nombre.isNotEmpty()) { //Si se escribió algo en el nombre se puede guardar
            val correo =
                binding.etCorreo.text.toString() //Obtiene el texto de lo que el usuario escribío
            val telefono =
                binding.etTelefono.text.toString() //Obtiene el texto de lo que el usuario escribío
            val web = binding.etWeb.text.toString() //Obtiene el texto de lo que el usuario escribío
            val latitud = binding.tvLatitud.text.toString().toDouble()
            val longitud = binding.tvLongitud.text.toString().toDouble()
            val altura = binding.tvAltura.text.toString().toDouble()
            val lugar = Lugar(
                "", nombre, correo, telefono, web,
                latitud, longitud, altura, rutaAudio, rutaImagen
            )

            //Se procede a registrar el nuevo lugar
            lugarViewModel.saveLugar(lugar)
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_lugar_added),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_addLugarFragment_to_nav_lugar) //Nos devuelve a la pantalla principal

        } else { //No se puede registrar el lugar... falta info
            Toast.makeText(requireContext(), getString(R.string.msg_data), Toast.LENGTH_LONG).show()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}