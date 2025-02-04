package com.example.holamundo2pildoras

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var miValorMiCampoRespuesta: TextView;
    lateinit var miValorMiSalidaLog: TextView; // esto es para tener un control de los errores que de la aplicacion.
    lateinit var miValorMiCampoAleatorio: EditText;
    var intentos = 11;
    var aleatorio = Random.nextInt(1,100);
    lateinit var miValorMiEtiqueta4 : TextView;
    lateinit var miValorMiBotonProbar: Button;
    lateinit var miValorMiEtiqueta6 : TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //val miBotonParaSegundaActividad = findViewById<Button>(R.id.miBotonProbar);
        //miBotonParaSegundaActividad.setOnClickListener {
        //    val miIntent =  Intent (this, MainActivity2::class.java);
        //    startActivity(miIntent);
        //}



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->        //esto es solo para que se mantenga el margen  de la pantalla de todos los botones que cree.
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        miValorMiSalidaLog = findViewById(R.id.salidaLog);

        var miValorMiCampoEdad:EditText = findViewById(R.id.miCampoEdad);
        var miValorMiBoton: Button = findViewById(R.id.miBoton);
        miValorMiBoton.setOnClickListener{
            println("onCreate()--- se ha pulsado el boton. ");
            miValorMiCampoRespuesta = findViewById(R.id.miCampoRespuesta);
            var miEntero_miValorMiCampoEdad  = miValorMiCampoEdad.text.toString().toIntOrNull();
            if (miEntero_miValorMiCampoEdad != null) {
                miEntero_miValorMiCampoEdad = miEntero_miValorMiCampoEdad * 12;
                miValorMiCampoRespuesta.text = miEntero_miValorMiCampoEdad.toString();
            }else{
                miValorMiCampoRespuesta.text = "No has escrito numeros, escriba su edad en años.  ";
            }
        }

        var miValorMiBotonRestar:Button = findViewById(R.id.miBotonRestar);
        miValorMiBotonRestar.setOnClickListener {
            restar();
        }
        var miValorMiBotonSumar:Button = findViewById(R.id.miBotonSumar);
        miValorMiBotonSumar.setOnClickListener {
            sumar();
        }

        miValorMiBotonProbar = findViewById(R.id.miBotonProbar);
        miValorMiBotonProbar.setOnClickListener{
            acertarAleatorio ();
        }
        miValorMiEtiqueta4 = findViewById(R.id.miEtiqueta4);
        miValorMiEtiqueta4.text = miValorMiEtiqueta4.text.toString() + intentos.toString();

        var miValorMiBotonPrimo: Button = findViewById(R.id.miBotonPrimo);
        miValorMiBotonPrimo.setOnClickListener{
            var miValorMiCampoPrimo : EditText = findViewById(R.id.miCampoPrimo);
            calcularCantidadPrimos(miValorMiCampoPrimo.text.toString().toIntOrNull());
        }
    }

    fun restar (){
        miValorMiCampoRespuesta = findViewById(R.id.miCampoRespuesta);
        var miEntero_miValorMiCampoRespuesta  = miValorMiCampoRespuesta.text.toString().toIntOrNull();
        if (miEntero_miValorMiCampoRespuesta == null){
            miValorMiCampoRespuesta.text = "0";
            miValorMiSalidaLog.text = "salida del log: ese campo esta vacio. ";
        }else{
            miEntero_miValorMiCampoRespuesta = miEntero_miValorMiCampoRespuesta - 1;
            miValorMiCampoRespuesta.text = miEntero_miValorMiCampoRespuesta.toString();
            miValorMiSalidaLog.text = "salida del log: SI se han escrito numeros. ";
        }
    }
    fun sumar (){
        miValorMiCampoRespuesta = findViewById(R.id.miCampoRespuesta);
        var miEntero_miValorMiCampoRespuesta  = miValorMiCampoRespuesta.text.toString().toIntOrNull();
        if (miEntero_miValorMiCampoRespuesta == null){
            miValorMiCampoRespuesta.text = "0";
            miValorMiSalidaLog.text = "salida del log: ese campo esta vacio. ";
        }else{
            miEntero_miValorMiCampoRespuesta = miEntero_miValorMiCampoRespuesta + 1;
            miValorMiCampoRespuesta.text = miEntero_miValorMiCampoRespuesta.toString();
            miValorMiSalidaLog.text = "salida del log: SI se han escrito numeros. ";
        }
    }
    fun acertarAleatorio(){
        miValorMiCampoAleatorio = findViewById(R.id.miCampoAleatorio);
        miValorMiBotonProbar = findViewById(R.id.miBotonProbar);
        var miNumero = miValorMiCampoAleatorio.text.toString().toIntOrNull() ?: 0;
        miValorMiEtiqueta4 = findViewById(R.id.miEtiqueta4);
        if (miNumero == aleatorio){
            miValorMiEtiqueta4.text = "Has acertado, fin del juego. "
            miValorMiBotonProbar.isEnabled = false;
        }else{
            intentos = intentos - 1;
            if (miNumero > aleatorio){
                miValorMiEtiqueta4.text = "Te has pasado, te quedan: " + intentos.toString() + " intentos. ";
            }else{
                miValorMiEtiqueta4.text = "Te has quedado corto, te quedan: " + intentos.toString() + " intentos. ";
            }
        }
        if (intentos == 0){
            miValorMiEtiqueta4.text = "Fin del juego, has perdido. ";
            miValorMiBotonProbar.isEnabled = false;
        }
    }
    fun calcularCantidadPrimos (parametroNumero: Int?){
        miValorMiEtiqueta6 = findViewById(R.id.miEtiqueta6);
        var contador : Int = 2;
        var cantidadDePrimosEncontrados = 0;

        if (parametroNumero != null){
            while (contador < parametroNumero){
                if (calcularPrimo(contador) == true){
                    cantidadDePrimosEncontrados++;
                }
                contador++;
            }
            miValorMiEtiqueta6.text = "canidad de primos encontrados:  " + cantidadDePrimosEncontrados.toString();
        }else{
            miValorMiEtiqueta6.text = "No se han escrito numeros en el campo de los primos. ";
        }
    }
    fun calcularPrimo (parametroNumero: Int) : Boolean{
        var esPrimo : Boolean = true;
        for (i in 2 until parametroNumero-1){
            if ((parametroNumero % i) == 0){
                esPrimo = false;
                break;
            }
        }
        return esPrimo;
    }

    fun imprimirMensajePrueba (view: View){
        miValorMiSalidaLog.text = "salida del log: SI se han escrito numeros. ";
        println("imprimirMensajePrueba() --- salida del log: SI se han escrito numeros.");
        val miIntent =  Intent (this, MainActivity2::class.java);
        startActivity(miIntent);
    }

}