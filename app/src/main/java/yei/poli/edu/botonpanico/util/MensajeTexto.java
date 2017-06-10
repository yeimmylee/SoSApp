package yei.poli.edu.botonpanico.util;


import android.app.Activity;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import yei.poli.edu.botonpanico.R;

/**
 * Created by Yeimmy Lee, Javier Becerra - Politécnico Grancolombiano - 2017
 */
public class MensajeTexto {

    private Activity activity;

    // constructor
    public MensajeTexto (Activity activity) {
        this.activity = activity;
    }

    public  void sendSmS(String numeroDestino, String ubicacion) {

        //arma el mensaje con el string oconfigurado y la ubicación
        String mensaje = activity.getResources().getString(R.string.mensajeTexto) + " "+ ubicacion;

        Log.d("***numeroDestino***", numeroDestino);
        Log.d("***MensajeTexto***", mensaje);

        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(numeroDestino,null,mensaje,null,null);
            Toast.makeText(activity.getApplicationContext(), "Mensaje Enviado.", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(), "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
            Toast.makeText(activity.getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
