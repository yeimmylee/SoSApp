package yei.poli.edu.botonpanico;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import yei.poli.edu.botonpanico.util.AdminPreferencias;
import yei.poli.edu.botonpanico.util.Constantes;
import yei.poli.edu.botonpanico.util.Correo;
import yei.poli.edu.botonpanico.util.ManejoContactos;
import yei.poli.edu.botonpanico.util.MensajeTexto;

/**
 * Created by Yeimmy Lee, Javier Becerra - Politécnico Grancolombiano - 2017
 */
public class AyudaActivity extends AppCompatActivity {

    /** variable para los logs **/
    private static final String TAG = AyudaActivity.class.getSimpleName();

    /** preferencias **/
    private AdminPreferencias adminPreferencias;

    /**  contactos */
    ManejoContactos manejoContactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // inicializa preferencias
        adminPreferencias = new AdminPreferencias(this);

        // contactos
        manejoContactos = new ManejoContactos(this);

    }

    public void enviarPrueba(View v) {

        if(adminPreferencias.obtenerValor(Constantes.ACTIVO).equals("S") ) {

            int cant = Integer.parseInt(adminPreferencias.obtenerValor(Constantes.CONTACTOS));

            if (cant > 0) {

                if(adminPreferencias.obtenerValor(Constantes.ENVIAR_MENSAJE).equals("S") || adminPreferencias.obtenerValor(Constantes.ENVIAR_CORREO).equals("S")) {

                    //averigua localización
                    String loc = "http://maps.google.es/?q="+PrincipalBPActivity.myCurrentLatitude+"%20"+PrincipalBPActivity.myCurrentLongitude;

                        //envía mensaje
                        for (int i = 1; i <= cant; i++) {
                            String uri = adminPreferencias.obtenerValor(Constantes.CONTACTO + i);
                            Log.d(TAG, "uri: " + uri);
                            if (uri != null) {
                                manejoContactos.uriContact = Uri.parse(uri);

                                manejoContactos.consultarIdContacto();
                                manejoContactos.consultarNombreContacto();
                                manejoContactos.consultarCorreoContacto();
                                manejoContactos.consultarTelefonoContacto();
                            }

                            //pregunta si envia mensaje de texto
                            if (adminPreferencias.obtenerValor(Constantes.ENVIAR_MENSAJE).equals("S")) {
                                if(manejoContactos.contactNumber != null) {
                                    MensajeTexto mt = new MensajeTexto(this);
                                    mt.sendSmS(manejoContactos.contactNumber, loc);
                                } else {
                                    Toast.makeText(getBaseContext(), getResources().getString(R.string.contactoSinNumero), Toast.LENGTH_LONG).show();
                                }
                            }

                            //pregunta si envia mensaje de corrreo
                            if (adminPreferencias.obtenerValor(Constantes.ENVIAR_CORREO).equals("S")) {
                                if(manejoContactos.contactEmail != null) {
                                    Correo correo = new Correo(this);
                                    correo.enviarCorreo(manejoContactos.contactEmail, correo.armarCuerpo("", loc));
                                } else {
                                    Toast.makeText(getBaseContext(), getResources().getString(R.string.contactoSinCorreo), Toast.LENGTH_LONG).show();
                                }
                            }

                        }

                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.noMetodoEnvio), Toast.LENGTH_LONG).show();

                }

            } else {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.noHayContactos), Toast.LENGTH_LONG).show();

            }

        } else {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.aplicacionInactiva), Toast.LENGTH_LONG).show();

        }
    }
}
