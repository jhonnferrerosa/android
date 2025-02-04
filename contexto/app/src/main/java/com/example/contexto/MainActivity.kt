package com.example.contexto

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

        var miPersona1 =  Persona();
        miPersona1.setContexto(applicationContext);

        println ("MainActivity : AppCompatActivity() ---- ${miPersona1.getDNI()}");
        println ("MainActivity : AppCompatActivity() ---- ${miPersona1.getNombre()}");
        println ("MainActivity : AppCompatActivity() ---- ${miPersona1.getEdad()}");
        println ("MainActivity : AppCompatActivity() ---- ${miPersona1.getContexto()}");





        var  miLabel2Contexto :TextView = findViewById(R.id.label2contexto);
        miLabel2Contexto.setText(applicationContext.toString());

        var miLabel3contexto : TextView = findViewById(R.id.label3contexto);
        var miContextoDeActividad: Context = this;
        miLabel3contexto.setText(miContextoDeActividad.toString());
    }
}