package com.example.contexto

import android.content.Context

class Persona (
    private var DNI: String = "55555555T",
    private var nombre : String = "ana",
    private var edad : Int = 30,
    private  var contexto : Context? = null
){
    // este es un contrcutor secundario.
    constructor(DNI: String, nombre: String) : this(DNI, nombre,  0, null)

    fun getDNI () : String{
        println ("Persona, getDNI()--- este es el contexto de la clase: ");
        println (this);
        println ("Persona, getDNI()--- este es el contexto de la aplicacion: ");
        println (this);
        return this.DNI;
    }
    fun getNombre () : String{
        return  this.nombre;
    }
    fun getEdad (): Int{
        return  this.edad;
    }
    fun getContexto (): Context?{
        return this.contexto;
    }

    fun setDNI (DNI: String){
        this.DNI = DNI;
    }
    fun setNombre (nombre: String){
        this.nombre = nombre
    }
    fun setEdad (edad: Int){
        this.edad = edad;
    }
    fun setContexto (contexto: Context?){
        this.contexto = contexto;
    }

}




