package yei.poli.edu.botonpanico;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import yei.poli.edu.botonpanico.util.AdminPreferencias;
import yei.poli.edu.botonpanico.util.Constantes;

/**
 * Created by Yeimmy Lee, Javier Becerra - Politécnico Grancolombiano - 2017
 */
public class ConfiguracionBPActivity extends AppCompatActivity {

    /** variable para los logs **/
    private static final String TAG = ConfiguracionBPActivity.class.getSimpleName();

    /** elementos de la vista **/
    private Switch btnEnviarMensaje;
    private Switch btnEnviarCorreo;
    private CheckBox chkAdjuntarAudio;
    private CheckBox chkAdjuntarImagenes;

    /** preferencias **/
    private AdminPreferencias adminPreferencias;

    // para los contactos
    private Uri uriContact;
    private String contactID;
    private String contactName;
    private String contactEmail;
    private String contactNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_bp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // inicializa preferencias
        adminPreferencias = new AdminPreferencias(this);

        // inicializa vista
        inicializarBtnEnviarMensaje();
        inicializarBtnEnviarCorreo();
        inicializarChkAdjuntarAudio();
        inicializarChkAdjuntarImagenes();
        inicializarContactos();
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

    // listener de los checkbox
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

    // Inicializa checkBox adjuntar imagenes
    private void inicializarContactos () {

        String uri = adminPreferencias.obtenerValor(Constantes.CONTACTO1);
        Log.d(TAG, "uri: " + uri);
        if(uri != null) {
            uriContact = Uri.parse(uri);

            consultarIdContacto();
            consultarNombreContacto();
            consultarCorreoContacto();
            consultarTelefonoContacto();
            mostrarInfoContacto();
        }
    }

    /**********************************************************************************************/
    /************************** PERMISOS **********************************************************/
    /**********************************************************************************************/

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constantes.MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // Si la solicitud fue cancelada, el resultado es un arreglo vacío.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // El usuario otorgó los permisos
                    cargarContactos();
                } else {

                    // El usuario negó los permisos
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.max4contactos), Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    /**********************************************************************************************/
    /************************** ADMINISTRACIÓN DE CONTACTOS ***************************************/
    /**********************************************************************************************/

    public void agregarContacto(View v){

        //Se valida inicialmente si tenemos permisos para acceder a los contactos
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //Aquí solicita permisos
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},Constantes.MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            //después de solicitar los permisos llama al método: onRequestPermissionsResult, para proceder de acuerdo a la respuesta del usuario.
        } else {
            //Si tenemos permisos continúa con el proceso normal
            cargarContactos();
        }

    }

    public void cargarContactos() {

        // using native contacts selection
        // Intent.ACTION_PICK = Pick an item from the data, returning what was selected.
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), Constantes.REQUEST_CODE_PICK_CONTACTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constantes.REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            Log.d(TAG, "uri: " + uriContact.toString());

            adminPreferencias.agregarContacto(uriContact.toString());

            consultarIdContacto();
            consultarNombreContacto();
            consultarCorreoContacto();
            consultarTelefonoContacto();
            mostrarInfoContacto();

        }
    }

    private void consultarIdContacto() {

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }
        cursorID.close();
        Log.d(TAG, "Contact ID: " + contactID);

    }

    private void consultarNombreContacto() {

        contactName = null;

        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
        Log.d(TAG, "Contact Name: " + contactName);
    }

    private void consultarTelefonoContacto() {

        contactNumber = null;

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                new String[]{contactID},
                null);
        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        cursorPhone.close();
        Log.d(TAG, "Contact Phone Number: " + contactNumber);
    }

    private void consultarCorreoContacto() {

        contactEmail = null;

        // querying contact data store
        Cursor emailCur = getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS},
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{contactID},
                null);

        while (emailCur.moveToNext()) {
            contactEmail = emailCur.getString(
                    emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
        }
        emailCur.close();
        Log.d(TAG, "Contact Email: " + contactEmail);

    }

    private void mostrarInfoContacto() {

        Bitmap photo = null;

        TextView nombreContacto1 = (TextView) findViewById(R.id.nombreContacto1);
        nombreContacto1.setText(contactName);

        TextView telefonoContacto1 = (TextView) findViewById(R.id.telefonoContacto1);
        telefonoContacto1.setText(contactNumber);

        TextView correoContacto1 = (TextView) findViewById(R.id.correoContacto1);
        correoContacto1.setText(contactEmail);


        ImageView imageView = (ImageView) findViewById(R.id.imagenContacto1);
        imageView.setImageResource(R.mipmap.ic_launcher);

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                //ImageView imageView = (ImageView) findViewById(R.id.imagenContacto1);
                imageView.setImageBitmap(photo);
                inputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
