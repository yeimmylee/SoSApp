package yei.poli.edu.botonpanico;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import yei.poli.edu.botonpanico.util.AdminPreferencias;
import yei.poli.edu.botonpanico.util.Constantes;

/**
 * Created by Yeimmy Lee, Javier Becerra - Politécnico Grancolombiano - 2017
 */
public class PrincipalBPActivity extends AppCompatActivity {

    /** elementos de la vista **/
    private Switch btnActivarBP;

    /** preferencias **/
    AdminPreferencias adminPreferencias;

    /** notificación **/
    private static NotificationManager nManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_bp);

        // inicializa preferencias
        adminPreferencias = new AdminPreferencias(this);

        // inicializa notification manager
        nManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // inicializa vista
        inicializarBtnActivarBP();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //método para activar / desactivar la aplicación
    private void inicializarBtnActivarBP () {

        btnActivarBP = (Switch) findViewById(R.id.activarBP);

        //Se le agrega el listener para que opere según la selección del usuario
        btnActivarBP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    adminPreferencias.guardarValor(Constantes.ACTIVO, "S");

                    // generamos la notificación permanente
                    Intent intent = new Intent(getApplicationContext(), PrincipalBPActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(
                            getBaseContext())
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentTitle("SoSApp")
                            .setContentText("Se encuentra en ejecución")
                            .setWhen(System.currentTimeMillis())
                            .setContentIntent(pendingIntent)
                            .setOngoing(true);

                    nManager.notify(Constantes.NOTIFICATION_ID, builder.build());

                }else{
                    adminPreferencias.guardarValor(Constantes.ACTIVO, "N");
                    //se elimina la notificación permanente
                    nManager.cancel(Constantes.NOTIFICATION_ID);
                }
            }
        });

        //valida el estado actual para mostrarlo
        if(adminPreferencias.obtenerValor(Constantes.ACTIVO).equals("S")) {
            btnActivarBP.setChecked(true);
        } else {
            btnActivarBP.setChecked(false);
        }
    }


    /**********************************************************************************************/
    /************************** LLAMADOS A OTROS ACTIVITIES ***************************************/
    /**********************************************************************************************/

    public void mostrarAcercaDe(View v){
        Intent intent = new Intent(this, AcercaDeActivity.class);
        startActivity(intent);
    }

    public void mostrarAyuda(View v){
        Intent intent = new Intent(this, AyudaActivity.class);
        startActivity(intent);
    }

    public void mostrarAlertas(View v){
        Intent intent = new Intent(this, HistorialAlertasListActivity.class);
        startActivity(intent);
    }

    public void mostrarConfiguracion(View v){
        Intent intent = new Intent(this, ConfiguracionBPActivity.class);
        startActivity(intent);
    }

}
