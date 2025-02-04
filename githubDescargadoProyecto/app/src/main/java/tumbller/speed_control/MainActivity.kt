package tumbller.speed_control

import android.os.Bundle
import android.view.View
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
    }

    fun imprimirMensajePrueba (view: View){
        println("imprimirMensajePrueba() --- se  esta imprimiendo el mansaje de pruebaaa. ");
    }

    override fun onDestroy (){
        super.onDestroy();
        Bluetooth.stop();
    }

    fun btnUpClick (view: View){
        println ("btnUpClick()---   se ha pulsado. ");
        Bluetooth.sendCommand(tumbller.speed_control.COMMAND_UP);  //jhonjames, esto lo he modificado yo porque la variable BluetoothKt, No existe en el código.
    }

    fun btnDownClick (view: View){
        println("btnDownClick()--- se ha pulsado. ");
        Bluetooth.sendCommand(tumbller.speed_control.COMMAND_DOWN);
    }

    fun btnCancelClick (view: View) {
        println("btnCancelClick()--- se ha pulsado. ");
        ProgrammableMotion.cancel();
    }
}