package com.example.clienteflask

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clienteflask.VariableGlobal.miRespuestaJSON

class MainActivity2 : AppCompatActivity() {

    lateinit var miButtonAceptar : Button;
    lateinit var miTextView1Apodo : TextView;
    lateinit var miTextView4nombreDelEvento : TextView;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        miTextView1Apodo = findViewById(R.id.TextView1Apodo);
        miTextView4nombreDelEvento = findViewById(R.id.textView4nombreDelEvento)


        miTextView1Apodo.setText(miRespuestaJSON?.apodoUsuario.toString());
        miTextView4nombreDelEvento.setText(miRespuestaJSON?.nombreEvento.toString());


        miButtonAceptar = findViewById(R.id.buttonAceptar);

        miButtonAceptar.setOnClickListener(){
            println ("miButtonAceptar.setOnClickListener()--- se ha pulsado, voy a mostrar el valor de la variable global. ");
            println(miRespuestaJSON.toString());




        }
    }
}