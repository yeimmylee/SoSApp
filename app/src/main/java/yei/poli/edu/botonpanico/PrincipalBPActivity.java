package yei.poli.edu.botonpanico;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.Toast;

import yei.poli.edu.botonpanico.servicios.BotonPanicoIntentService;
import yei.poli.edu.botonpanico.servicios.BotonPanicoService;
import yei.poli.edu.botonpanico.util.AdminPreferencias;
import yei.poli.edu.botonpanico.util.Constantes;

/**
 * Created by Yeimmy Lee, Javier Becerra - Politécnico Grancolombiano - 2017
 */
public class PrincipalBPActivity extends AppCompatActivity {

    /** variable para los logs **/
    private static final String TAG = PrincipalBPActivity.class.getSimpleName();

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

        // para el GPS
        //load_GPS_Initialize();

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

                    // para el GPS
                    load_GPS_Initialize();


                    adminPreferencias.guardarValor(Constantes.ACTIVO, "S");

                    // generamos la notificación permanente
                    Intent intent = new Intent(getApplicationContext(), PrincipalBPActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(
                            getBaseContext())
                            .setSmallIcon(R.drawable.logo_notificacion)
                            .setContentTitle("SoSApp")
                            .setContentText(getResources().getString(R.string.seEncuentraEnEjecucion))
                            .setWhen(System.currentTimeMillis())
                            .setContentIntent(pendingIntent)
                            .setOngoing(true);

                    nManager.notify(Constantes.NOTIFICATION_ID, builder.build());

                    //startService(new Intent(getApplicationContext(), BotonPanicoService.class));

                }else{
                    adminPreferencias.guardarValor(Constantes.ACTIVO, "N");
                    //se elimina la notificación permanente
                    nManager.cancel(Constantes.NOTIFICATION_ID);

                    //stopService(new Intent(getApplicationContext(), BotonPanicoService.class));
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




    /**********************************************************************************************/
    /************************** GPS ***************************************************************/
    /**********************************************************************************************/

    private static final int PERMISSION_LOCATION_REQUEST_CODE = 22;

    public static double myCurrentLatitude = 0;
    public static double myCurrentLongitude = 0;

    /*Region GPS*/
    public void load_GPS_Initialize() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_REQUEST_CODE );
        } else {
            consultaUbicacion();
        }
    }

    public void consultaUbicacion() {

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Define a listener that responds to location updates
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    newLocationChanged(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION_REQUEST_CODE: {
                // Si la solicitud fue cancelada, el resultado es un arreglo vacío.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // El usuario otorgó los permisos
                    consultaUbicacion();
                } else {

                    // El usuario negó los permisos
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.msjPermisos), Toast.LENGTH_LONG).show();

                    //desactiva la app
                    btnActivarBP.setChecked(false);
                    adminPreferencias.guardarValor(Constantes.ACTIVO, "N");
                    nManager.cancel(Constantes.NOTIFICATION_ID);
                }
                return;
            }
        }
    }



    public static void newLocationChanged(Location location) {
        Log.d(TAG, "newLocationChanged ");
        myCurrentLatitude = location.getLatitude();
        myCurrentLongitude = location.getLongitude();
    }


}
