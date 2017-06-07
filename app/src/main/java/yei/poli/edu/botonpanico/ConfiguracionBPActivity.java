package yei.poli.edu.botonpanico;

import android.Manifest;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import yei.poli.edu.botonpanico.util.AdminPreferencias;
import yei.poli.edu.botonpanico.util.Constantes;

/**
 * Created by Yeimmy Lee, Javier Becerra - Politécnico Grancolombiano - 2017
 */
public class ConfiguracionBPActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /** para los permisos **/
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    /** para los contactos **/
    static final int REQUEST_SELECT_CONTACT = 1;
    static final int DETAILS_QUERY_ID = 0;
    Uri contactUri = null;

    /** elementos de la vista **/
    private Switch btnEnviarMensaje;
    private Switch btnEnviarCorreo;
    private CheckBox chkAdjuntarAudio;
    private CheckBox chkAdjuntarImagenes;

    /** preferencias **/
    AdminPreferencias adminPreferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_bp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // inicializa preferencias
        adminPreferencias = new AdminPreferencias(this);

        // inicializa vista
        inicializarBtnEnviarMensaje();
        inicializarBtnEnviarCorreo();
        inicializarChkAdjuntarAudio();
        inicializarChkAdjuntarImagenes();
    }


    /**********************************************************************************************/
    /************************** PROCESA OBJETOS DE LA VISTA ***************************************/
    /**********************************************************************************************/

    // Inicializa botón enviar mensaje
    private void inicializarBtnEnviarMensaje () {

        btnEnviarMensaje = (Switch) findViewById(R.id.enviarMensaje);

        //Se le agrega el listener para que opere según la selección del usuario
        btnEnviarMensaje.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    adminPreferencias.guardarValor(Constantes.ENVIAR_MENSAJE, "S");
                }else{
                    adminPreferencias.guardarValor(Constantes.ENVIAR_MENSAJE, "N");
                }
            }
        });

        //valida el estado actual para mostrarlo
        if(adminPreferencias.obtenerValor(Constantes.ENVIAR_MENSAJE).equals("S")) {
            btnEnviarMensaje.setChecked(true);
        } else {
            btnEnviarMensaje.setChecked(false);
        }
    }

    // Inicializa botón enviar correos
    private void inicializarBtnEnviarCorreo () {

        btnEnviarCorreo = (Switch) findViewById(R.id.enviarCorreo);

        //Se le agrega el listener para que opere según la selección del usuario
        btnEnviarCorreo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    adminPreferencias.guardarValor(Constantes.ENVIAR_CORREO, "S");
                }else{
                    adminPreferencias.guardarValor(Constantes.ENVIAR_CORREO, "N");
                }
            }
        });

        //valida el estado actual para mostrarlo
        if(adminPreferencias.obtenerValor(Constantes.ENVIAR_CORREO).equals("S")) {
            btnEnviarCorreo.setChecked(true);
        } else {
            btnEnviarCorreo.setChecked(false);
        }
    }

    // Inicializa checkBox adjuntar audio
    private void inicializarChkAdjuntarAudio () {

        chkAdjuntarAudio = (CheckBox) findViewById(R.id.adjuntarAudio);

        //valida el estado actual para mostrarlo
        if(adminPreferencias.obtenerValor(Constantes.ADJUNTAR_AUDIO).equals("S")) {
            chkAdjuntarAudio.setChecked(true);
        } else {
            chkAdjuntarAudio.setChecked(false);
        }
    }

    // Inicializa checkBox adjuntar imagenes
    private void inicializarChkAdjuntarImagenes () {

        chkAdjuntarImagenes = (CheckBox) findViewById(R.id.adjuntarImagenes);

        //valida el estado actual para mostrarlo
        if(adminPreferencias.obtenerValor(Constantes.ADJUNTAR_IMAGEN).equals("S")) {
            chkAdjuntarImagenes.setChecked(true);
        } else {
            chkAdjuntarImagenes.setChecked(false);
        }
    }

    public void onCheckboxClicked(View view) {

        // averigua si fue marcado o desmarcado
        boolean checked = ((CheckBox) view).isChecked();

        // verifica cuál checkbox fue modificado
        switch(view.getId()) {
            case R.id.adjuntarAudio:
                if (checked)
                    adminPreferencias.guardarValor(Constantes.ADJUNTAR_AUDIO, "S");
                else
                    adminPreferencias.guardarValor(Constantes.ADJUNTAR_AUDIO, "N");
                break;
            case R.id.adjuntarImagenes:
                if (checked)
                    adminPreferencias.guardarValor(Constantes.ADJUNTAR_IMAGEN, "S");
                else
                    adminPreferencias.guardarValor(Constantes.ADJUNTAR_IMAGEN, "N");
                break;
        }
    }


    public void agregarContacto(View v){

        //Se valida inicialmente si tenemos permisos para acceder a los contactos
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //Aquí solicita permisos
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            //después de solicitar los permisos llama al método: onRequestPermissionsResult, para proceder de acuerdo a la respuesta del usuario.
        } else {
            //Si tenemos permisos continúa con el proceso normal
            cargarContactos();
        }
    }

    public void cargarContactos () {

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_CONTACT);
        }
    }


    /**********************************************************************************************/
    /************************** PROCESA LA RESPUESTA **********************************************/
    /**********************************************************************************************/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            contactUri = data.getData();
            Toast.makeText(getBaseContext(), "Aquí proceso el contacto", Toast.LENGTH_LONG).show();
            System.out.println("Aquí proceso el contacto " + contactUri);

            //getLoaderManager().initLoader(DETAILS_QUERY_ID, null, this);


        }
    }


    /**********************************************************************************************/
    /************************** PERMISOS **********************************************************/
    /**********************************************************************************************/
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // Si la solicitud fue cancelada, el resultado es un arreglo vacío.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // El usuario otorgó los permisos
                    cargarContactos();
                } else {

                    // El usuario negó los permisos
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.msjPermisos), Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    /**********************************************************************************************/
    /************************** PROCESA EL LOADER *************************************************/
    /**********************************************************************************************/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
