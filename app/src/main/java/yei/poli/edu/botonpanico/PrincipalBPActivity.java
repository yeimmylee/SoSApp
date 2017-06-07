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

    private void inicializarBtnActivarBP () {

        btnActivarBP = (Switch) findViewById(R.id.activarBP);

        //Se le agrega el listener para que opere según la selección del usuario
        btnActivarBP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    adminPreferencias.guardarValor(Constantes.ACTIVO, "S");

                    // generamos la notificación
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






    /**********************************************************************************************/
    /************************** ADMINISTRAR PREFERENCIAS ******************************************/
    /********************************************************************************************** /

    public void inicializarPreferencias() {
        preferenciasBP = getSharedPreferences(Constantes.PREFS_BP, 0);
    }

    public void guardarValor(String clave, String valor) {

        editor = preferenciasBP.edit();

        switch (clave) {
            case Constantes.ACTIVO :
                editor.putString(Constantes.ACTIVO, valor);
                break;
            case Constantes.ENVIAR_MENSAJE :
                editor.putString(Constantes.ENVIAR_MENSAJE, valor);
                break;
            case Constantes.ENVIAR_CORREO :
                editor.putString(Constantes.ENVIAR_CORREO, valor);
                break;
            case Constantes.ADJUNTAR_IMAGEN :
                editor.putString(Constantes.ADJUNTAR_IMAGEN, valor);
                break;
            case Constantes.ADJUNTAR_AUDIO :
                editor.putString(Constantes.ADJUNTAR_AUDIO, valor);
                break;
            case Constantes.CONTACTOS :
                editor.putString(Constantes.CONTACTOS, valor);
                break;
            case Constantes.HISTORIAS :
                editor.putString(Constantes.HISTORIAS, valor);
                break;
        }

        editor.commit();

    }

    public String obtenerValor(String clave) {

        switch (clave) {
            case Constantes.ACTIVO :
                return preferenciasBP.getString(Constantes.ACTIVO, "N");
            case Constantes.ENVIAR_MENSAJE :
                return preferenciasBP.getString(Constantes.ENVIAR_MENSAJE, "N");
            case Constantes.ENVIAR_CORREO :
                return preferenciasBP.getString(Constantes.ENVIAR_CORREO, "N");
            case Constantes.ADJUNTAR_IMAGEN :
                return preferenciasBP.getString(Constantes.ADJUNTAR_IMAGEN, "N");
            case Constantes.ADJUNTAR_AUDIO :
                return preferenciasBP.getString(Constantes.ADJUNTAR_AUDIO, "N");
            case Constantes.CONTACTOS :
                return preferenciasBP.getString(Constantes.CONTACTOS, "0");
            case Constantes.HISTORIAS :
                return preferenciasBP.getString(Constantes.HISTORIAS, "0");
        }

        return null;

    }
*/
}
