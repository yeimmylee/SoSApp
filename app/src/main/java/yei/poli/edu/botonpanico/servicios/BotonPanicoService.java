package yei.poli.edu.botonpanico.servicios;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Yeimmy Lee, Javier Becerra - Politécnico Grancolombiano - 2017
 */
public class BotonPanicoService extends Service {

    /** variable para los logs **/
    private static final String TAG = BotonPanicoService.class.getSimpleName();
    TimerTask timerTask;




    /* Método de acceso */
    public class ParsingBinder extends Binder {
        BotonPanicoService getService() {
            return BotonPanicoService.this;
        }
    }

    private final IBinder binder = new ParsingBinder();



    public BotonPanicoService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "Servicio creado...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Servicio iniciado...");

        Timer timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {

                Log.d(TAG, "servicio sigue activo ...");
            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, 1000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Servicio destruido...");
    }







}
