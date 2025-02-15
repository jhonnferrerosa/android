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

object VariableGlobal{
    var miRespuestaJSON : FlaskResponse? = null;
}

class MainActivity : AppCompatActivity() {

    lateinit var miButton1leerCodigoQR : Button;
    lateinit var miTextView1direccionIP : TextView;
    lateinit var miButton2accederAlEvento : Button;

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
            scanearCodigo();
            println("miButton1leerCodigoQR.setOnClickListener()--- fin del escaneo. ");
        }

        miButton2accederAlEvento.setOnClickListener(){
            println ("miButton2accederAlEvento.setOnClickListener()--- se ha pulsado el boton. ");
            println(miTextView1direccionIP.text.toString());
            buscarInternet ();
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
            println ("barraLanzadora = registerForActivityResult(ScanContract())---$miStringURL");

            if (miStringURL.length < 40) {
                miTextView1direccionIP.setText("Error al recibir el código. ");
            } else {
                miTextView1direccionIP.setText(miStringURL);
                miButton2accederAlEvento.isEnabled  = true;
            }
        }
    }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.167.64:5000/")  // Solo la base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun buscarInternet() {
        println("buscarInternet()--- se ejecuta, este es el parámetro.")
        val retrofit = getRetrofit();

        runBlocking {
            try {
                val apiService = getRetrofit().create(APIservicio::class.java)
                val response = apiService.funcion_registrarse()

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
                // en esta parte me ha devuelto un robot, por lo tanto voy a pasar a la pantalla en la que se decide si acepto o no el robot.
                println("buscarInternet()--- se va a cambiar de pantalla a la de eleccion.");
                val miIntent =  Intent (this, MainActivity2::class.java);
                startActivity(miIntent);
            }else{
                // en esta parte la que está esperando por un robot.
                println("buscarInternet()---  se va a cambair a la pantalla de error o a la de que se eta esperando un robot. ");
            }
        }else{
            println ("buscarInternet()--- el JSON ha sifo NULL. ");
        }
    }
}