package com.example.lugares_j.ui.lugar

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.lugares_j.R
import com.example.lugares_j.databinding.FragmentAddLugarBinding
import com.example.lugares_j.databinding.FragmentLugarBinding
import com.example.lugares_j.databinding.FragmentUpdateLugarBinding
import com.example.lugares_j.model.Lugar
import com.example.lugares_j.viewmodel.LugarViewModel


class UpdateLugarFragment : Fragment() {

    //Se define un objeto para obtener los argumentos pasados al fragmento
    private val args by navArgs<UpdateLugarFragmentArgs>()


    //El objeto para interactuar finalmente con la tabla...
    private lateinit var lugarViewModel: LugarViewModel

    private var _binding: FragmentUpdateLugarBinding? = null
    private val binding get() = _binding!!

    //Objeto media Player para escuchar audio desde la nube
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lugarViewModel = ViewModelProvider(this).get(LugarViewModel::class.java)

        _binding = FragmentUpdateLugarBinding.inflate(inflater, container, false)

        binding.etNombre.setText(args.lugar.nombre)
        binding.etCorreo.setText(args.lugar.correo)
        binding.etTelefono.setText(args.lugar.telefono)
        binding.etWeb.setText(args.lugar.web)
        binding.tvLongitud.text = args.lugar.longitud.toString()
        binding.tvLatitud.text = args.lugar.latitud.toString()
        binding.tvAltura.text = args.lugar.altura.toString()

        binding.btUpdate.setOnClickListener { updateLugar() }
        binding.btDelete.setOnClickListener { deleteLugar() }

        binding.btEmail.setOnClickListener { escribirCorreo() }
        binding.btPhone.setOnClickListener { llamarLugar() }
        binding.btWhatsapp.setOnClickListener { enviarWhatsApp() }
        binding.btWeb.setOnClickListener { verWeb() }
        binding.btLocation.setOnClickListener { verEnMapa() }

        if (args.lugar.ruta_audio?.isNotEmpty()==true){
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(args.lugar.ruta_audio)
            mediaPlayer.prepare()
            binding.btPlay.isEnabled=true
        } else {
            binding.btPlay.isEnabled=false
        }
        binding.btPlay.setOnClickListener{mediaPlayer.start()}

        if (args.lugar.ruta_imagen?.isNotEmpty()==true){
            Glide.with(requireContext())
                .load(args.lugar.ruta_imagen)
                .fitCenter()
                .into(binding.imagen)
        }

        return binding.root
    }

    private fun escribirCorreo() {
        val valor = binding.etCorreo.text.toString()
        if(valor.isNotEmpty()) { //si el correo tiene algo... entonces se intenta enviarcorreo
            val intent = Intent(Intent.ACTION_SEND)
            intent.type ="message/rfc822" //significa correo electronico para android
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(valor))
            intent.putExtra(Intent.EXTRA_SUBJECT,
            getString(R.string.msg_saludos)+" "+binding.etNombre.text)
            intent.putExtra(Intent.EXTRA_TEXT,
              getString(R.string.msg_mensaje_correo))
            startActivity(intent)
        }else{ //Si no hay info no se puede realizar la accion
            Toast.makeText(requireContext(),
            getString(R.string.msg_data),Toast.LENGTH_LONG).show()
        }
    }

    private fun llamarLugar() {

    }

    private fun enviarWhatsApp() {
        val valor = binding.etTelefono.text.toString()
        if(valor.isNotEmpty()) { //si el telefono tiene algo... entonces se intenta enviar mensaje
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = "whatsapp://send?phone=506$valor&text="+ //esto es necesario para whatsapp
                getString(R.string.msg_saludos)
            intent.setPackage("com.whatsapp")
            intent.data = Uri.parse(uri)

            startActivity(intent)
        }else{ //Si no hay info no se puede realizar la accion
            Toast.makeText(requireContext(),
                getString(R.string.msg_data),Toast.LENGTH_LONG).show()
        }
    }

    private fun verWeb() {
        val valor = binding.etWeb.text.toString()
        if(valor.isNotEmpty()) { //si el sitio web tiene algo... entonces se intenta ingresar

            val uri = "http://$valor"+ //esto es necesario para ver el sitio web
                    getString(R.string.msg_saludos)

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        }else{ //Si no hay info no se puede realizar la accion
            Toast.makeText(requireContext(),
                getString(R.string.msg_data),Toast.LENGTH_LONG).show()
        }
    }

    private fun verEnMapa() {

    }

    private fun deleteLugar() {
        val alerta = AlertDialog.Builder(requireContext())
        alerta.setTitle(R.string.bt_delete_lugar)
        alerta.setMessage(getString(R.string.msg_pregunta_delete)+ " ${args.lugar.nombre}?")
        alerta.setPositiveButton(getString(R.string.msg_si)){_,_ ->
            lugarViewModel.deleteLugar(args.lugar) //Efectivamente borra el lugar
            Toast.makeText(requireContext(),getString(R.string.msg_lugar_deleted),Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateLugarFragment_to_nav_lugar)
        }
        alerta.setNegativeButton(getString(R.string.msg_no)){_,_ ->}
        alerta.create().show()
    }

    private fun updateLugar() {
        val nombre = binding.etNombre.text.toString() //Obtiene el texto de lo que el usuario escrib??o
        if (nombre.isNotEmpty()) { //Si se escribi?? algo en el nombre se puede guardar
            val correo = binding.etCorreo.text.toString() //Obtiene el texto de lo que el usuario escrib??o
            val telefono = binding.etTelefono.text.toString() //Obtiene el texto de lo que el usuario escrib??o
            val web = binding.etWeb.text.toString() //Obtiene el texto de lo que el usuario escrib??o
            val lugar = Lugar(args.lugar.id,nombre,correo,telefono,web,
                args.lugar.latitud,
                args.lugar.longitud,
                args.lugar.altura,
                args.lugar.ruta_audio,
                args.lugar.ruta_imagen)

            //Se procede a actualizar el nuevo lugar
            lugarViewModel.saveLugar(lugar)
            Toast.makeText(
                requireContext(),
                getString(R.string.msg_lugar_updated),
                Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateLugarFragment_to_nav_lugar) //Nos devuelve a la pantalla principal

        }else{ //No se puede actualizar el lugar... falta info
            Toast.makeText(requireContext(), getString(R.string.msg_data), Toast.LENGTH_LONG).show()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}