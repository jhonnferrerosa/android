package com.example.clienteflask

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clienteflask.VariableGlobal.miRespuestaJSON
import com.example.clienteflask.VariableGlobal.miDominioDelaWeb
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

object VariableGlobal{
    var miRespuestaJSON : FlaskResponse? = null;
    lateinit var miDominioDelaWeb : String;
}

class MainActivity : AppCompatActivity() {

    lateinit var miButton1leerCodigoQR : Button;
    lateinit var miTextView1direccionIP : TextView;
    lateinit var miButton2accederAlEvento : Button;
    lateinit  var direccionURL : String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        miButton1leerCodigoQR = findViewById(R.id.button1leerCodigoQR);
        miTextView1direccionIP = findViewById(R.id.textView1direccionIP);
        miButton2accederAlEvento = findViewById(R.id.button2accederAlEvento);
        miButton2accederAlEvento.isEnabled  = false;

        miButton1leerCodigoQR.setOnClickListener(){
            println("miButton1leerCodigoQR.setOnClickListener()--- empieza el escaneo. ");
            //scanearCodigo();
            miTextView1direccionIP.setText("http://192.168.183.64:5000/demostracionesroboticas/miQR1/");
            direccionURL = "http://192.168.183.64:5000/demostracionesroboticas/miQR1/";
            miButton2accederAlEvento.isEnabled  = true;
            println("miButton1leerCodigoQR.setOnClickListener()--- fin del escaneo. ");
        }

        miButton2accederAlEvento.setOnClickListener(){
            println ("miButton2accederAlEvento.setOnClickListener()--- se ha pulsado el boton, esta es la direcccion: $direccionURL");
            miDominioDelaWeb = conseguirDominio (direccionURL);
            buscarInternet (direccionURL);
        }
    }

    fun scanearCodigo (){
        var miOptions = ScanOptions();
        miOptions.setBeepEnabled(true);
        miOptions.setOrientationLocked(true);
        miOptions.setCaptureActivity(Capturar::class.java);
        barraLanzadora.launch(miOptions);
    }

    private val barraLanzadora = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            var miStringURL: String;
            miStringURL = result.contents;
            println ("barraLanzadora = registerForActivityResult(ScanContract())---uno:  $miStringURL");
            direccionURL = miStringURL;
            direccionURL = direccionURL + "/";
            println ("barraLanzadora = registerForActivityResult(ScanContract())--- dos:  $direccionURL");

            if (miStringURL.length < 40) {
                miTextView1direccionIP.setText("Error al recibir el código. ");
            } else {
                miTextView1direccionIP.setText(miStringURL);
                miButton2accederAlEvento.isEnabled  = true;
            }
        }
    }
    fun buscarInternet(url: String) {
        println("buscarInternet()--- se ejecuta con la URL: $url")

        println("buscarInternet()--- se ejecuta con la URL: "+ url.substringBeforeLast("/"));

        val retrofit = Retrofit.Builder()
            .baseUrl(url) // Se toma solo la parte base
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        runBlocking {
            try {
                val apiService = retrofit.create(APIservicio::class.java)
                val response = apiService.funcion_registrarse(url.substringBeforeLast("/"))

                if (response.isSuccessful) {
                    miRespuestaJSON = response.body()
                    println("buscarInternet()--- mensaje recibido.")
                    println(miRespuestaJSON)
                } else {
                    println("buscarInternet()---error en la respuesta: ${response.code()}")
                }
            } catch (e: Exception) {
                println("buscarInternet()---error al recibir el mensaje: ${e.message}")
            }
        }

        if (miRespuestaJSON != null) {
            println(miRespuestaJSON!!.estado.toString())
            if (miRespuestaJSON!!.estado.toString() == "Robot Listo"){
                val miIntent = Intent(this, MainActivity2::class.java)
                startActivity(miIntent)
            } else {
                println("buscarInternet()--- esperando robot.")
            }
        } else {
            println("buscarInternet()--- el JSON ha sido NULL.")
        }
    }

    // con esta función lo que hago es conseguir solamente el dominio para que lo pueda utilizar el siguiente MainActivity2.
    fun conseguirDominio (url: String): String{
        var objetoURL = URL (url);
        return "${objetoURL.protocol}://${objetoURL.host}:${objetoURL.port}"
    }

}