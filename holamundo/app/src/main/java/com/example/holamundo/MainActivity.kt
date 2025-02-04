package com.example.holamundo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.holamundo.ui.theme.HolamundoTheme

const val miOtraConstante:String = "james";
class Persona (var nombre:String){

}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var miNumero:Int = 25;
            miNumero++;
            println("Este es un numero:  "+  miNumero);
            println ("aqui esta mi numero otra vez:  $miNumero");
            println (String.format("aqui esta mi numero otra vez:  %d", miNumero));
            println("este tambien es mi numero:  " + devolverNumero());
            val miConstante:String = "jhon";
            var mesesDelAno: Array<Int> = arrayOf(1,2,3,4,5,6,7,8,9,10,11,12);
            lateinit var miArrayDos:Array<String>;
            var miOtroArrayConCeros = Array(10){0}
            miArrayDos = arrayOf("1", "2", "3", "4");
            var personaUno:Any = Persona ("juan");
            println (personaUno);
            println (personaUno.toString());

            miFuncionImprimirNumeros (miArrayDos);
            miBotonComponente();
            miBotonComponente2();
        }
    }
}

@Composable
fun miBotonComponente (){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd){
        val context = LocalContext.current
        Button(onClick={
            Toast.makeText(context, "acabas de presionar el boton", Toast.LENGTH_SHORT).show()
        }){Text(text = "presione aqui, primerBoton. ")}
    }
}
@Composable
fun miBotonComponente2 (){
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        val context = LocalContext.current
        Button(onClick={
            Toast.makeText(context, "acabas de presionar el boton", Toast.LENGTH_SHORT).show()
        }){Text(text = "presione aqui, segundo ")}
    }
}
@Composable
fun devolverNumero ():Int{
    return 8;
}
@Composable
fun miFuncionImprimirNumeros (args: Array<String>){
    var numeros = args.map { it.toInt() }.toIntArray()
    for (numero in numeros){
        println ("Este es el numero:  " + numero);
    }
}
@Composable
fun miFuncionDameTipoDeDatoQueEs (miObjetoParametro: Any): Any{
    return miObjetoParametro::class.simpleName?:"No se puede saber que tipo de deto utiliza la variable que pasas por parametro.";
}
