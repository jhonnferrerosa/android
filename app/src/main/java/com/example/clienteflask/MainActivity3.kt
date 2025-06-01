package com.example.clienteflask

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clienteflask.VariableGlobal.miRespuestaJSON
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.io.println
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit



class A : Thread{
    constructor(s: String): super (s)

    override fun run() {
        for (i in 1..15){
            println("MainActivity3.kt, Thread () --- "  + name + i);
            try{
                Thread.sleep(5000)
            } catch (e: InterruptedException) {
                println("Hilo interrumpido: " + e.message)
                break;
            }
        }
    }
}


class MainActivity3 : AppCompatActivity() {

    lateinit var miTextView2 : TextView;
    lateinit var miTextView2idDelRobot : TextView;
    lateinit var miTextView3macDelRobot: TextView;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        println ("mainActivity3, oncreate() ---- comienza");

        miTextView2idDelRobot = findViewById(R.id.textView2idDelRobot);
        miTextView3macDelRobot = findViewById(R.id.textView3macDelRobot);

        miTextView2idDelRobot.setText(miTextView2idDelRobot.text.toString() + miRespuestaJSON?.idRobot.toString());
        miTextView3macDelRobot.setText(miTextView3macDelRobot.text.toString() + miRespuestaJSON?.mac.toString());

        var a1 : A=A("hola, soy el hilo secundario");
        a1.start();


    }
}



