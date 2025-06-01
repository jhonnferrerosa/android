package com.example.clienteflask

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clienteflask.VariableGlobal.miRespuestaJSON
import com.example.clienteflask.VariableGlobal.miDominioDelaWeb
import android.util.Base64
import com.example.clienteflask.VariableGlobal.miOkHttpClientQucContieneLaCoockie
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity2 : AppCompatActivity() {

    lateinit var miButtonAceptar : Button;
    lateinit var miBotonRechazar: Button;
    lateinit var miTextView1Apodo : TextView;
    lateinit var miTextView4nombreDelEvento : TextView;
    lateinit var miTextView6macDelRobot : TextView;
    lateinit var miTextView5idDelRobot : TextView;
    lateinit var miTextView7esAdministrador : TextView;
    lateinit var miTextView8estado : TextView;
    lateinit var miImageViewFotoDelRobot : ImageView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        println ("mainActivity2, oncreate() ---- comienza");

        miTextView1Apodo = findViewById(R.id.TextView1Apodo);
        miTextView4nombreDelEvento = findViewById(R.id.textView4nombreDelEvento);
        miTextView5idDelRobot = findViewById(R.id.textView5idDelRobot);
        miTextView6macDelRobot = findViewById(R.id.textView6macDelRobot);
        miTextView7esAdministrador = findViewById(R.id.textView7esAdministrador);
        miTextView8estado = findViewById(R.id.textView8estado);
        miImageViewFotoDelRobot = findViewById(R.id.imageViewFotoDelRobot);

        miTextView1Apodo.setText(miRespuestaJSON?.apodoUsuario.toString());
        miTextView4nombreDelEvento.setText(miRespuestaJSON?.nombreEvento.toString());
        miTextView5idDelRobot.setText(miTextView5idDelRobot.text.toString() + miRespuestaJSON?.idRobot.toString());
        miTextView6macDelRobot.setText(miTextView6macDelRobot.text.toString() + miRespuestaJSON?.mac.toString());
        miTextView7esAdministrador.setText(miTextView7esAdministrador.text.toString() + miRespuestaJSON?.correoAdministrador.toString());
        miTextView8estado.setText(miTextView8estado.text.toString() + miRespuestaJSON?.estado.toString());

        println ("mainActivity2, oncreate() ---- se va a visualzar el string de la foto: ");
        println(miRespuestaJSON?.fotoDelRobot.toString());
        var miBitmapDeLaImagen : Bitmap = pasarDeStringAbase64 (miRespuestaJSON?.fotoDelRobot.toString());

        miImageViewFotoDelRobot.setImageBitmap(miBitmapDeLaImagen);

        miButtonAceptar = findViewById(R.id.buttonAceptar);
        miButtonAceptar.setOnClickListener(){
            println ("miButtonAceptar.setOnClickListener()---  ");
            miDominioDelaWeb = "http://192.168.1.129:5000/aceptarrobot/"
            println(miDominioDelaWeb);
            buscarInternetAceptarRobot (miDominioDelaWeb);
        }
        miBotonRechazar = findViewById(R.id.button2rechazar);
        miBotonRechazar.setOnClickListener (){
            println ("miBotonRechazar.setOnClickListener ()--- ");
            println(miDominioDelaWeb);
        }
    }

    fun pasarDeStringAbase64 (parametroString : String?) : Bitmap {
        println ("MainActivity2, pasarDeStringAbase64()---este es el string: ");
        println (parametroString);
        // en esta primera parte convierto el String a una lista de bits.
        var misBytesDeLaFoto = Base64.decode(parametroString, Base64.DEFAULT);
        // En este segunda parte paso la lista de String a Bitmap.
        var miBitMap = BitmapFactory.decodeByteArray(misBytesDeLaFoto, 0, misBytesDeLaFoto.size);
        return  miBitMap;
    }

    fun buscarInternetAceptarRobot(url: String) {
        println("buscarInternetAceptarRobot()--- se ejecuta con la URL: $url");
        println("buscarInternetAceptarRobot()--- se ejecuta con la URL: " + url.substringBeforeLast("/"));



        val retrofit = Retrofit.Builder()
            .baseUrl(url) // Se toma solo la parte base
            .addConverterFactory(GsonConverterFactory.create())
            .client(miOkHttpClientQucContieneLaCoockie)
            .build()

        val miPeticion = PeticionBody (
            //miParametroCodigoQR = miRespuestaJSON?.nombreEvento.toString(),
            //miParametroCorreoElectronicoDelAdministrador = miRespuestaJSON?.correoAdministrador.toString(),
            //miParametroIdRobot = miRespuestaJSON?.idRobot.toString()
            miParametroCodigoQR = "miQR1",
            miParametroCorreoElectronicoDelAdministrador = "holaSoyElCorreoElectronicoDelAdmin",
            miParametroIdRobot = 101
        )

        println ("buscarInternetAceptarRobot()--- Json enviado: " + Gson().toJson(miPeticion));
        println ("buscarInternetAceptarRobot()--- este es el CSFR token: "+ miRespuestaJSON?.csrfToken.toString());
        val csrfToken = miRespuestaJSON?.csrfToken ?: ""
        println("buscarInternetAceptarRobot()---  desdde chatGPT. CSRF-Token enviado: $csrfToken");

        runBlocking {
            try{
                val apiService = retrofit.create(APIservicio::class.java)
                val response = apiService.funcion_aceptarRobot(csrfToken = miRespuestaJSON?.csrfToken.toString(), url = url.substringBeforeLast("/"), peticionBody =  miPeticion);
                if (response.isSuccessful){
                    miRespuestaJSON = response.body();
                    println("buscarInternetAceptarRobot()--- mensaje recibido");
                    println(miRespuestaJSON);
                }else{
                    println("buscarInternetAceptarRobot()--- error en la respuesta:  "+ response.code() );
                    miRespuestaJSON = response.body();
                    println("buscarInternetAceptarRobot()--- error, esta sería la respuesta JSON:  "+ miRespuestaJSON);
                    println("buscarInternetAceptarRobot()--- error en la respuesta:  "+ response.errorBody()?.string());
                }
            } catch (e: Exception){
                println ("buscarInternetAceptarRobot()--- error al recibir el mensaje. ");
            }
        }

        if (miRespuestaJSON != null){
            println("buscarInternetAceptarRobot()--- sí que se ha obtenido respuesta del servidor. ");
            if (miRespuestaJSON!!.estado.toString() == "Robot manejando") {
                val miIntent = Intent(this, MainActivity3::class.java);
                startActivity(miIntent);
            }else{
                println ("buscarInternetAceptarRobot()--- no ha robots. ");
            }

        }else{
            println ("buscarInternetAceptarRobot()--- No se ha obtenido respuesta en el POST. ");
        }
    }
}