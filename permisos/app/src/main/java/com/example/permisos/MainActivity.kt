package com.example.permisos

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var miValorMiButton1camara: Button = findViewById(R.id.button1camara);
        miValorMiButton1camara.setOnClickListener {
            println("MainActivity ()  .--- se ha pulsado el boton 1 camara. Comeinzo de la ejecucion de  los permisos. ");
            var verdadPermisosValidados = checkPermission();
            if (verdadPermisosValidados == true){
                openCamera();
            }else{
                println("MainActivity ()  .--- no se ha abierto la camara, no se ha acaptado los permisos.   ");
            }
            println("MainActivity ()  .--- se ha pulsado el boton 1 camara. Fin de la ejecucion de los permisos.  ");
        }
    }
    private fun checkPermission () : Boolean{
        var verdad = false;
        // en el caso de que este permiso no haya sido aceptado.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //permiso no aceptado
            verdad = false;
            requestCameraPermision();
        }else{
            // abrir camara porque el permiso ya lo tiene.
            verdad = true;
        }
        return verdad;
    }
    private  fun requestCameraPermision(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
            // el usuario ya ha rechazado los permisos.
            Toast.makeText(this, "Permisos rechazados ", Toast.LENGTH_SHORT).show();
        }else{
            //aqui se piden los permisos.
            println ("MainActivity : AppCompatActivity(), requestCameraPermision()--- en este momento exacto es donde se piden los permisos. ");
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 777);
        }
    }
    private fun openCamera(){
        println("MainActivity : AppCompatActivity(), openCamera() ---- aqui se esta abriendo la camara. ");
        Toast.makeText(this, "abriendo camara", Toast.LENGTH_SHORT).show();
    }
}