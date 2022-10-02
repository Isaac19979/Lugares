package com.example.lugares_j

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.lugares_j.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    //Definicion del objeto para hacer la autenticacion
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding //enlace para el activity_main.xml osea los objetos que están visualmetne


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        binding.btRegister.setOnClickListener { haceRegistro() }
        binding.btLogin.setOnClickListener { haceLogin() }
    }

    private fun haceRegistro() {
        //Recuperamos la informacion que ingresó el usuario...
        val email = binding.etEmail.text.toString()
        val clave = binding.etClave.text.toString()

        //Se llama a la funcion para crear un usuario en Firebase(correo/contraseña)
        auth.createUserWithEmailAndPassword(email, clave)
            .addOnCompleteListener(this) { task ->
                var user: FirebaseUser? = null
                if (task.isSuccessful) { //Si pudo crear el usuario
                    Log.d("Autenticando", "Usario creado")
                    user = auth.currentUser    //Recupero la info del usuario creado
                } else {
                    Log.d("Autenticando", "Error al crear el usauario")
                }
                actualiza(user) //si el usuario está me pasa a la siguiente pantalla
            }
    }

    private fun haceLogin() {
        //Recuperamos la informacion que ingresó el usuario...
        val email = binding.etEmail.text.toString()
        val clave = binding.etClave.text.toString()

        //Se llama a la funcion para crear un usuario en Firebase(correo/contraseña)
        auth.signInWithEmailAndPassword(email, clave)
            .addOnCompleteListener(this) { task ->
                var user: FirebaseUser? = null
                if (task.isSuccessful) { //Si pudo crear el usuario
                    Log.d("Autenticando", "Usario autenticado")
                    user = auth.currentUser    //Recupero la info del usuario creado
                } else {
                    Log.d("Autenticando", "Error al autenticar el usauario")
                }
                actualiza(user) //si el usuario está me pasa a la siguiente pantalla
            }
    }

    private fun actualiza(user: FirebaseUser?) {
        //Si hay un usuario definido se pasa a la pantalla principal (Activity)
        if (user != null) {
            //se pasa a la pantalla principal
            val intent = Intent(this, Principal::class.java)
            startActivity(intent)
        }
    }

    //Se ejecuta cuando el app aperezca en la pantalla
    public override fun onStart() {
        super.onStart()
        val usuario = auth.currentUser
        actualiza(usuario)
    }

}