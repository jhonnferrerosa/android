package com.example.pruebarobots2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.view.View


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        println ("AppCompatActivity()--- comienza la ejecucion de prgrama. ");
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    fun imprimirMensajePrueba (view: View){
        println("imprimirMensajePrueba() --- se  esta imprimiendo el mansaje de pruebaaa. ");

    }

    fun btnUpClick (view: View){
        println ("btnUpClick()---   se ha pulsado. ");
    }

    fun btnDownClick (view: View){
        println("btnDownClick()--- se ha pulsado. ");
    }

    fun btnCancelClick () {
        println("btnCancelClick()--- se ha pulsado. ");
    }
}




